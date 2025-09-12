from app.proto import usuarios_pb2
from app import schemas

class ProtoMapper:
    
    @staticmethod
    def usuario_to_create_user_request_proto(usuario):
        usuarioCreate = usuarios_pb2.CreateUserRequest(
            nombreUsuario=getattr(usuario, "nombreUsuario", "") or "",
            nombre=getattr(usuario, "nombre", "") or "",
            apellido=getattr(usuario, "apellido", "") or "",
            telefono=getattr(usuario, "telefono", "") or "",
            email=getattr(usuario, "email", "") or "",
            rol=getattr(usuario, "rol", "") or ""
        )    
        usuarioCreateResponse = usuarios_pb2.CreateUserResponse(
            usuario=usuarioCreate,
            mensaje=getattr(usuario, "mensaje", "") or ""
        ) 
        return usuarioCreateResponse


    @staticmethod
    def usuario_response_to_usuario_proto(usuario):
        return usuarios_pb2.Usuario(
            nombreUsuario=getattr(usuario, "nombreUsuario", "") or "",
            nombre=getattr(usuario, "nombre", "") or "",
            apellido=getattr(usuario, "apellido", "") or "",
            telefono=getattr(usuario, "telefono", "") or "",
            email=getattr(usuario, "email", "") or "",
            rol=getattr(usuario, "rol", "") or "",
            activo=getattr(usuario, "estaActivo", True)
        )

    @staticmethod
    def usuario_to_usuario_delete_and_update_response_proto(usuario: schemas.UsuarioDeleteAndUpdateResponse):
        print("Mapper Proto - Usuario a devolver:")
        print(usuario)
        usuarioCreate = usuarios_pb2.CreateUserRequest(
            nombreUsuario=getattr(usuario, "nombreUsuario", "") or "",
            nombre=getattr(usuario, "nombre", "") or "",
            apellido=getattr(usuario, "apellido", "") or "",
            telefono=getattr(usuario, "telefono", "") or "",
            email=getattr(usuario, "email", "") or "",
            rol=getattr(usuario, "rol", "") or ""
        )    
        print("UsuarioCreate creado:")
        print(usuario.mensaje)
        return usuarios_pb2.UpdateAndDeleteUserResponse(
            usuario=usuarioCreate,
            mensaje=usuario.mensaje 
        )
    
class SchemaMapper:
    
    @staticmethod
    def usuario_request_to_usuario_response(request, success, generated_password, mensaje):
        return schemas.UsuarioResponse(
            nombreUsuario=request.nombreUsuario,
            nombre=request.nombre,
            apellido=request.apellido,
            telefono=request.telefono,
            email=request.email,
            rol=request.rol,
            estaActivo=success,
            generated_password=generated_password,
            mensaje=mensaje
        )
        
    @staticmethod
    def login_request_to_login_response(request, loginResult, mensaje):
        return schemas.LoginResponse(
            loginResult=loginResult,
            mensaje=mensaje,
            nombreUsuario=request.nombreUsuario,
            nombre=request.nombre,
            apellido=request.apellido,
            telefono=request.telefono,
            email=request.email,
            rol=request.rol
        )
    
    @staticmethod
    def usuario_to_usuario_delete_and_update_response(usuario, mensaje):

        print("Mapper Schema - Usuario a devolver:")
        usuarioResponse = schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol,
            mensaje=mensaje
        )
        print("UsuarioResponse creado:")
        print(usuarioResponse)

        return usuarioResponse
    
    @staticmethod
    def usuario_to_usuario_delete_and_update_response_error():

        usuarioResponse = schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario="",
            nombre="",
            apellido="",
            telefono="",
            email="",
            rol=None,
            mensaje="Error: Usuario no encontrado"
        )
        print("UsuarioResponse creado:")
        print(usuarioResponse)

        return usuarioResponse

 