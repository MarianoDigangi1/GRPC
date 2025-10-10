import secrets
from passlib.hash import bcrypt
from app.models import Email
import smtplib
from email.mime.text import MIMEText

def generar_clave():
    return secrets.token_urlsafe(8) 

def encriptar_clave(plain_password: str):
    return bcrypt.hash(plain_password)

def verificar_clave(plain_password: str, hashed_password: str):
    return bcrypt.verify(plain_password, hashed_password)

def enviar_email(email: Email):
    cuerpo = f"""\
    Hola {email.usuario},

    Tu cuenta ha sido creada correctamente.

    Estos son tus datos de acceso:

    Usuario: {email.usuario}
    Contraseña: {email.password}

    No la compartas con nadie.

    Este mensaje fue generado automáticamente. Por favor, no respondas a este correo.
    """
    msg = MIMEText(cuerpo)
    msg['Subject'] = 'Envio de password'
    msg['From'] = 'no_responder@ong.com'
    msg['To'] = email.email

    try:
      with smtplib.SMTP('localhost', 1025) as server:
        print(f"📧 Enviando clave {email.password} a {email.email}")
        server.send_message(msg)
    except Exception as e:
      print(f"error al enviar el mail: {e}")
