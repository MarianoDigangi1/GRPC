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
            usuarioResponse = crud_usuario.crear_usuario(db, usuario_create)  # 👈 nuevo import
            usuarioResponseProto = mapper.ProtoMapper.usuario_to_create_user_request_proto(usuarioResponse)
            return usuarioResponseProto
        finally:
            db.close()

    # Modificar usuario
    def ModificarUsuario(self, request, context):
        print("Entró al método ModificarUsuario del servicio gRPC")
        db: Session = self.db_session()
        
        if request.rol not in RolEnum.__members__ and request.rol not in [e.value for e in RolEnum]:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details(f"Rol '{request.rol}' no es válido. Debe ser uno de: {[e.value for e in RolEnum]}")
            return usuarios_pb2.UpdateAndDeleteUserResponse()  
        
        try:
            usuario_update = schemas.UsuarioUpdate(
                nombreUsuario=request.nombreUsuario, 
                nombre=request.nombre,
                apellido=request.apellido,
                telefono=request.telefono,
                email=request.email,
                rol=request.rol,
                estaActivo=request.activo,
            )
            usuarioUpdate = crud_usuario.modificar_usuario(db, request.id, usuario_update)  # 👈 nuevo import

            usuarioToProto = usuarios_pb2.CreateUserRequest(
                nombreUsuario=usuarioUpdate.nombreUsuario,
                nombre=usuarioUpdate.nombre,
                apellido=usuarioUpdate.apellido,
                telefono=usuarioUpdate.telefono if usuarioUpdate.telefono else "",
                email=usuarioUpdate.email,
                rol=usuarioUpdate.rol if usuarioUpdate.rol else ""
            )

            return usuarios_pb2.UpdateAndDeleteUserResponse(
                usuario=usuarioToProto,
                mensaje=usuarioUpdate.mensaje
            )
        finally:
            db.close()

    # Baja usuario
    def BajaUsuario(self, request, context):
        print("Entró al método BajaUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            mensaje = crud_usuario.baja_usuario(db, request.id)  # 👈 nuevo import
            return usuarios_pb2.UpdateAndDeleteUserResponse(mensaje=mensaje)
        finally:
            db.close()

    # Login
    def Login(self, request, context):
        db: Session = self.db_session()
        try:
            resultado = crud_usuario.autenticar_usuario(db, request.identificador, request.clave)  # 👈 nuevo import
            
            print("Resultado del login:")
            print(f"{resultado.loginResult} {resultado.apellido} {resultado.nombre}")

            usuarioResponse = usuarios_pb2.Usuario(
                id=resultado.id,
                nombreUsuario=resultado.nombreUsuario,
                nombre=resultado.nombre,
                apellido=resultado.apellido,
                telefono=resultado.telefono,
                email=resultado.email,
                rol=resultado.rol
            )

            loginResponse = usuarios_pb2.LoginResponse(
                result=resultado.loginResult,
                mensaje=resultado.mensaje,
                usuario=usuarioResponse
            )
            return loginResponse
        finally:
            db.close()

    # Listar usuarios
    def ListarUsuarios(self, request, context):
        print("Entró al método ListarUsuarios del servicio gRPC")
        db: Session = self.db_session()
        try:
            usuarios = crud_usuario.listar_usuarios(db)  # 👈 nuevo import
            usuarios_proto = [
                mapper.ProtoMapper.usuario_to_usuario_response_proto(usuario)
                for usuario in usuarios
            ]
            return usuarios_pb2.ListarUsuariosResponse(usuarios=usuarios_proto)
        finally:
            db.close()

    # Obtener usuario por ID
    def ObtenerUsuarioPorId(self, request, context):
        print("Entró al método ObtenerUsuarioPorId del servicio gRPC")
        db: Session = self.db_session()
        try:
            usuario = crud_usuario.obtener_usuario_por_id(db, request.id)  # 👈 nuevo import
            if usuario is None:
                context.set_code(grpc.StatusCode.NOT_FOUND)
                context.set_details(f"Usuario con ID {request.id} no encontrado.")
                return usuarios_pb2.UserResponse()  
            
            usuario_proto = mapper.ProtoMapper.usuario_to_usuario_response_proto(usuario)
            return usuarios_pb2.UserResponse(usuario=usuario_proto)
        finally:
            db.close()
