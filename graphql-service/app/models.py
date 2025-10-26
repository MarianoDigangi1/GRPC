from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Text, DateTime, Boolean, ForeignKey, JSON
from sqlalchemy.orm import relationship

Base = declarative_base()

# ==========================================================
#  EVENTO
# ==========================================================
class Evento(Base):
    __tablename__ = "evento"

    id = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(200), nullable=False)
    descripcion = Column(Text)
    fecha_evento = Column(DateTime, nullable=False)
    origen_organizacion_id = Column(String(100), nullable=True)
    vigente = Column(Boolean, default=True)
    publicado = Column(Boolean, default=False)
    evento_id_organizacion_externa = Column(String(100), nullable=True)




# ==========================================================
#  TRANSFERENCIA_DONACION_EXTERNA 
# ==========================================================
class TransferenciaDonacionExterna(Base):
    __tablename__ = "transferencia_donacion_externa"

    id = Column(Integer, primary_key=True, index=True)
    id_transferencia = Column(String(255))
    id_organizacion_origen = Column(String(255))
    id_organizacion_destino = Column(String(255))
    fecha_transferencia = Column(DateTime, nullable=False)
    contenido = Column(JSON, nullable=False)  # JSON con categorías y cantidades
    es_externa = Column(Boolean, default=False)
    vigente = Column(Boolean, default=True)


# ==========================================================
#  USUARIO
# ==========================================================
class Usuario(Base):
    __tablename__ = "usuarios"

    id = Column(Integer, primary_key=True)
    nombreUsuario = Column(String(100))
    nombre = Column(String(100))
    apellido = Column(String(100))
    email = Column(String(100))
    rol = Column(String(50))


# ==========================================================
#  FILTROS GUARDADOS
# ==========================================================
class FiltroGuardado(Base):
    __tablename__ = "filtros_guardados"

    id = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(100), nullable=False)
    filtros = Column(JSON, nullable=False)
    id_usuario = Column(Integer, ForeignKey("usuarios.id"), nullable=False)

    usuario = relationship("Usuario")
