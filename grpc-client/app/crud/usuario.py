from sqlalchemy.orm import Session
from app import models, schemas, utils
from app.schemas import LoginResultCode
from app.mapper import SchemaMapper


# Crear usuario
def crear_usuario(db: Session, usuario: schemas.UsuarioCreate):
    # Verificar si el nombreUsuario ya existe
    if db.query(models.Usuario).filter(models.Usuario.nombreUsuario == usuario.nombreUsuario).first():
        return SchemaMapper.usuario_request_to_usuario_response(
            usuario, False, "", "Error al crear usuario, el nombre de usuario ya existe"
        )

    # Verificar si el email ya existe
    if db.query(models.Usuario).filter(models.Usuario.email == usuario.email).first():
        return SchemaMapper.usuario_request_to_usuario_response(
            usuario, False, "", "Error al crear usuario, el email ya está registrado"
        )

    # Generar y encriptar clave
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

    # Enviar clave por email
    utils.enviar_email(usuario.email, clave)

    return SchemaMapper.usuario_request_to_usuario_response(
        usuario, True, clave, "Usuario creado correctamente"
    )


# Modificar usuario
def modificar_usuario(db: Session, user_id: int, datos: schemas.UsuarioUpdate):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()

    if not usuario:
        return schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario="",
            nombre="",
            apellido="",
            telefono=None,
            email="",
            rol=None,
            mensaje="Usuario no encontrado"
        )

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


# Baja lógica de usuario
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


# Autenticación
def autenticar_usuario(db: Session, identificador: str, clave: str):
    usuario = db.query(models.Usuario).filter(
        (models.Usuario.nombreUsuario == identificador) | (models.Usuario.email == identificador)
    ).first()

    if not usuario:
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
        return SchemaMapper.login_request_to_login_response(
            usuario, LoginResultCode.LOGIN_INACTIVE_USER, "Usuario inactivo. Contacte al administrador."
        )

    if not utils.verificar_clave(clave, usuario.clave):
        return SchemaMapper.login_request_to_login_response(
            usuario, LoginResultCode.LOGIN_INVALID_CREDENTIALS, "Credenciales incorrectas"
        )

    return SchemaMapper.login_request_to_login_response(
        usuario, LoginResultCode.LOGIN_OK, "Login exitoso"
    )


# Listar todos los usuarios
def listar_usuarios(db: Session):
    return db.query(models.Usuario).all()


# Obtener usuario por ID
def obtener_usuario_por_id(db: Session, user_id: int):
    return db.query(models.Usuario).filter(models.Usuario.id == user_id).first()
