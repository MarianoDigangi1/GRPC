from sqlalchemy.orm import Session
from datetime import datetime
from app import models, schemas

# ---------- Listar ofertas externas ----------
def listar_ofertas_externas(db: Session):
    return db.query(models.OfertaExterna).all()