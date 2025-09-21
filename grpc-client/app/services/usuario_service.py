from app import crud, schemas, database, mapper
from app.proto import usuarios_pb2, usuarios_pb2_grpc
from app.models import RolEnum  
from sqlalchemy.orm import Session
import grpc

class UsuarioService(usuarios_pb2_grpc.UsuarioServiceServicer):
    
    def __init__(self):
        self.db_session = database.SessionLocal

    # Funciona correctamente
    def CrearUsuario(self, request, context):
        print("Entró al método CrearUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            # Crear el objeto UsuarioCreate desde el request
            usuario_create = schemas.UsuarioCreate(
                nombreUsuario=request.nombreUsuario,
                nombre=request.nombre,
                apellido=request.apellido,
                telefono=request.telefono,
                email=request.email,
                rol=request.rol
            )
            # Crea el usuario en la base de datos
            usuarioResponse = crud.crear_usuario(db, usuario_create) 
            # Mapea el resultado a un objeto proto, para usarlo con gRPC      
            usuarioResponseProto = mapper.ProtoMapper.usuario_to_create_user_request_proto(usuarioResponse)
            # Retorna el objeto proto
            return usuarioResponseProto
        finally:
            db.close()

########################################################################################################
########################################################################################################
# Modificar usuario
########################################################################################################
########################################################################################################
    def ModificarUsuario(self, request, context):
        print("Estoy en clase usuario_service: Entró al método ModificarUsuario del servicio gRPC")
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
 
            usuarioUpdate = crud.modificar_usuario(db, request.id, usuario_update)

            #print("Estoy en clase usuario_service: Usuario modificado")
            #print(usuarioUpdate)

            usuarioToProto = usuarios_pb2.CreateUserRequest(
                nombreUsuario=usuarioUpdate.nombreUsuario,
                nombre=usuarioUpdate.nombre,
                apellido=usuarioUpdate.apellido,
                telefono=usuarioUpdate.telefono if usuarioUpdate.telefono else "",
                email=usuarioUpdate.email,
                rol=usuarioUpdate.rol if usuarioUpdate.rol else ""
            )
            #print("Estoy en clase usuario_service: Retornando respuesta de usuario modificado")
            #print(usuarioUpdate)

            return usuarios_pb2.UpdateAndDeleteUserResponse(
                usuario=usuarioToProto,
                mensaje=usuarioUpdate.mensaje
            )
        
            #mapper.ProtoMapper.usuario_to_usuario_delete_and_update_response_proto(usuario_update)
        finally:
            db.close()

    # Funciona correctamente
    def BajaUsuario(self, request, context):
        print("Entró al método BajaUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            mensaje = crud.baja_usuario(db, request.id)
            return usuarios_pb2.UpdateAndDeleteUserResponse(mensaje=mensaje)
        finally:
            db.close()

    def Login(self, request, context):
        db: Session = self.db_session()
        try:
            resultado = crud.autenticar_usuario(db, request.identificador, request.clave)
            
            print("Resultado del login:")
            print(resultado.loginResult + " " + resultado.apellido + " " + resultado.nombre)

            usuarioResponse = usuarios_pb2.Usuario(
                id=resultado.id,
                nombreUsuario=resultado.nombreUsuario,
                nombre=resultado.nombre,
                apellido=resultado.apellido,
                telefono=resultado.telefono,
                email=resultado.email,
                rol=resultado.rol
            )

            print("UsuarioResponse creado:" + str(usuarioResponse))
            
            loginResponse = usuarios_pb2.LoginResponse(
                result=resultado.loginResult,
                mensaje=resultado.mensaje,
                usuario=usuarioResponse
            )

            return loginResponse
        finally:
            db.close()

    def ListarUsuarios(self, request, context):
        print("Entró al método ListarUsuarios del servicio gRPC")
        db: Session = self.db_session()
        try:
            usuarios = crud.listar_usuarios(db)
            usuarios_proto = []

            for usuario in usuarios:
                usuario_proto = mapper.ProtoMapper.usuario_to_usuario_response_proto(usuario)
                usuarios_proto.append(usuario_proto)
            response = usuarios_pb2.ListarUsuariosResponse(usuarios=usuarios_proto)
            return response
        finally:
            db.close()

    def ObtenerUsuarioPorId(self, request, context):
        print("Entró al método ObtenerUsuarioPorId del servicio gRPC")
        db: Session = self.db_session()
        try:
            usuario = crud.obtener_usuario_por_id(db, request.id)
            if usuario is None:
                context.set_code(grpc.StatusCode.NOT_FOUND)
                context.set_details(f"Usuario con ID {request.id} no encontrado.")
                return usuarios_pb2.UserResponse()  # Retorna un objeto vacío en caso de no encontrar el usuario
            
            usuario_proto = mapper.ProtoMapper.usuario_to_usuario_response_proto(usuario)
            return usuarios_pb2.UserResponse(usuario=usuario_proto)
        finally:
            db.close()
