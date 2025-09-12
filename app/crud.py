from typing import Optional
from pydantic import EmailStr
from sqlalchemy.orm import Session
from . import models, schemas, utils
from app.schemas import LoginResultCode
from app.mapper import SchemaMapper

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
########################################################################################################
# Modificar usuario
########################################################################################################
########################################################################################################
def modificar_usuario(db: Session, user_id: int, datos: schemas.UsuarioUpdate):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()
    print("Estoy en clase crud - Usuario  modificar:")
    print(usuario)

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

########################################################################################################
########################################################################################################
# Baja usuario (Baja lógica)
########################################################################################################
########################################################################################################
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
