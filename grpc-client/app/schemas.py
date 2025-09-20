from pydantic import BaseModel, EmailStr, validator
from typing import Optional, List
from enum import Enum
from datetime import datetime

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

class UsuarioBaseLogin(BaseModel):
    id: int
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

class LoginResponse(UsuarioBaseLogin):
    loginResult: LoginResultCode
    mensaje: str
   


class DonacionUsada(BaseModel):
    inventario_id: int
    cantidad_usada: int

class EventoBase(BaseModel):
    nombre: str
    descripcion: Optional[str]
    fecha_evento_iso: str 

    @validator("fecha_evento_iso")
    def debe_ser_futuro(cls, v):
        try:
            fecha = datetime.fromisoformat(v)
        except Exception:
            raise ValueError("fecha_evento_iso debe estar en ISO 8601")
        return v

class EventoCreate(EventoBase):
    miembros_ids: List[int] = []

class EventoUpdate(BaseModel):
    nombre: Optional[str]
    fecha_evento_iso: Optional[str]
    agregar_miembros_ids: List[int] = []
    quitar_miembros_ids: List[int] = []
    donaciones_usadas: List[DonacionUsada] = [] 

class EventoResponse(BaseModel):
    id: int
    nombre: str
    descripcion: Optional[str]
    fecha_evento_iso: str
    miembros_ids: List[int] = []
    mensaje: Optional[str] = None

    class Config:
        orm_mode = True

class InventarioBase(BaseModel):
    categoria: str
    descripcion: str
    cantidad: int

class InventarioCreate(InventarioBase):
    usuario_alta: str

class InventarioUpdate(BaseModel):
    descripcion: str
    cantidad: int
    usuario_modificacion: str

class InventarioBaja(BaseModel):
    usuario_modificacion: str

class Inventario(InventarioBase):
    id: int
    eliminado: bool
    fecha_alta: str
    usuario_alta: str
    fecha_modificacion: str = None
    usuario_modificacion: str = None

    class Config:
        orm_mode = True