from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Text, DateTime, Boolean

Base = declarative_base()

class Evento(Base):
    __tablename__ = "evento"

    id = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(200), nullable=False)
    descripcion = Column(Text)
    fecha_evento = Column(DateTime, nullable=False)
    origen_organizacion_id = Column(String(100), nullable=False)
    evento_id_organizacion_externa = Column(String(100), nullable=True)
    vigente = Column(Boolean, default=True)
    publicado = Column(Boolean, default=False)
    evento_id_organizacion_externa = Column(String(100), nullable=True)

class Inventario(Base):
    __tablename__ = "inventario"

    id = Column(Integer, primary_key=True, index=True)
    categoria = Column(String(100), nullable=False)
    cantidad = Column(Integer, nullable=False)
    creado_en = Column(DateTime, nullable=False)
    eliminado = Column(Boolean, default=False)