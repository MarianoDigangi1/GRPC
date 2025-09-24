from sqlalchemy.orm import Session
from sqlalchemy import and_
from datetime import datetime
from app import models, schemas
from app.mapper import SchemaMapper


# ---------- Helpers ----------
def _es_futuro(dt: datetime) -> bool:
    return dt > datetime.now()

def _miembro_activo(db: Session, usuario_id: int) -> bool:
    u = db.query(models.Usuario).filter(models.Usuario.id == usuario_id).first()
    return bool(u and u.estaActivo)


# ---------- Crear ----------
def crear_evento(db: Session, data: schemas.EventoCreate):
    fecha = datetime.fromisoformat(data.fecha_evento_iso)
    if not _es_futuro(fecha):
        return None, "La fecha del evento debe ser a futuro."

    evento = models.Evento(
        nombre=data.nombre,
        descripcion=data.descripcion,
        fecha_evento=fecha
    )
    db.add(evento)
    db.flush()

    for uid in set(data.miembros_ids or []):
        if _miembro_activo(db, uid):
            db.add(models.EventoUsuario(evento_id=evento.id, usuario_id=uid))

    db.commit()
    db.refresh(evento)

    miembros_ids = [eu.usuario_id for eu in evento.participantes]
    return SchemaMapper.evento_model_to_response(evento, miembros_ids, "Evento creado correctamente"), None


# ---------- Modificar ----------
def modificar_evento(db: Session, evento_id: int, data: schemas.EventoUpdate, actor_usuario_id: int, actor_rol: str):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return None, "Evento no encontrado."

    # Actualizar nombre
    if data.nombre is not None:
        ev.nombre = data.nombre

    # Actualizar fecha
    if data.fecha_evento_iso is not None:
        nueva_fecha = datetime.fromisoformat(data.fecha_evento_iso)
        if not _es_futuro(nueva_fecha):
            return None, "La nueva fecha/hora debe ser a futuro."
        ev.fecha_evento = nueva_fecha

    # Reglas de autorización
    def _puede_gestionar(usuario_id: int) -> bool:
        if actor_rol in ("Presidente", "Coordinador"):
            return True
        return actor_rol == "Voluntario" and usuario_id == actor_usuario_id

    # Agregar miembros
    for uid in set(data.agregar_miembros_ids or []):
        if not _puede_gestionar(uid):
            return None, "No autorizado para asignar este miembro."
        if _miembro_activo(db, uid):
            ya = db.query(models.EventoUsuario).filter(
                and_(models.EventoUsuario.evento_id == ev.id, models.EventoUsuario.usuario_id == uid)
            ).first()
            if not ya:
                db.add(models.EventoUsuario(evento_id=ev.id, usuario_id=uid))

    # Quitar miembros
    for uid in set(data.quitar_miembros_ids or []):
        if not _puede_gestionar(uid):
            return None, "No autorizado para quitar este miembro."
        db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id == ev.id, models.EventoUsuario.usuario_id == uid)
        ).delete()

    # Registrar donaciones (solo eventos pasados)
    if data.donaciones_usadas:
        if _es_futuro(ev.fecha_evento):
            return None, "Solo se pueden registrar donaciones en eventos pasados."
        for d in data.donaciones_usadas:
            if d.cantidad_usada <= 0:
                return None, "Las cantidades usadas deben ser positivas."

            inv = db.query(models.Inventario).filter(
                models.Inventario.id == d.inventario_id,
                models.Inventario.eliminado == False
            ).with_for_update().first()

            if not inv or inv.cantidad < d.cantidad_usada:
                return None, f"Inventario {d.inventario_id} insuficiente o inexistente."

            inv.cantidad -= d.cantidad_usada
            inv.updated_at = datetime.now()
            inv.updated_by = actor_usuario_id

            ya = db.query(models.EventoInventario).filter(
                and_(models.EventoInventario.evento_id == ev.id, models.EventoInventario.inventario_id == d.inventario_id)
            ).first()

            if ya:
                ya.cantidad_usada += d.cantidad_usada
            else:
                db.add(models.EventoInventario(
                    evento_id=ev.id,
                    inventario_id=d.inventario_id,
                    cantidad_usada=d.cantidad_usada
                ))

    db.commit()
    db.refresh(ev)

    miembros_ids = [eu.usuario_id for eu in ev.participantes]
    return SchemaMapper.evento_model_to_response(ev, miembros_ids, "Evento modificado correctamente"), None


# ---------- Baja (física) ----------
def baja_evento(db: Session, evento_id: int):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return "Evento no encontrado."
    if not _es_futuro(ev.fecha_evento):
        return "Solo pueden eliminarse eventos a futuro."

    db.delete(ev)
    db.commit()
    return "Evento eliminado correctamente."


# ---------- Asignar / Quitar miembro ----------
def asignar_quitar_miembro(db: Session, evento_id: int, usuario_id: int, actor_usuario_id: int, actor_rol: str, agregar: bool):
    ev = db.query(models.Evento).filter(models.Evento.id == evento_id).first()
    if not ev:
        return "Evento no encontrado."

    def _autz():
        if actor_rol in ("Presidente", "Coordinador"):
            return True
        return actor_rol == "Voluntario" and usuario_id == actor_usuario_id

    if not _autz():
        return "No autorizado."

    if agregar:
        if not _miembro_activo(db, usuario_id):
            return "Solo se pueden asignar miembros activos."
        ya = db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id == evento_id, models.EventoUsuario.usuario_id == usuario_id)
        ).first()
        if ya:
            return "El miembro ya participa del evento."
        db.add(models.EventoUsuario(evento_id=evento_id, usuario_id=usuario_id))
    else:
        db.query(models.EventoUsuario).filter(
            and_(models.EventoUsuario.evento_id == evento_id, models.EventoUsuario.usuario_id == usuario_id)
        ).delete()

    db.commit()
    return "Operación realizada correctamente."


# ---------- Quitar usuario de eventos futuros ----------
def quitar_usuario_de_eventos_futuros(db: Session, usuario_id: int):
    ahora = datetime.now()
    futuros = db.query(models.EventoUsuario).join(
        models.Evento, models.EventoUsuario.evento_id == models.Evento.id
    ).filter(
        models.EventoUsuario.usuario_id == usuario_id,
        models.Evento.fecha_evento > ahora
    ).all()

    for p in futuros:
        db.delete(p)

    db.commit()


# ---------- Listar eventos disponibles ----------
def listar_eventos_disponibles(db: Session):
    eventos = db.query(models.Evento).all()
    result = []
    for ev in eventos:
        miembros_ids = [eu.usuario_id for eu in ev.participantes]
        result.append(SchemaMapper.evento_model_to_response(ev, miembros_ids))
    return result
