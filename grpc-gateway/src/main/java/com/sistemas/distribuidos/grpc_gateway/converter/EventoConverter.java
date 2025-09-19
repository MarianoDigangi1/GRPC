package com.sistemas.distribuidos.grpc_gateway.converter;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;

import com.sistemas.distribuidos.grpc_gateway.stubs.eventos.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventoConverter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public static CrearEventoRequest convertCrearEventoRequestFromDto(CrearEventoRequestDto dto) {
        CrearEventoRequest request = CrearEventoRequest.newBuilder()
            .setNombre(dto.getNombre())
            .setDescripcion(dto.getDescripcion())
            .setFechaEventoIso(dto.getFechaEventoIso() != null ? dto.getFechaEventoIso().format(FORMATTER) : "")
            .addAllMiembrosIds(dto.getMiembrosIds() != null ? dto.getMiembrosIds() : List.of())
            .setActorUsuarioId(dto.getActorUsuarioId())
            .setActorRol(dto.getActorRol())
            .build();

        // DEBUG
          System.out.println("➡ Converter -> Request gRPC generado: " + request);

        return request;
    }

    public static CrearEventoResponseDto convertCrearEventoResponseToDto(CrearEventoResponse response) {
        return CrearEventoResponseDto.builder().evento(
                        EventoDto.builder()
                                .id(response.getEvento().getId())
                                .nombre(response.getEvento().getNombre())
                                .descripcion(response.getEvento().getDescripcion())
                                // String → LocalDateTime
                                .fechaEventoIso(!response.getEvento().getFechaEventoIso().isEmpty()
                                        ? LocalDateTime.parse(response.getEvento().getFechaEventoIso(), FORMATTER)
                                        : null)
                                .miembrosIds(response.getEvento().getMiembrosIdsList())
                                .build()
                )
                .mensaje(response.getMensaje())
                .build();
    }

    public static ModificarEventoRequest convertModificarEventoRequestFromDto(ModificarEventoRequestDto dto) {
        List<DonacionUsada> donacionesUsadasProto = dto.getDonacionesUsadas().stream()
                .map(dtoDonacion -> DonacionUsada.newBuilder()
                        .setInventarioId(dtoDonacion.getInventarioId())
                        .setCantidadUsada(dtoDonacion.getCantidadUsada())
                        .build())
                .toList();

        return ModificarEventoRequest.newBuilder()
                .setId(dto.getId())
                .setNombre(dto.getNombre())
                // LocalDateTime → String
                .setFechaEventoIso(dto.getFechaEventoIso() != null 
                        ? dto.getFechaEventoIso().format(FORMATTER) 
                        : "")
                .addAllAgregarMiembrosIds(dto.getAgregarMiembrosIds())
                .addAllQuitarMiembrosIds(dto.getQuitarMiembrosIds())
                .addAllDonacionesUsadas(donacionesUsadasProto)
                .setActorUsuarioId(dto.getActorUsuarioId())
                .setActorRol(dto.getActorRol())
                .build();
    }

    public static ModificarEventoResponseDto convertModificarEventoResponseToDto(ModificarEventoResponse response) {
        return ModificarEventoResponseDto.builder().evento(
                        EventoDto.builder()
                                .id(response.getEvento().getId())
                                .nombre(response.getEvento().getNombre())
                                .descripcion(response.getEvento().getDescripcion())
                                // String → LocalDateTime
                                .fechaEventoIso(!response.getEvento().getFechaEventoIso().isEmpty()
                                        ? LocalDateTime.parse(response.getEvento().getFechaEventoIso(), FORMATTER)
                                        : null)
                                .miembrosIds(response.getEvento().getMiembrosIdsList())
                                .build()
                )
                .mensaje(response.getMensaje())
                .build();
    }

    public static BajaEventoRequest convertBajaEventoRequestFromDto(BajaEventoRequestDto dto) {
        return BajaEventoRequest.newBuilder()
                .setId(dto.getId())
                .setActorUsuarioId(dto.getActorUsuarioId())
                .setActorRol(dto.getActorRol())
                .build();
    }

    public static AsignarQuitarRequest convertAsignarQuitarRequestFromDto(AsignarQuitarRequestDto dto) {
        return AsignarQuitarRequest.newBuilder()
                .setEventoId(dto.getEventoId())
                .setUsuarioId(dto.getUsuarioId())
                .setActorUsuarioId(dto.getActorUsuarioId())
                .setActorRol(dto.getActorRol())
                .setAgregar(dto.isAgregar())
                .build();
    }

    public static EventoDto convertEventoResponseToDto(EventoResponse response) {
        if (response == null) {
            return null;
        }

        // Debug: mostrar qué fecha está llegando del backend
        System.out.println("Fecha recibida gRPC: " + response.getFechaEventoIso());

        return EventoDto.builder()
                .id(response.getId())
                .nombre(response.getNombre())
                .descripcion(response.getDescripcion())
                // String → LocalDateTime
                .fechaEventoIso(!response.getFechaEventoIso().isEmpty()
                        ? LocalDateTime.parse(response.getFechaEventoIso(), FORMATTER)
                        : null)
                .miembrosIds(response.getMiembrosIdsList())
                .build();
    }
}
