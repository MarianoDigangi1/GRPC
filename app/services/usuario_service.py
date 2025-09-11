from app import crud, schemas, database
from app.proto import usuarios_pb2, usuarios_pb2_grpc
from sqlalchemy.orm import Session
import grpc

class UsuarioService(usuarios_pb2_grpc.UsuarioServiceServicer):
    
    def __init__(self):
        self.db_session = database.SessionLocal

    # Funciona correctamente
    def CrearUsuario(self, request, context):
        print("Entró al método CrearUsuario del servicio gRPC")
        db: Session = self.db_session()
        print(request.nombreUsuario)
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

            # Llamar a crud.crear_usuario con el objeto correcto
            mensaje_crud = crud.crear_usuario(db, usuario_create)

            if mensaje_crud.startswith("Error"):
                context.set_details(mensaje_crud)
                context.set_code(grpc.StatusCode.ALREADY_EXISTS)
            
            return usuarios_pb2.UsuarioResponse(mensaje=mensaje_crud)
        finally:
            db.close()

    def ModificarUsuario(self, request, context):
        print("Entró al método ModificarUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            # Convierto el estado de string a booleano
            estado_bool = True if request.estado.lower() == "activo" else False
            
            print("Request recibido en ModificarUsuario:")
            print(request)
            usuario_update = schemas.UsuarioUpdate(
                nombreUsuario=request.nombreUsuario, 
                nombre=request.nombre,
                apellido=request.apellido,
                telefono=request.telefono,
                emailUnico=request.emailUnico,
                rol=request.rol,
                estaActivo=estado_bool
            )

            mensaje = crud.modificar_usuario(db, request.id, usuario_update)
            return usuarios_pb2.ModificarUsuarioResponse(mensaje=mensaje)
        finally:
            db.close()

    # Funciona correctamente
    def BajaUsuario(self, request, context):
        print("Entró al método BajaUsuario del servicio gRPC")
        db: Session = self.db_session()
        try:
            mensaje = crud.baja_usuario(db, request.id)
            return usuarios_pb2.BajaUsuarioResponse(mensaje=mensaje)
        finally:
            db.close()

    def Login(self, request, context):
        db: Session = self.db_session()
        try:
            resultado = crud.autenticar_usuario(db, request.identificador, request.clave)
            
            print("Resultado del login:")
            print(resultado.loginResult + " " + resultado.apellido + " " + resultado.nombre)

            usuarioResponse = usuarios_pb2.Usuario(
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
