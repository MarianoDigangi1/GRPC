package com.sistemas.distribuidos.grpc_gateway.service;



import java.util.List;
import java.util.stream.Collectors;

import com.sistemas.distribuidos.grpc_gateway.converter.EventoConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.eventos.*;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@Service
public class EventosService {

    private final EventoServiceGrpc.EventoServiceBlockingStub stubBlocking;

    @Autowired
    public EventosService(ManagedChannel grpcChannel) {
        this.stubBlocking = EventoServiceGrpc.newBlockingStub(grpcChannel);
    }

    public CrearEventoResponseDto crearEvento(CrearEventoRequestDto dto) {
    try {
        CrearEventoResponse response =
            stubBlocking.crearEvento(EventoConverter.convertCrearEventoRequestFromDto(dto));
        return EventoConverter.convertCrearEventoResponseToDto(response);

    } catch (StatusRuntimeException e) {
        // Validación de negocio desde el microservicio
        if (e.getStatus().getCode() == Status.Code.INVALID_ARGUMENT) {
            throw new IllegalArgumentException(e.getStatus().getDescription());
        }
        // Errores de conexión u otros
        throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);

    } catch (Exception e) {
        throw new GrpcConnectionException("Error inesperado en gRPC: " + e.getMessage(), e);
    }
}

    public ModificarEventoResponseDto modificarEvento(ModificarEventoRequestDto dto) {
    try {
        // Convertir el DTO a request gRPC
        var request = EventoConverter.convertModificarEventoRequestFromDto(dto);
        // Log opcional
        System.out.println("➡️ Service: Request gRPC -> " + request);

        // Llamada al stub (una sola vez)
        ModificarEventoResponse response = stubBlocking.modificarEvento(request);

        // Log opcional
        System.out.println(" Service: Response gRPC -> " + response);

        // Convertir la respuesta a DTO y devolver
        return EventoConverter.convertModificarEventoResponseToDto(response);
    } catch (Exception e) {
        throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
    }
}


    public String darBajaEvento(BajaEventoRequestDto dto) {
        BajaEventoResponse response;

        try {
            response = stubBlocking.bajaEvento(EventoConverter.convertBajaEventoRequestFromDto(dto));
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return response.getMensaje();
    }

    public EventoDto buscarEventoPorId(int id) {
        List<EventoDto> eventos = listarEventos();
        return eventos.stream()
                .filter(ev -> ev.getId() == id)
                .findFirst()
                .orElse(null);
    }


    public String asignarOQuitarUsuario(AsignarQuitarRequestDto dto) {
        AsignarQuitarResponse response;

        try {
            response = stubBlocking.asignarOQuitarMiembro(EventoConverter.convertAsignarQuitarRequestFromDto(dto));
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return response.getMensaje();
    }
    
    public List<EventoDto> listarEventos() {
    ListarEventosResponse response;

    try {
        response = stubBlocking.listarEventosDisponibles(Empty.newBuilder().build());
    } catch (Exception e) {
        throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
    }

    // Convertir la respuesta gRPC a DTOs
    return response.getEventosList().stream()
            .map(EventoConverter::convertEventoResponseToDto)
            .collect(Collectors.toList());
}
}
