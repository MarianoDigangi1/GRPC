from sqlalchemy.orm import Session
from app import crud, schemas, mapper, database
import app.proto.solicitudesExternas_pb2_grpc as solicitudesExternas_pb2_grpc
import app.proto.solicitudesExternas_pb2 as solicitudesExternas_pb2


class SolicitudesExternasService(solicitudesExternas_pb2_grpc.SolicitudesExternasServiceServicer): 
  def __init__(self):
    self.db_session = database.SessionLocal

  def listarSolicitudesVigentes(self, request, context):
    print("Entro al método listarSolicitudesVigentes del servicio gRPC")

    db: Session = self.db_session()
    try:
        solicitudes = crud.listar_solicitudes_externas(db)
        solicitudes_proto = [
            mapper.ProtoMapper.solicitud_to_solicitud_list_response_proto(item)
            for item in solicitudes
        ]
        return solicitudesExternas_pb2.ListarSolicitudesExternasResponse(solicitudes=solicitudes_proto)
    finally:
        db.close()