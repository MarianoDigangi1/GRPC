from typing import Optional
from pydantic import EmailStr
from sqlalchemy.orm import Session
from sqlalchemy import and_
from . import models, schemas, utils
from app.schemas import LoginResultCode
from app.mapper import SchemaMapper
from datetime import datetime
from sqlalchemy.orm import selectinload

########################################################################################################
########################################################################################################
# Crear usuario
########################################################################################################
########################################################################################################
def crear_usuario(db: Session, usuario: schemas.UsuarioCreate):
    # Verificar si el nombreUsuario ya existe
    if db.query(models.Usuario).filter(models.Usuario.nombreUsuario == usuario.nombreUsuario).first():
        print("El nombre de usuario ya existe en la base de datos")
        return SchemaMapper.usuario_request_to_usuario_response(usuario, False, "", "Error al crear usuario, el nombre de usuario ya existe")

    # Verificar si el email ya existe
    if db.query(models.Usuario).filter(models.Usuario.email == usuario.email).first():
        print("El email ya está registrado en la base de datos")
        return SchemaMapper.usuario_request_to_usuario_response(usuario, False, "", "Error al crear usuario, el email ya está registrado")

    clave = utils.generar_clave()
    hashed_clave = utils.encriptar_clave(clave)

    db_usuario = models.Usuario(
        nombreUsuario=usuario.nombreUsuario,
        nombre=usuario.nombre,
        apellido=usuario.apellido,
        telefono=usuario.telefono,
        clave=hashed_clave,
        email=usuario.email,
        rol=usuario.rol,
        estaActivo=True
    )

    db.add(db_usuario)
    db.commit()
    db.refresh(db_usuario)

    utils.enviar_email(usuario.email, clave)
    return SchemaMapper.usuario_request_to_usuario_response(usuario, True, clave, "Usuario creado correctamente")

########################################################################################################
# Modificar usuario
########################################################################################################

def modificar_usuario(db: Session, user_id: int, datos: schemas.UsuarioUpdate):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()

    if not usuario:
        print("Estoy en clase crud - Usuario no encontrado")
        return schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario="",
            nombre="",
            apellido="",
            telefono=None,
            email="",
            rol=None,
            mensaje="Usuario no encontrado"
        )
    #print(usuario)
    
    if usuario:
        print("Estoy en clase crud - Modificando usuario...")
        # Imprimir información del usuario antes de modificar
        print("Usuario encontrado:")
        print({
            "id": usuario.id,
            "nombreUsuario": usuario.nombreUsuario,
            "nombre": usuario.nombre,
            "apellido": usuario.apellido,
            "email": usuario.email,
            "rol": usuario.rol,
            "estaActivo": usuario.estaActivo
        })
        usuario.nombreUsuario = datos.nombreUsuario
        usuario.nombre = datos.nombre
        usuario.apellido = datos.apellido
        usuario.telefono = datos.telefono
        usuario.email = datos.email
        usuario.rol = datos.rol
        usuario.estaActivo = datos.estaActivo
        db.commit()
        db.refresh(usuario)
    
        return schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol,
            mensaje="Usuario modificado correctamente"
        )

def baja_usuario(db: Session, user_id: int):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()
    
    if not usuario:
        return "Usuario no encontrado"
    
    if not usuario.estaActivo:
        return "El usuario ya está inactivo"

    usuario.estaActivo = False
    db.commit()
    db.refresh(usuario)
    return "Usuario dado de baja correctamente"


#Iniciar sesion
def autenticar_usuario(db: Session, identificador: str, clave: str):
    usuario = db.query(models.Usuario).filter(
        (models.Usuario.nombreUsuario == identificador) | (models.Usuario.email == identificador)
    ).first()

    if not usuario:
        print("Usuario no encontrado en la base de datos")

        return schemas.LoginResponse(
            id=0,
            loginResult=LoginResultCode.LOGIN_USER_NOT_FOUND,
            mensaje="Usuario no encontrado",
            nombreUsuario="",
            nombre="",
            apellido="",
            telefono="",
            email="",
            rol=None
        )

    if not usuario.estaActivo:
        return SchemaMapper.login_request_to_login_response(usuario, LoginResultCode.LOGIN_INACTIVE_USER, "Usuario inactivo. Contacte al administrador.")

    # Verificar clave con bcrypt
    if not utils.verificar_clave(clave, usuario.clave):
        return SchemaMapper.login_request_to_login_response(usuario, LoginResultCode.LOGIN_INVALID_CREDENTIALS, "Credenciales incorrectas")

    return SchemaMapper.login_request_to_login_response(usuario, LoginResultCode.LOGIN_OK, "Login exitoso")

