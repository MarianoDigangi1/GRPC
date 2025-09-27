from sqlalchemy.orm import Session
from app import crud, schemas, mapper, database
import app.proto.inventario_pb2 as inventario_pb2
import app.proto.inventario_pb2_grpc as inventario_pb2_grpc
import grpc


class InventarioService(inventario_pb2_grpc.InventarioServiceServicer):
    def __init__(self):
        self.db_session = database.SessionLocal

    # ---------- Registrar ----------
    def RegistrarInventario(self, request, context):
        db: Session = self.db_session()
        try:
            data = schemas.InventarioCreate(
                categoria=request.categoria,
                descripcion=request.descripcion,
                cantidad=request.cantidad,
                usuario_alta=request.usuario_alta,
            )
            inventario = crud.create_inventario(db, data)
            return inventario_pb2.InventarioResponse(
                success=True if inventario else False,
                message="Inventario registrado correctamente" if inventario else "Error al registrar",
                id=inventario.id if inventario else 0,
            )
        finally:
            db.close()

    # ---------- Modificar ----------
    def ModificarInventario(self, request, context):
        db: Session = self.db_session()
        try:
            data = schemas.InventarioUpdate(
                descripcion=request.descripcion,
                cantidad=request.cantidad,
                usuario_modificacion=request.usuario_modificacion,
            )
            inventario = crud.update_inventario(db, request.id, data)
            return inventario_pb2.InventarioResponse(
                success=True if inventario else False,
                message="Inventario modificado correctamente" if inventario else "No encontrado",
                id=inventario.id if inventario else 0,
            )
        finally:
            db.close()

    # ---------- Baja lógica ----------
    def BajaInventario(self, request, context):
        db: Session = self.db_session()
        try:
            inventario = crud.baja_inventario(db, request.id, request.usuario_modificacion)
            return inventario_pb2.InventarioResponse(
                success=True if inventario else False,
                message="Inventario dado de baja correctamente" if inventario else "No encontrado",
                id=inventario.id if inventario else 0,
            )
        finally:
            db.close()

    # ---------- Listar ----------
    def ListarInventario(self, request, context):
        print("Entró al método ListarInventario del servicio gRPC")
        db: Session = self.db_session()
        try:
            inventarios = crud.listar_inventario(db)
            inventarios_proto = [
                mapper.ProtoMapper.inventario_to_inventario_list_response_proto(item)
                for item in inventarios
            ]
            return inventario_pb2.ListarInventarioResponse(inventarios=inventarios_proto)
        finally:
            db.close()

    # ---------- Obtener por ID ----------
    def ObtenerInventarioPorId(self, request, context):
        print("Entró al método ObtenerInventarioPorId del servicio gRPC")
        db: Session = self.db_session()
        try:
            resp, err = crud.obtener_inventario_por_id(db, request.id)
            if err:
                context.set_code(grpc.StatusCode.NOT_FOUND)
                context.set_details(err)
                return inventario_pb2.ObtenerInventarioPorIdResponse()

            return mapper.ProtoMapper.inventario_to_obtener_inventario_por_id_response_proto(resp, "")
        finally:
            db.close()
