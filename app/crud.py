from typing import Optional
from pydantic import EmailStr
from sqlalchemy.orm import Session
from . import models, schemas, utils
from app.schemas import LoginResultCode, UsuarioResponse



########################################################################################################
########################################################################################################
# Crear usuario
########################################################################################################
########################################################################################################
def crear_usuario(db: Session, usuario: schemas.UsuarioCreate):
    
    # Verificar si el nombreUsuario ya existe
    if db.query(models.Usuario).filter(models.Usuario.nombreUsuario == usuario.nombreUsuario).first():

        print("El nombre de usuario ya existe en la base de datos")
        return UsuarioResponse(
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol,
            estaActivo=False,
            generated_password= "" ,
            mensaje="Error al crear usuario, el nombre de usuario ya existe"
        )

    # Verificar si el email ya existe
    if db.query(models.Usuario).filter(models.Usuario.email == usuario.email).first():
        return schemas.UsuarioResponse(
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol,
            estaActivo=False,
            generated_password= "" ,
            mensaje="Error al crear usuario, el email ya está registrado"
        )

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
    
    # Enviar email con la clave generada
    utils.enviar_email(usuario.email, clave)

    return schemas.UsuarioResponse(
        nombreUsuario=usuario.nombreUsuario,
        nombre=usuario.nombre,
        apellido=usuario.apellido,
        telefono=usuario.telefono,
        email=usuario.email,
        rol=usuario.rol,
        estaActivo=True,
        generated_password=clave,
        mensaje="Usuario creado correctamente"
    )

def obtener_usuarios(db: Session):
    return db.query(models.Usuario).all()

########################################################################################################
########################################################################################################
# Modificar usuario
########################################################################################################
########################################################################################################
def modificar_usuario(db: Session, user_id: int, datos: schemas.UsuarioUpdate):
    usuario = db.query(models.Usuario).filter(models.Usuario.id == user_id).first()
    print("Usuario a modificar:")
    print(usuario)
    if usuario:
        usuario.nombreUsuario = datos.nombreUsuario
        usuario.nombre = datos.nombre
        usuario.apellido = datos.apellido
        usuario.telefono = datos.telefono
        usuario.email = datos.emailUnico
        usuario.rol = datos.rol
        usuario.estaActivo = datos.estaActivo
        db.commit()
        db.refresh(usuario)
    return "Usuario modificado correctamente"

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
        (models.Usuario.nombreUsuario == identificador) | (models.Usuario.emailUnico == identificador)
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
        print("Usuario inactivo")
        return schemas.LoginResponse(
            loginResult=LoginResultCode.LOGIN_INACTIVE_USER,
            mensaje="Usuario inactivo. Contacte al administrador.",
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol
        )

    # Verificar clave con bcrypt
    if not utils.verificar_clave(clave, usuario.clave):
        return schemas.LoginResponse(
            loginResult=LoginResultCode.LOGIN_INVALID_CREDENTIALS,
            mensaje="Credenciales incorrectas",
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.emailUnico,
            rol=usuario.rol
        )
    
    return schemas.LoginResponse(
            loginResult=LoginResultCode.LOGIN_OK,
            mensaje="Login exitoso",
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.emailUnico,
            rol=usuario.rol
        )
