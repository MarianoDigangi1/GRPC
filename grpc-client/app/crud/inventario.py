from sqlalchemy.orm import Session
from datetime import datetime
from app import models, schemas


# ---------- Crear inventario ----------
def create_inventario(db: Session, inventario: schemas.InventarioCreate):
    db_inventario = models.Inventario(
        categoria=inventario.categoria,
        descripcion=inventario.descripcion,
        cantidad=inventario.cantidad,
        eliminado=False,
        created_at=datetime.now(),        # 👈 coincide con models.py
        created_by=inventario.usuario_alta
    )
    db.add(db_inventario)
    db.commit()
    db.refresh(db_inventario)
    return db_inventario


# ---------- Modificar inventario ----------
def update_inventario(db: Session, inventario_id: int, inventario: schemas.InventarioUpdate):
    db_inventario = db.query(models.Inventario).filter_by(id=inventario_id, eliminado=False).first()
    if db_inventario:
        db_inventario.descripcion = inventario.descripcion
        db_inventario.cantidad = inventario.cantidad
        db_inventario.updated_at = datetime.now()    # 👈 coincide con models.py
        db_inventario.updated_by = inventario.usuario_modificacion
        db.commit()
        db.refresh(db_inventario)
    return db_inventario


# ---------- Baja lógica ----------
def baja_inventario(db: Session, inventario_id: int, usuario_modificacion: str):
    db_inventario = db.query(models.Inventario).filter_by(id=inventario_id, eliminado=False).first()
    if db_inventario:
        db_inventario.eliminado = True
        db_inventario.updated_at = datetime.now()    # 👈 coincide con models.py
        db_inventario.updated_by = usuario_modificacion
        db.commit()
        db.refresh(db_inventario)
    return db_inventario


# ---------- Listar inventario ----------
def listar_inventario(db: Session):
    return db.query(models.Inventario).filter(models.Inventario.eliminado == False).all()


# ---------- Obtener inventario por ID ----------
def obtener_inventario_por_id(db: Session, inventario_id: int):
    inventario = db.query(models.Inventario).filter_by(id=inventario_id).first()
    if not inventario:
        return None, "Inventario no encontrado."
    return inventario, None
