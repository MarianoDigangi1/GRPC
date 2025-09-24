from datetime import datetime
from sqlalchemy.orm import Session
import grpc

from app import mapper, database
from app.database import SessionLocal
from app.crud import inventario as crud_inventario   
import app.proto.inventario_pb2 as inventario_pb2
import app.proto.inventario_pb2_grpc as inventario_pb2_grpc


class InventarioGRPCService(inventario_pb2_grpc.InventarioServiceServicer):
    def __init__(self):
        self.db_session = database.SessionLocal

    # Registrar inventario
    def RegistrarInventario(self, request, context):
        db: Session = SessionLocal()
        try:
            inventario_data = {
                "categoria": request.categoria,
                "descripcion": request.descripcion,
                "cantidad": request.cantidad,
                "usuario_alta": request.usuario_alta
            }
            db_inventario = crud_inventario.create_inventario(db, inventario_pb2.InventarioCreate(**inventario_data))
            return inventario_pb2.InventarioResponse(
                success=True if db_inventario else False,
                message="Inventario registrado correctamente" if db_inventario else "Error al registrar",
                id=db_inventario.id if db_inventario else 0
            )
        finally:
            db.close()

    # Modificar inventario
    def ModificarInventario(self, request, context):
        db: Session = SessionLocal()
        try:
            update_data = {
                "descripcion": request.descripcion,
                "cantidad": request.cantidad,
                "usuario_modificacion": request.usuario_modificacion
            }
            db_inventario = crud_inventario.update_inventario(db, request.id, inventario_pb2.InventarioUpdate(**update_data))
            return inventario_pb2.InventarioResponse(
                success=True if db_inventario else False,
                message="Inventario modificado correctamente" if db_inventario else "No encontrado",
                id=db_inventario.id if db_inventario else 0
            )
        finally:
            db.close()

    # Baja inventario
    def BajaInventario(self, request, context):
        db: Session = SessionLocal()
        try:
            db_inventario = crud_inventario.baja_inventario(db, request.id, request.usuario_modificacion)
            return inventario_pb2.InventarioResponse(
                success=True if db_inventario else False,
                message="Inventario dado de baja correctamente" if db_inventario else "No encontrado",
                id=db_inventario.id if db_inventario else 0
            )
        finally:
            db.close()

    # Listar inventario
    def ListarInventario(self, request, context):
        print("Entró al método ListarInventario del servicio gRPC")
        db: Session = self.db_session()
        try:
            inventario = crud_inventario.listar_inventario(db)  # 👈 nuevo crud
            inventarios_proto = [
                mapper.ProtoMapper.inventario_to_inventario_list_response_proto(item)
                for item in inventario
            ]
            return inventario_pb2.ListarInventarioResponse(inventarios=inventarios_proto)
        finally:
            db.close()
    
    # Obtener inventario por ID
    def ObtenerInventarioPorId(self, request, context):
        print("Entró al método ObtenerInventarioPorId del servicio gRPC")
        db: Session = self.db_session()
        try:
            resp, err = crud_inventario.obtener_inventario_por_id(db, request.id)  # 👈 nuevo crud
            if err:
                context.set_code(grpc.StatusCode.NOT_FOUND)
                context.set_details(err)
                return inventario_pb2.ObtenerInventarioPorIdResponse()
            return mapper.ProtoMapper.inventario_to_obtener_inventario_por_id_response_proto(resp, "")
        finally:
            db.close()
