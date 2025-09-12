from pydantic import BaseModel, EmailStr
from typing import Optional
from enum import Enum

# Enumerado
class RolEnum(str, Enum):
    Presidente = "Presidente"
    Vocal = "Vocal"
    Coordinador = "Coordinador"
    Voluntario = "Voluntario"

class LoginResultCode(str, Enum):
    LOGIN_OK = "LOGIN_OK"
    LOGIN_USER_NOT_FOUND = "LOGIN_USER_NOT_FOUND"
    LOGIN_INVALID_CREDENTIALS = "LOGIN_INVALID_CREDENTIALS"
    LOGIN_INACTIVE_USER = "LOGIN_INACTIVE_USER"

#####################
# ---- Request -----
#####################

class UsuarioBase(BaseModel):
    nombreUsuario: str
    nombre: str
    apellido: str
    telefono: Optional[str]
    email: str
    rol: Optional[RolEnum]

class UsuarioCreate(UsuarioBase):
    pass

class UsuarioUpdate(UsuarioBase):
    estaActivo: bool
    
class UsuarioDeleteAndUpdateResponse(UsuarioBase):
    mensaje: str
    
class UsuarioResponse(UsuarioBase):
    estaActivo: bool
    generated_password: Optional[str] 
    mensaje: Optional[str] = None

    class Config:
        orm_mode = True



##########################################
# ---- Request Login -----################
##########################################
class LoginRequest(BaseModel):
    identificador: str
    clave: str

class LoginResponse(UsuarioBase):
    loginResult: LoginResultCode
    mensaje: str
   