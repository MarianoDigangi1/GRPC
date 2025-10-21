from app.proto import usuarios_pb2, eventos_pb2, inventario_pb2, solicitudesExternas_pb2
from app import schemas
from datetime import datetime
import json


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
    def usuario_to_usuario_response_proto(request):
        #print(request.estaActivo)

        variablePrueba = getattr(request, "estaActivo", True) 
        #print(variablePrueba)

        return usuarios_pb2.UserResponse(
            nombreUsuario=getattr(request, "nombreUsuario", "") or "",
            id=getattr(request, "id", 0) or 0,
            apellido=getattr(request, "apellido", "") or "",
            nombre=getattr(request, "nombre", "") or "",
            telefono=getattr(request, "telefono", "") or "",
            email=getattr(request, "email", "") or "",
            rol=getattr(request, "rol", "") or "",
            activo=getattr(request, "estaActivo", True)
        )
    
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
        """
        Mapea la respuesta del CRUD de modificación/baja de usuario al proto gRPC.
        """
        print("✅ Mapper Proto: preparando UpdateAndDeleteUserResponse con mensaje:", usuario.mensaje)
        return usuarios_pb2.UpdateAndDeleteUserResponse(
            mensaje=getattr(usuario, "mensaje", "Usuario actualizado correctamente")
        )



    @staticmethod
    def evento_to_proto(evento_resp: schemas.EventoResponse) -> eventos_pb2.Evento:
        return eventos_pb2.Evento(
            id=evento_resp.id,
            nombre=evento_resp.nombre,
            descripcion=evento_resp.descripcion or "",
            fecha_evento_iso=evento_resp.fecha_evento_iso,
            miembros_ids=evento_resp.miembros_ids
        )

    @staticmethod
    def evento_create_response_proto(evento_resp: schemas.EventoResponse, mensaje: str):
        return eventos_pb2.CrearEventoResponse(
            evento=ProtoMapper.evento_to_proto(evento_resp),
            mensaje=mensaje
        )

    @staticmethod
    def evento_update_response_proto(evento_resp: schemas.EventoResponse, mensaje: str):
        return eventos_pb2.ModificarEventoResponse(
            evento=ProtoMapper.evento_to_proto(evento_resp),
            mensaje=mensaje
        )
    @staticmethod
    def evento_to_evento_response_proto(request):

        from datetime import datetime
        fecha_iso = getattr(request, "fecha_evento_iso", "") or ""


        #if getattr(request, "fecha_evento", None) is None:
        #    print(f"[DEBUG] evento.id={getattr(request, 'id', None)} → NO tiene atributo fecha_evento")
        #else:
        #    print(f"[DEBUG] evento.id={getattr(request, 'id', None)} → fecha_evento={request.fecha_evento}")

        return eventos_pb2.EventoResponse(
        id=getattr(request, "id", 0) or 0,
        nombre=getattr(request, "nombre", "") or "",
        descripcion=getattr(request, "descripcion", "") or "",
        fecha_evento_iso=fecha_iso,
        miembros_ids=list(getattr(request, "miembros_ids", [])) or [],
        pasado=datetime.fromisoformat(fecha_iso) < datetime.now() if fecha_iso else False
        )
    
    @staticmethod
    def inventario_to_inventario_list_response_proto(request):
        return inventario_pb2.InventarioListResponse(
            id=getattr(request, "id", 0) or 0,
            categoria=getattr(request, "categoria", "") or "",
            descripcion=getattr(request, "descripcion", "") or "",
            cantidad=getattr(request, "cantidad", 0) or 0
        )
    
    @staticmethod
    def inventario_to_obtener_inventario_por_id_response_proto(request, mensaje=""):
        return inventario_pb2.ObtenerInventarioPorIdResponse(
            inventario = inventario_pb2.Inventario(
                categoria=getattr(request, "categoria", "") or "",
                descripcion=getattr(request, "descripcion", "") or "",
                cantidad=getattr(request, "cantidad", 0) or 0
            ),
            message=mensaje
        )
    
    @staticmethod
    def solicitud_to_solicitud_list_response_proto(request):
        
        # Convierte contenido a JSON valido con comillas dobles
        contenido = getattr(request, "contenido", {})
        contenido_json = json.dumps(contenido) if contenido else ""
        
        return solicitudesExternas_pb2.SolicitudesExternasResponse(
            id=getattr(request, "id", 0) or 0,
            external_org_id=getattr(request, "external_org_id", "") or "",
            solicitud_id=getattr(request, "solicitud_id", "") or "",
            contenido=contenido_json,
            recibida_en=str(getattr(request, "recibida_en", "")) or "",
            vigente=getattr(request, "vigente", True)
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
        #print("Mapper Schema - LoginResponse a devolver:", request.id, request.nombreUsuario)
        return schemas.LoginResponse(
            id=request.id,
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

       # print("Mapper Schema - Usuario a devolver:")
        usuarioResponse = schemas.UsuarioDeleteAndUpdateResponse(
            nombreUsuario=usuario.nombreUsuario,
            nombre=usuario.nombre,
            apellido=usuario.apellido,
            telefono=usuario.telefono,
            email=usuario.email,
            rol=usuario.rol,
            mensaje=mensaje
        )
      #  print("UsuarioResponse creado:")
      #  print(usuarioResponse)

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
       # print("UsuarioResponse creado:")
        #print(usuarioResponse)

        return usuarioResponse

    @staticmethod
    def evento_model_to_response(evento, miembros_ids, mensaje=""):
        from datetime import datetime
        return schemas.EventoResponse(
            id=evento.id,
            nombre=evento.nombre,
            descripcion=evento.descripcion,
            fecha_evento_iso=evento.fecha_evento.isoformat() if evento.fecha_evento else "",
            miembros_ids=miembros_ids,
            mensaje=mensaje
        )    
    
    def inventario_to_schema(inventario):
        return {
        "id": getattr(inventario, "id", None),
        "categoria": getattr(inventario, "categoria", None),
        "descripcion": getattr(inventario, "descripcion", None),
        "cantidad": getattr(inventario, "cantidad", None),
        "eliminado": getattr(inventario, "eliminado", None),
        "fecha_alta": inventario.fecha_alta.isoformat() if getattr(inventario, "fecha_alta", None) else None,
        "usuario_alta": getattr(inventario, "usuario_alta", None),
        "fecha_modificacion": inventario.fecha_modificacion.isoformat() if getattr(inventario, "fecha_modificacion", None) else None,
        "usuario_modificacion": getattr(inventario, "usuario_modificacion", None),
    }


        

 