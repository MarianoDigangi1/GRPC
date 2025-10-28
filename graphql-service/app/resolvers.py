import graphene
import json
from sqlalchemy import func
from .models import TransferenciaDonacionExterna, FiltroGuardado, Usuario, Evento, EventoUsuario
from .database import SessionLocal
from .schemas import DonacionInformeType, EventoPropioInformeType, FiltroGuardadoType, FiltroGuardadoInput

class EventoListadoType(graphene.ObjectType):
    dia = graphene.String()
    nombre = graphene.String()
    descripcion = graphene.String()

class EventoMesGroupType(graphene.ObjectType):
    mes = graphene.String()
    eventos = graphene.List(EventoListadoType)

class Query(graphene.ObjectType):
    informe_donaciones = graphene.List(
        DonacionInformeType,
        categoria=graphene.String(),
        fechaInicio=graphene.String(),
        fechaFin=graphene.String(),
        eliminado=graphene.String()
    )
    informe_participacion = graphene.List(
        EventoMesGroupType,
        usuario_id=graphene.Int(required=True),
        fechaInicio=graphene.String(),
        fechaFin=graphene.String(),
        reparto_donaciones=graphene.String()
    )
    mis_filtros = graphene.List(FiltroGuardadoType)

    def resolve_informe_donaciones(self, info, categoria=None, fechaInicio=None, fechaFin=None, eliminado=None):
        session = SessionLocal()
        resultados = {}
        try:
            query = session.query(TransferenciaDonacionExterna)
            if fechaInicio:
                query = query.filter(TransferenciaDonacionExterna.fecha_transferencia >= fechaInicio)
            if fechaFin:
                query = query.filter(TransferenciaDonacionExterna.fecha_transferencia <= fechaFin)
            if eliminado == "si":
                query = query.filter(TransferenciaDonacionExterna.vigente == False)
            elif eliminado == "no":
                query = query.filter(TransferenciaDonacionExterna.vigente == True)

            registros = query.all()
            for r in registros:
                items = json.loads(r.contenido) if isinstance(r.contenido, str) else r.contenido
                if not items:
                    continue
                for item in items:
                    cat = item.get("categoria")
                    cant = item.get("cantidad") or 0
                    if not cat:
                        continue
                    if categoria and cat != categoria:
                        continue
                    elim_bool = not bool(r.vigente)
                    key = (cat, elim_bool)
                    resultados[key] = resultados.get(key, 0) + cant

            return [
                DonacionInformeType(
                    categoria=cat,
                    eliminado=elim,
                    total_cantidad=total
                )
                for (cat, elim), total in resultados.items()
            ]
        finally:
            session.close()

    def resolve_mis_filtros(self, info):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            if not current_user_id:
                raise Exception("Usuario no autenticado.")
            return session.query(FiltroGuardado).filter(FiltroGuardado.id_usuario == current_user_id).all()
        finally:
            session.close()

    def resolve_informe_participacion(self, info, usuario_id, fechaInicio=None, fechaFin=None, reparto_donaciones=None):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            usuario = session.query(Usuario).filter_by(id=current_user_id).first()
            if usuario.rol not in ["Presidente", "Coordinador"] and usuario_id != current_user_id:
                raise Exception("No tienes permiso para ver otros usuarios.")

            q = session.query(Evento).join(EventoUsuario).filter(
                EventoUsuario.usuario_id == usuario_id,
                Evento.vigente == True
            )
            if fechaInicio:
                q = q.filter(Evento.fecha_evento >= fechaInicio)
            if fechaFin:
                q = q.filter(Evento.fecha_evento <= fechaFin)
            if hasattr(Evento, "reparto_donaciones"):
                if reparto_donaciones == "si":
                    q = q.filter(Evento.reparto_donaciones == True)
                elif reparto_donaciones == "no":
                    q = q.filter(Evento.reparto_donaciones == False)

            eventos = q.all()
            grupos = {}
            for e in eventos:
                mes = e.fecha_evento.strftime("%B")
                dia = e.fecha_evento.strftime("%d")
                grupos.setdefault(mes, []).append(
                    {"dia": dia, "nombre": e.nombre, "descripcion": e.descripcion}
                )
            return [
                EventoMesGroupType(
                    mes=mes,
                    eventos=[EventoListadoType(**it) for it in lista]
                )
                for mes, lista in grupos.items()
            ]
        finally:
            session.close()

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
            nuevo = FiltroGuardado(
                nombre=filtro_data.nombre,
                filtros=filtro_data.filtros,
                id_usuario=current_user_id
            )
            session.add(nuevo)
            session.commit()
            session.refresh(nuevo)
            return CrearFiltro(filtro=nuevo)
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()

class ActualizarFiltro(graphene.Mutation):
    class Arguments:
        id = graphene.ID(required=True)
        filtro_data = FiltroGuardadoInput(required=True)
    ok = graphene.Boolean()
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
            return ActualizarFiltro(ok=True)
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()

class EliminarFiltro(graphene.Mutation):
    class Arguments:
        id = graphene.ID(required=True)
    ok = graphene.Boolean()
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
            return EliminarFiltro(ok=True)
        except Exception as e:
            session.rollback()
            raise e
        finally:
            session.close()

class Mutation(graphene.ObjectType):
    crear_filtro = CrearFiltro.Field()
    actualizar_filtro = ActualizarFiltro.Field()
    eliminar_filtro = EliminarFiltro.Field()
