import graphene
from sqlalchemy import func
from .models import Inventario
from .database import SessionLocal
from .schemas import DonacionInformeType
import graphene

class Query(graphene.ObjectType):
    informe_donaciones = graphene.List(
        DonacionInformeType,
        categoria=graphene.String(),
        fecha_inicio=graphene.String(),
        fecha_fin=graphene.String(),
        eliminado=graphene.String()  # "si", "no", "ambos"
    )

    def resolve_informe_donaciones(self, info, categoria=None, fecha_inicio=None, fecha_fin=None, eliminado=None):
        session = SessionLocal()
        query = session.query(
            Inventario.categoria,
            Inventario.eliminado,
            func.sum(Inventario.cantidad).label('total_cantidad')
        )

        # Filtros dinámicos
        if categoria:
            query = query.filter(Inventario.categoria == categoria)
        if fecha_inicio:
            query = query.filter(Inventario.created_at >= fecha_inicio)
        if fecha_fin:
            query = query.filter(Inventario.created_at <= fecha_fin)
        if eliminado == "si":
            query = query.filter(Inventario.eliminado == True)
        elif eliminado == "no":
            query = query.filter(Inventario.eliminado == False)
        # Si es "ambos" o None, no se filtra

        query = query.group_by(Inventario.categoria, Inventario.eliminado)
        resultados = query.all()

        return [
            DonacionInformeType(
                categoria=r.categoria,
                eliminado=r.eliminado,
                total_cantidad=r.total_cantidad
            ) for r in resultados
        ]