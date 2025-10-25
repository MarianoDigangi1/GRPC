import graphene
from graphene_sqlalchemy import SQLAlchemyObjectType
from .models import Evento

class EventoType(SQLAlchemyObjectType):
    class Meta:
        model = Evento

class DonacionInformeType(graphene.ObjectType):
    categoria = graphene.String()
    eliminado = graphene.Boolean()
    total_cantidad = graphene.Int()

    