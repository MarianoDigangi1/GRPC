from sqlalchemy.orm import Session
from app import crud, schemas, database, mapper
from app.proto import eventos_pb2, eventos_pb2_grpc
import grpc

class EventoService(eventos_pb2_grpc.EventoServiceServicer):
    def __init__(self):
        self.db_session = database.SessionLocal

    def CrearEvento(self, request, context):
        db: Session = self.db_session()
        try:
            data = schemas.EventoCreate(
                nombre=request.nombre,
                descripcion=request.descripcion,
                fecha_evento_iso=request.fecha_evento_iso,
                miembros_ids=list(request.miembros_ids)
            )
            resp, err = crud.crear_evento(db, data)
            if err:
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details(err)
                return eventos_pb2.CrearEventoResponse()
            return mapper.ProtoMapper.evento_create_response_proto(resp, resp.mensaje or "Evento creado")
        finally:
            db.close()

    def ModificarEvento(self, request, context):
        db: Session = self.db_session()
        try:
            data = schemas.EventoUpdate(
                nombre=request.nombre if request.nombre else None,
                fecha_evento_iso=request.fecha_evento_iso if request.fecha_evento_iso else None,
                agregar_miembros_ids=list(request.agregar_miembros_ids),
                quitar_miembros_ids=list(request.quitar_miembros_ids),
                donaciones_usadas=[schemas.DonacionUsada(inventario_id=d.inventario_id, cantidad_usada=d.cantidad_usada)
                                   for d in request.donaciones_usadas]
            )
            resp, err = crud.modificar_evento(db, request.id, data, request.actor_usuario_id, request.actor_rol)
            if err:
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details(err)
                return eventos_pb2.ModificarEventoResponse()
            return mapper.ProtoMapper.evento_update_response_proto(resp, resp.mensaje or "Evento modificado")
        finally:
            db.close()

    def BajaEvento(self, request, context):
        db: Session = self.db_session()
        try:
            mensaje = crud.baja_evento(db, request.id)
            if "Solo pueden eliminarse" in mensaje or "no encontrado" in mensaje:
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details(mensaje)
                return eventos_pb2.BajaEventoResponse()
            return eventos_pb2.BajaEventoResponse(mensaje=mensaje)
        finally:
            db.close()

    def AsignarOQuitarMiembro(self, request, context):
        db: Session = self.db_session()
        try:
            mensaje = crud.asignar_quitar_miembro(
                db,
                evento_id=request.evento_id,
                usuario_id=request.usuario_id,
                actor_usuario_id=request.actor_usuario_id,
                actor_rol=request.actor_rol,
                agregar=request.agregar
            )
            if mensaje != "Operación realizada correctamente.":
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details(mensaje)
                return eventos_pb2.AsignarQuitarResponse()
            return eventos_pb2.AsignarQuitarResponse(mensaje=mensaje)
        finally:
            db.close()
