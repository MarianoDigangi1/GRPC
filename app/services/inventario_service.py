from datetime import datetime
from sqlalchemy.orm import Session
from app import models
from app.database import SessionLocal
import app.proto.inventario_pb2 as inventario_pb2
import app.proto.inventario_pb2_grpc as inventario_pb2_grpc

class InventarioService:
    def registrar_inventario(self, db: Session, request):
        inventario = models.Inventario(
            categoria=request.categoria,
            descripcion=request.descripcion,
            cantidad=request.cantidad,
            eliminado=False,
            created_at=datetime.now(),
            created_by=request.usuario_alta  # Aquí debes enviar el ID del usuario
        )
        db.add(inventario)
        db.commit()
        db.refresh(inventario)
        return inventario

    def modificar_inventario(self, db: Session, request):
        inventario = db.query(models.Inventario).filter_by(id=request.id, eliminado=False).first()
        if inventario:
            inventario.descripcion = request.descripcion
            inventario.cantidad = request.cantidad
            inventario.updated_at = datetime.now()
            inventario.updated_by = request.usuario_modificacion  # ID del usuario
            db.commit()
            db.refresh(inventario)
        return inventario

    def baja_inventario(self, db: Session, request):
        inventario = db.query(models.Inventario).filter_by(id=request.id, eliminado=False).first()
        if inventario:
            inventario.eliminado = True
            inventario.updated_at = datetime.now()
            inventario.updated_by = request.usuario_modificacion
            db.commit()
            db.refresh(inventario)
        return inventario

class InventarioGRPCService(inventario_pb2_grpc.InventarioServiceServicer):
    def RegistrarInventario(self, request, context):
        db = SessionLocal()
        service = InventarioService()
        inventario = service.registrar_inventario(db, request)
        return inventario_pb2.InventarioResponse(
            success=True if inventario else False,
            message="Inventario registrado correctamente" if inventario else "Error al registrar",
            id=inventario.id if inventario else 0
        )

    def ModificarInventario(self, request, context):
        db = SessionLocal()
        service = InventarioService()
        inventario = service.modificar_inventario(db, request)
        return inventario_pb2.InventarioResponse(
            success=True if inventario else False,
            message="Inventario modificado correctamente" if inventario else "No encontrado",
            id=inventario.id if inventario else 0
        )

    def BajaInventario(self, request, context):
        db = SessionLocal()
        service = InventarioService()
        inventario = service.baja_inventario(db, request)
        return inventario_pb2.InventarioResponse(
            success=True if inventario else False,
            message="Inventario dado de baja correctamente" if inventario else "No encontrado",
            id=inventario.id if inventario else 0
        )
