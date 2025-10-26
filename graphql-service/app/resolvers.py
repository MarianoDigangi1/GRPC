import graphene
import json
from sqlalchemy import func
from .models import TransferenciaDonacionExterna, FiltroGuardado, Usuario, Evento, EventoUsuario
from .database import SessionLocal
from .schemas import DonacionInformeType, EventoPropioInformeType, FiltroGuardadoType, FiltroGuardadoInput, EventoPropioInformeType

class Query(graphene.ObjectType):
    informe_donaciones = graphene.List(
        DonacionInformeType,
        categoria=graphene.String(),
        fechaInicio=graphene.String(),
        fechaFin=graphene.String(),
        eliminado=graphene.String()  # "si", "no", "ambos"
    )
    
    informe_participacion_eventos = graphene.List(
        EventoPropioInformeType,
        usuario_id=graphene.Int(required=True),
        fechaInicio=graphene.String(),
        fechaFin=graphene.String(),
        #reparto_donaciones=graphene.String()  # "si", "no", "ambos"
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
                query = query.filter(TransferenciaDonacionExterna.vigente == True)
            if eliminado == "no":
                query = query.filter(TransferenciaDonacionExterna.vigente == False)

            registros = query.all()
            for r in registros:
                try:
                    if isinstance(r.contenido, str):
                        items = json.loads(r.contenido)
                    else:
                        items = r.contenido
                    for item in items:
                        cat = item.get("categoria")
                        cant = item.get("cantidad") or 0
                        if not cat:
                            continue
                        if categoria and cat != categoria:
                            continue
                        key = (cat, r.vigente)
                        if key not in resultados:
                            resultados[key] = 0
                        resultados[key] += cant
                except Exception:
                    continue 


            print("Resultado informe_donaciones:", [
                DonacionInformeType(
                    categoria=cat,
                    eliminado=eliminado_val,
                    total_cantidad=total
                )           
                for (cat, eliminado_val), total in resultados.items()
            ])

            return [
                DonacionInformeType(
                    categoria=cat,
                    eliminado=eliminado_val,
                    total_cantidad=total
                )
                #for cat, info in resultados.items()
                for (cat, eliminado_val), total in resultados.items()
            ]
        finally:
            session.close()

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

    def resolve_informe_eventos_propios(self, info, usuario_id, fechaInicio=None, fechaFin=None, reparto_donaciones=None):
        session = SessionLocal()
        try:
            current_user_id = info.context.get("user_id")
            usuario = session.query(Usuario).filter_by(id=current_user_id).first()
            # Validar acceso
            if usuario.rol not in ["Presidente", "Coordinador"] and usuario_id != current_user_id:
                raise Exception("No tienes permiso para ver otros usuarios.")
            # Query base
            query = session.query(Evento).join(EventoUsuario).filter(
                EventoUsuario.usuario_id == usuario_id,
                Evento.vigente == True
            )
            # Filtros
            if fechaInicio:
                query = query.filter(Evento.fecha_evento >= fechaInicio)
            if fechaFin:
                query = query.filter(Evento.fecha_evento <= fechaFin)
            # Si tienes campo reparto_donaciones en Evento, agrega filtro aquí
            if reparto_donaciones == "si":
                query = query.filter(Evento.reparto_donaciones == True)
            elif reparto_donaciones == "no":
                query = query.filter(Evento.reparto_donaciones == False)
            # Agrupar y formatear
            eventos = query.all()
            resultado = []
            for evento in eventos:
                mes = evento.fecha_evento.strftime("%B")
                dia = evento.fecha_evento.strftime("%d")
                resultado.append(EventoPropioInformeType(
                    mes=mes,
                    dia=dia,
                    nombre_evento=evento.nombre,
                    descripcion=evento.descripcion
                ))
            return resultado
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

class Mutation(graphene.ObjectType):
    crear_filtro = CrearFiltro.Field()
    actualizar_filtro = ActualizarFiltro.Field()
    eliminar_filtro = EliminarFiltro.Field()
