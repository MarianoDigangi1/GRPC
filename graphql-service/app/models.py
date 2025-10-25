from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Text, DateTime, Boolean

Base = declarative_base()

class Evento(Base):
    __tablename__ = "evento"
    id = Column(Integer, primary_key=True, autoincrement=True)
    nombre = Column(String(200), nullable=False)
    descripcion = Column(Text)
    fecha_evento = Column(DateTime, nullable=False)
    origen_organizacion_id = Column(String(100))
    vigente = Column(Boolean, default=True)
    evento_id_organizacion_externa = Column(String(100))