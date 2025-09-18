import secrets
from passlib.hash import bcrypt

def generar_clave():
    return secrets.token_urlsafe(8) 

def encriptar_clave(plain_password: str):
    return bcrypt.hash(plain_password)

def verificar_clave(plain_password: str, hashed_password: str):
    return bcrypt.verify(plain_password, hashed_password)

def enviar_email(destinatario: str, clave: str):
    print(f"📧 Enviando clave {clave} a {destinatario}")