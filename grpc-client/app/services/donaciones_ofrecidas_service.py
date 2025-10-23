from sqlalchemy.orm import Session
from app import crud, schemas, mapper, database
import app.proto.donacionesOfrecidas_pb2_grpc as donacionesOfrecidas_pb2_grpc
import app.proto.donacionesOfrecidas_pb2 as donacionesOfrecidas_pb2


class DonacionesOfrecidasService(donacionesOfrecidas_pb2_grpc.DonacionesOfrecidasServiceServicer): 
  def __init__(self):
    self.db_session = database.SessionLocal

  def listarSolicitudesVigentes(self, request, context):
    print("Entro al metodo donacionesOfrecidas del servicio GRPC")

    db: Session = self.db_session()
    try:
        ofertas = crud.listar_ofertas_externas(db)
        ofertas_proto = [
            mapper.ProtoMapper.oferta_to_oferta_list_response_proto(item)
            for item in ofertas
        ]
        return donacionesOfrecidas_pb2.ListarDonacionesOfrecidasResponse(solicitudes=ofertas_proto)
    finally:
        db.close()