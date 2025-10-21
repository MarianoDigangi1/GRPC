import grpc
from concurrent import futures
from app.services.usuario_service import UsuarioService
from app.proto import usuarios_pb2_grpc
from app.services.eventos_service import EventoService
from app.proto import eventos_pb2_grpc
from app.services.inventario_service import InventarioService
from app.proto import inventario_pb2_grpc
from app.services.solicitudes_externas_service import SolicitudesExternasService
from app.proto import solicitudesExternas_pb2_grpc


def server():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))

    # Registrar servicios existentes
    usuarios_pb2_grpc.add_UsuarioServiceServicer_to_server(UsuarioService(), server)
    eventos_pb2_grpc.add_EventoServiceServicer_to_server(EventoService(), server)
    inventario_pb2_grpc.add_InventarioServiceServicer_to_server(InventarioService(), server)
    solicitudesExternas_pb2_grpc.add_SolicitudesExternasServiceServicer_to_server(SolicitudesExternasService(), server)

    server.add_insecure_port("[::]:50052")
    print("Servidor gRPC escuchando en puerto 50052...")
    server.start()
    server.wait_for_termination()
