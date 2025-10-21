from sqlalchemy.orm import Session
from datetime import datetime
from app import models, schemas

# ---------- Listar solicitudes externas ----------
def listar_solicitudes_externas(db: Session):
    return db.query(models.SolicitudExterna).filter(models.SolicitudExterna.vigente == True).all()