package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.EventoConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.EventoServiceGrpc;
import com.sistemas.distribuidos.grpc_gateway.stubs.*;
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
        } catch (Exception e) {
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

    public String asignarOQuitarUsuario(AsignarQuitarRequestDto dto) {
        AsignarQuitarResponse response;

        try {
            response = stubBlocking.asignarOQuitarMiembro(EventoConverter.convertAsignarQuitarRequestFromDto(dto));
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return response.getMensaje();
    }
}
