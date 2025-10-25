import graphene
from sqlalchemy import func
from .models import Inventario, FiltroGuardado
from .database import SessionLocal
from .schemas import DonacionInformeType, FiltroGuardadoType, FiltroGuardadoInput
import graphene
HARDCODED_USER_ID = 1
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
    mis_filtros = graphene.List(FiltroGuardadoType)

def resolve_mis_filtros(self, info):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            if not current_user_id:
                raise Exception("Usuario no autenticado.")

            filtros = session.query(FiltroGuardado).filter(
                FiltroGuardado.id_usuario == current_user_id
            ).all()
            return filtros
        finally:
            session.close()


# ==========================================================
#  MUTATIONS
# ==========================================================

# ---------- Crear Filtro ----------
class CrearFiltro(graphene.Mutation):
    class Arguments:
        filtro_data = FiltroGuardadoInput(required=True)

    filtro = graphene.Field(FiltroGuardadoType)

    def mutate(self, info, filtro_data):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            if not current_user_id:
                raise Exception("Usuario no autenticado.")

            nuevo_filtro = FiltroGuardado(
                nombre=filtro_data.nombre,
                filtros=filtro_data.filtros,
                id_usuario=current_user_id
            )
            session.add(nuevo_filtro)
            session.commit()
            session.refresh(nuevo_filtro)
            return CrearFiltro(filtro=nuevo_filtro)
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()


# ---------- Actualizar Filtro ----------
class ActualizarFiltro(graphene.Mutation):
    class Arguments:
        id = graphene.ID(required=True)
        filtro_data = FiltroGuardadoInput(required=True)

    filtro = graphene.Field(FiltroGuardadoType)

    def mutate(self, info, id, filtro_data):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            if not current_user_id:
                raise Exception("Usuario no autenticado.")

            filtro = session.query(FiltroGuardado).filter(
                FiltroGuardado.id == id,
                FiltroGuardado.id_usuario == current_user_id
            ).first()

            if not filtro:
                raise Exception("Filtro no encontrado o no te pertenece.")

            filtro.nombre = filtro_data.nombre
            filtro.filtros = filtro_data.filtros

            session.commit()
            session.refresh(filtro)
            return ActualizarFiltro(filtro=filtro)
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()


# ---------- Eliminar Filtro ----------
class EliminarFiltro(graphene.Mutation):
    class Arguments:
        id = graphene.ID(required=True)

    id_eliminado = graphene.ID()
    mensaje = graphene.String()

    def mutate(self, info, id):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            if not current_user_id:
                raise Exception("Usuario no autenticado.")

            filtro = session.query(FiltroGuardado).filter(
                FiltroGuardado.id == id,
                FiltroGuardado.id_usuario == current_user_id
            ).first()

            if not filtro:
                raise Exception("Filtro no encontrado o no te pertenece.")

            session.delete(filtro)
            session.commit()
            return EliminarFiltro(id_eliminado=id, mensaje="Filtro eliminado exitosamente.")
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()


# ==========================================================
# REGISTRO DE MUTATIONS
# ==========================================================
class Mutation(graphene.ObjectType):
    crear_filtro = CrearFiltro.Field()
    actualizar_filtro = ActualizarFiltro.Field()
    eliminar_filtro = EliminarFiltro.Field()