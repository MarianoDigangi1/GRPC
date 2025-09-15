from sqlalchemy.orm import relationship
from .database import Base
import enum
from sqlalchemy import (
    Column, Integer, String, Boolean, Enum, Date, DateTime, ForeignKey, Text, Float, func
)


# ---------------- ENUMS ---------------- #
class RolEnum(str, enum.Enum):
    Presidente = "Presidente"
    Vocal = "Vocal"
    Coordinador = "Coordinador"
    Voluntario = "Voluntario"

# ---------------- MODELOS USUARIO ---------------- #
class Usuario(Base):
    __tablename__ = "usuarios"

    id = Column(Integer, primary_key=True, index=True)
    nombreUsuario = Column(String(50), unique=True, nullable=False)
    nombre = Column(String(100), nullable=False)
    apellido = Column(String(100), nullable=False)
    telefono = Column(String(20))
    clave = Column(String(255), nullable=False) 
    email = Column(String(100), unique=True, nullable=False)
    rol = Column(Enum(RolEnum), nullable=False)
    estaActivo = Column(Boolean, default=True, nullable=False)
    
    # Relaciones
    #actividades = relationship("Actividad", back_populates="responsable")
    #asignaciones = relationship("Asignacion", back_populates="usuario")
    #auditorias = relationship("Auditoria", back_populates="usuario")

# ---------------- MODELOS ACTIVIDAD ---------------- #


# ---------------- MODELOS EVENTO ---------------- #
class Evento(Base):
    __tablename__ = "evento"

    id = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(200), nullable=False)
    descripcion = Column(Text)
    fecha_evento = Column(DateTime, nullable=False)

    # Relaciones
    participantes = relationship("EventoUsuario", cascade="all, delete-orphan",
                                 back_populates="evento")
    donaciones = relationship("EventoInventario", cascade="all, delete-orphan",
                              back_populates="evento")

class EventoUsuario(Base):
    __tablename__ = "evento_usuario"

    evento_id = Column(Integer, ForeignKey("evento.id", ondelete="CASCADE"), primary_key=True)
    usuario_id = Column(Integer, ForeignKey("usuarios.id", ondelete="CASCADE"), primary_key=True)

    evento = relationship("Evento", back_populates="participantes")
    usuario = relationship("Usuario")

class EventoInventario(Base):
    __tablename__ = "evento_inventario"

    evento_id = Column(Integer, ForeignKey("evento.id", ondelete="CASCADE"), primary_key=True)
    inventario_id = Column(Integer, ForeignKey("inventario.id"), primary_key=True)
    cantidad_usada = Column(Integer, nullable=False)

    evento = relationship("Evento", back_populates="donaciones")
   