def listar_usuarios(db: Session):
    usuarios = db.query(models.Usuario).all()
    return usuarios

def obtener_usuario_por_id(db: Session, user_id: int):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()
    if not usuario:
        return None
    return usuario
########################################################################################################
    # ---------- Helpers ----------
def _es_futuro(dt: datetime) -> bool:
    return dt > datetime.now()

def _miembro_activo(db: Session, usuario_id: int) -> bool:
    u = db.query(models.Usuario).filter(models.Usuario.id == usuario_id).first()
    return bool(u and u.estaActivo)

# ---------- Crear ----------
def crear_evento(db: Session, data: schemas.EventoCreate):
    fecha = datetime.fromisoformat(data.fecha_evento_iso)
    if not _es_futuro(fecha):
        return None, "La fecha del evento debe ser a futuro."

    evento = models.Evento(
        nombre=data.nombre,
        descripcion=data.descripcion,
        fecha_evento=fecha
    )
    db.add(evento)
    db.flush()  

    
    for uid in set(data.miembros_ids or []):
        if _miembro_activo(db, uid):
            db.add(models.EventoUsuario(evento_id=evento.id, usuario_id=uid))

    db.commit()
    db.refresh(evento)

    miembros_ids = [eu.usuario_id for eu in evento.participantes]
    return SchemaMapper.evento_model_to_response(evento, miembros_ids, "Evento creado correctamente"), None

# ---------- Modificar ----------
def modificar_evento(db: Session, evento_id: int, data: schemas.EventoUpdate, actor_usuario_id: int, actor_rol: str):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return None, "Evento no encontrado."

   
    if data.nombre is not None:
        ev.nombre = data.nombre

    if data.fecha_evento_iso is not None:
        nueva_fecha = datetime.fromisoformat(data.fecha_evento_iso)
        if not _es_futuro(nueva_fecha):
            return None, "La nueva fecha/hora debe ser a futuro."
        ev.fecha_evento = nueva_fecha

    # Asignaciones / bajas de miembros 
    def _puede_gestionar(usuario_id: int) -> bool:
        if actor_rol in ("Presidente", "Coordinador"):
            return True
        return actor_rol == "Voluntario" and usuario_id == actor_usuario_id

    # Agregar
    for uid in set(data.agregar_miembros_ids or []):
        if not _puede_gestionar(uid):
            return None, "No autorizado para asignar este miembro."
        if _miembro_activo(db, uid):
            ya = db.query(models.EventoUsuario).filter(
                and_(models.EventoUsuario.evento_id==ev.id, models.EventoUsuario.usuario_id==uid)
            ).first()
            if not ya:
                db.add(models.EventoUsuario(evento_id=ev.id, usuario_id=uid))

    # Quitar
    for uid in set(data.quitar_miembros_ids or []):
        if not _puede_gestionar(uid):
            return None, "No autorizado para quitar este miembro."
        db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id==ev.id, models.EventoUsuario.usuario_id==uid)
        ).delete()

    # Registrar donaciones si el evento YA PASÓ
    if data.donaciones_usadas:
        if _es_futuro(ev.fecha_evento):
            return None, "Solo se pueden registrar donaciones en eventos pasados."
        for d in data.donaciones_usadas:
            if d.cantidad_usada <= 0:
                return None, "Las cantidades usadas deben ser positivas."
            inv = db.query(models.Inventario).filter(models.Inventario.id==d.inventario_id, models.Inventario.eliminado==False).with_for_update().first()
            if not inv or inv.cantidad < d.cantidad_usada:
                return None, f"Inventario {d.inventario_id} insuficiente o inexistente."
            inv.cantidad -= d.cantidad_usada
            inv.updated_at = datetime.now()
            inv.updated_by = actor_usuario_id
            ya = db.query(models.EventoInventario).filter(
                and_(models.EventoInventario.evento_id==ev.id, models.EventoInventario.inventario_id==d.inventario_id)
            ).first()
            if ya:
                ya.cantidad_usada += d.cantidad_usada
            else:
                db.add(models.EventoInventario(evento_id=ev.id, inventario_id=d.inventario_id, cantidad_usada=d.cantidad_usada))

    db.commit()
    db.refresh(ev)
    miembros_ids = [eu.usuario_id for eu in ev.participantes]
    return SchemaMapper.evento_model_to_response(ev, miembros_ids, "Evento modificado correctamente"), None

