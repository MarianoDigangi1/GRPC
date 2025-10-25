import graphene
from graphene_sqlalchemy import SQLAlchemyObjectType
from .models import Evento, FiltroGuardado

class EventoType(SQLAlchemyObjectType):
    class Meta:
        model = Evento

class FiltroGuardadoType(SQLAlchemyObjectType):
    class Meta:
        model = FiltroGuardado    

class DonacionInformeType(graphene.ObjectType):
    categoria = graphene.String()
    eliminado = graphene.Boolean()
    total_cantidad = graphene.Int()

class FiltroGuardadoInput(graphene.InputObjectType):
    nombre = graphene.String(required=True)
    filtros = graphene.JSONString(required=True)  
    id_usuario = graphene.Int(required=True)