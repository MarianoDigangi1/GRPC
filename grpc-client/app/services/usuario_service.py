from sqlalchemy.orm import Session
import grpc

from app import schemas, database, mapper
from app.proto import usuarios_pb2, usuarios_pb2_grpc
from app.models import RolEnum  
from app.crud import usuario as crud_usuario  


class UsuarioService(usuarios_pb2_grpc.UsuarioServiceServicer):
    
    def __init__(self):
        self.db_session = database.SessionLocal

    # Crear usuario
    def CrearUsuario(self, request, context):
        print("Entró al método CrearUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            usuario_create = schemas.UsuarioCreate(
                nombreUsuario=request.nombreUsuario,
                nombre=request.nombre,
                apellido=request.apellido,
                telefono=request.telefono,
                email=request.email,
                rol=request.rol
            )
            usuarioResponse = crud_usuario.crear_usuario(db, usuario_create)  
            return mapper.ProtoMapper.usuario_to_create_user_request_proto(usuarioResponse)
        finally:
            db.close()

    # Modificar usuario
    def ModificarUsuario(self, request, context):
        print("Entró al método ModificarUsuario del servicio gRPC")
        db: Session = self.db_session()

        print(f"Rol recibido desde gRPC: {request.rol}")
        print(f"Roles válidos en el Enum: {[e.value for e in RolEnum]}")

        # Normalizar rol (por si llega en mayúsculas)
        rol_normalizado = request.rol.capitalize() if request.rol else None
        print(f"🧩 Rol normalizado a: {rol_normalizado}")

        try:
            usuario_update = schemas.UsuarioUpdate(
                nombreUsuario=request.nombreUsuario,
                nombre=request.nombre,
                apellido=request.apellido,
                telefono=request.telefono,
                email=request.email,
                rol=rol_normalizado,
                estaActivo=request.activo,
            )

            print(f"🟢 Datos recibidos por gRPC ModificarUsuario:\n"
                f"ID: {request.id}\n"
                f"nombreUsuario: {request.nombreUsuario}\n"
                f"nombre: {request.nombre}\n"
                f"apellido: {request.apellido}\n"
                f"telefono: {request.telefono}\n"
                f"email: {request.email}\n"
                f"rol: {rol_normalizado}\n"
                f"activo: {request.activo}")

            # 👉 Asegurate de que esta llamada esté presente:
            usuarioUpdate = crud_usuario.modificar_usuario(db, request.id, usuario_update)

            print(f"✅ Resultado del CRUD: {usuarioUpdate.mensaje if hasattr(usuarioUpdate, 'mensaje') else 'Sin mensaje'}")

            return mapper.ProtoMapper.usuario_to_usuario_delete_and_update_response_proto(usuarioUpdate)

        finally:
            db.close()


    # Baja usuario
    def BajaUsuario(self, request, context):
        print("Entró al método BajaUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            mensaje = crud_usuario.baja_usuario(db, request.id)  
            return usuarios_pb2.UpdateAndDeleteUserResponse(mensaje=mensaje)
        finally:
            db.close()

    # Login
    def Login(self, request, context):
        db: Session = self.db_session()
        try:
            resultado = crud_usuario.autenticar_usuario(db, request.identificador, request.clave) 
            
            if resultado.loginResult == schemas.LoginResultCode.LOGIN_OK:
                print(f"Login OK: {resultado.nombre} {resultado.apellido}")

            usuarioResponse = usuarios_pb2.Usuario(
                id=resultado.id,
                nombreUsuario=resultado.nombreUsuario,
                nombre=resultado.nombre,
                apellido=resultado.apellido,
                telefono=resultado.telefono,
                email=resultado.email,
                rol=resultado.rol if resultado.rol else ""
            )

            return usuarios_pb2.LoginResponse(
                result=resultado.loginResult,
                mensaje=resultado.mensaje,
                usuario=usuarioResponse
            )
        finally:
            db.close()

    # Listar usuarios
    def ListarUsuarios(self, request, context):
        db: Session = self.db_session()
        try:
            usuarios = crud_usuario.listar_usuarios(db) 
            usuarios_proto = [
                mapper.ProtoMapper.usuario_to_usuario_response_proto(usuario)
                for usuario in usuarios
            ]
            return usuarios_pb2.ListarUsuariosResponse(usuarios=usuarios_proto)
        finally:
            db.close()

    # Traer usuario por ID
    def TraerUsuarioPorId(self, request, context):
        db: Session = self.db_session()
        try:
            usuario = crud_usuario.obtener_usuario_por_id(db, request.id)  
            return usuarios_pb2.UserResponse(
                id=request.id,
                nombreUsuario=usuario.nombreUsuario,
                nombre=usuario.nombre,
                apellido=usuario.apellido,
                telefono=usuario.telefono if usuario.telefono else "",
                email=usuario.email,
                rol=usuario.rol if usuario.rol else "",
                activo=usuario.estaActivo,
            )
        finally:
            db.close()
            
