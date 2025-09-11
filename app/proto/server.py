import grpc
from concurrent import futures
from sqlalchemy.orm import Session
from app.services.usuario_service import UsuarioService
from app.proto import usuarios_pb2_grpc

def server():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    usuarios_pb2_grpc.add_UsuarioServiceServicer_to_server(UsuarioService(), server)
    server.add_insecure_port("[::]:50052")
    print("Servidor gRPC escuchando en puerto 50052...")
    server.start()
    server.wait_for_termination()
