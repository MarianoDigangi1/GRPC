from sqlalchemy import func, and_
from .models import Donacion
from .database import SessionLocal
from .schemas import DonacionInformeType
import graphene

class Query(graphene.ObjectType):
    informe_donaciones = graphene.List(
        DonacionInformeType,
        categoria=graphene.String(),
        fecha_inicio=graphene.DateTime(),
        fecha_fin=graphene.DateTime(),
        eliminado=graphene.String()  # "si", "no", "ambos"
    )

    def resolve_informe_donaciones(self, info, categoria=None, fecha_inicio=None, fecha_fin=None, eliminado=None):
        session = SessionLocal()
        query = session.query(
            Donacion.categoria,
            Donacion.eliminado,
            func.sum(Donacion.cantidad).label("total_cantidad")
        )

        filters = []
        if categoria:
            filters.append(Donacion.categoria == categoria)
        if fecha_inicio:
            filters.append(Donacion.fecha_alta >= fecha_inicio)
        if fecha_fin:
            filters.append(Donacion.fecha_alta <= fecha_fin)
        if eliminado == "si":
            filters.append(Donacion.eliminado == True)
        elif eliminado == "no":
            filters.append(Donacion.eliminado == False)
        # Si "ambos", no se filtra por eliminado

        if filters:
            query = query.filter(and_(*filters))

        query = query.group_by(Donacion.categoria, Donacion.eliminado)
        results = query.all()

        return [
            DonacionInformeType(
                categoria=row.categoria,
                eliminado=row.eliminado,
                total_cantidad=row.total_cantidad
            )
            for row in results
        ]