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

@Service
public class EventosService {

    private final EventoServiceGrpc.EventoServiceBlockingStub stubBlocking;

    @Autowired
    public EventosService(ManagedChannel grpcChannel) {
        this.stubBlocking = EventoServiceGrpc.newBlockingStub(grpcChannel);
    }

    public CrearEventoResponseDto crearEvento(CrearEventoRequestDto dto) {
        CrearEventoResponse response;

        try {
            response = stubBlocking.crearEvento(EventoConverter.convertCrearEventoRequestFromDto(dto));
            System.out.println(" Service -> Respuesta gRPC: " + response);
        } catch (Exception e) {
            System.out.println("  Service -> Error gRPC: " + e.getMessage());
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return EventoConverter.convertCrearEventoResponseToDto(response);
    }

    public ModificarEventoResponseDto modificarEvento(ModificarEventoRequestDto dto) {
        ModificarEventoResponse response;

        try {
            response = stubBlocking.modificarEvento(EventoConverter.convertModificarEventoRequestFromDto(dto));
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return EventoConverter.convertModificarEventoResponseToDto(response);
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