# ---------- Baja (física) ----------
def baja_evento(db: Session, evento_id: int):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return "Evento no encontrado."
    if not _es_futuro(ev.fecha_evento):
        return "Solo pueden eliminarse eventos a futuro."
    db.delete(ev)
    db.commit()
    return "Evento eliminado correctamente."

# ---------- Asignar / Quitar miembro (endpoint dedicado) ----------
def asignar_quitar_miembro(db: Session, evento_id: int, usuario_id: int, actor_usuario_id: int, actor_rol: str, agregar: bool):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return "Evento no encontrado."
    def _autz():
        if actor_rol in ("Presidente", "Coordinador"):
            return True
        return actor_rol == "Voluntario" and usuario_id == actor_usuario_id
    if not _autz():
        return "No autorizado."

    if agregar:
        if not _miembro_activo(db, usuario_id):
            return "Solo se pueden asignar miembros activos."
        ya = db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id==evento_id, models.EventoUsuario.usuario_id==usuario_id)
        ).first()
        if ya:
            return "El miembro ya participa del evento."
        db.add(models.EventoUsuario(evento_id=evento_id, usuario_id=usuario_id))
    else:
        db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id==evento_id, models.EventoUsuario.usuario_id==usuario_id)
        ).delete()

    db.commit()
    return "Operación realizada correctamente."

# ---------- Utilidad: al dar de baja un usuario, quitarlo de eventos a futuro ----------
def quitar_usuario_de_eventos_futuros(db: Session, usuario_id: int):
    ahora = datetime.now()
    futuros = db.query(models.EventoUsuario).join(models.Evento, models.EventoUsuario.evento_id==models.Evento.id)\
                  .filter(models.EventoUsuario.usuario_id==usuario_id, models.Evento.fecha_evento > ahora).all()
    for p in futuros:
        db.delete(p)
    db.commit()

def listar_eventos_disponibles(db: Session):
    eventos = db.query(models.Evento).all()
    result = []
    for ev in eventos:
        miembros_ids = [eu.usuario_id for eu in ev.participantes]
        result.append(
            SchemaMapper.evento_model_to_response(ev, miembros_ids)
        )
    return result
   # result = []
    #for ev in eventos:
     #   miembros_ids = [eu.usuario_id for eu in ev.participantes]
        # Reutilizamos el mismo mapper que ya usás en crear/modificar
      #  result.append(SchemaMapper.evento_model_to_response(ev, miembros_ids))
   # return result  

########################################################################################################
########################################################################################################
# Inventario de donaciones
########################################################################################################
########################################################################################################

def create_inventario(db, inventario: schemas.InventarioCreate):
    db_inventario = models.Inventario(
        categoria=inventario.categoria,
        descripcion=inventario.descripcion,
        cantidad=inventario.cantidad,
        eliminado=False,
        fecha_alta=datetime.now(),
        usuario_alta=inventario.usuario_alta
    )
    db.add(db_inventario)
    db.commit()
    db.refresh(db_inventario)
    return db_inventario

def update_inventario(db, inventario_id: int, inventario: schemas.InventarioUpdate):
    db_inventario = db.query(models.Inventario).filter_by(id=inventario_id, eliminado=False).first()
    if db_inventario:
        db_inventario.descripcion = inventario.descripcion
        db_inventario.cantidad = inventario.cantidad
        db_inventario.fecha_modificacion = datetime.now()
        db_inventario.usuario_modificacion = inventario.usuario_modificacion
        db.commit()
        db.refresh(db_inventario)
    return db_inventario

def baja_inventario(db, inventario_id: int, usuario_modificacion: str):
    db_inventario = db.query(models.Inventario).filter_by(id=inventario_id, eliminado=False).first()
    if db_inventario:
        db_inventario.eliminado = True
        db_inventario.fecha_modificacion = datetime.now()
        db_inventario.usuario_modificacion = usuario_modificacion
        db.commit()
        db.refresh(db_inventario)
    return db_inventario

def listar_inventario(db: Session):
    inventarios = db.query(models.Inventario).filter(models.Inventario.eliminado == False).all()
    return inventarios

def obtener_inventario_por_id(db: Session, inventario_id: int):
    inventario = db.query(models.Inventario).filter_by(id=inventario_id).first()
    if not inventario:
        return None, "Inventario no encontrado."
    return inventario, None