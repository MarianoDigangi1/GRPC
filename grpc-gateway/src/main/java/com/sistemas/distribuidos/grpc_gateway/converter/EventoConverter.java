package com.sistemas.distribuidos.grpc_gateway.converter;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.stubs.*;

import java.util.List;

public class EventoConverter {

    public static CrearEventoRequest convertCrearEventoRequestFromDto(CrearEventoRequestDto dto) {
        return CrearEventoRequest.newBuilder()
                .setNombre(dto.getNombre())
                .setDescripcion(dto.getDescripcion())
                .setFechaEventoIso(dto.getFechaEventoIso())
                .addAllMiembrosIds(dto.getMiembrosIds())
                .setActorUsuarioId(dto.getActorUsuarioId())
                .setActorRol(dto.getActorRol())
                .build();
    }

    public static CrearEventoResponseDto convertCrearEventoResponseToDto(CrearEventoResponse response) {
        return CrearEventoResponseDto.builder().evento(
                        EventoDto.builder()
                                .id(response.getEvento().getId())
                                .nombre(response.getEvento().getNombre())
                                .descripcion(response.getEvento().getDescripcion())
                                .fechaEventoIso(response.getEvento().getFechaEventoIso())
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
                .setFechaEventoIso(dto.getFechaEventoIso())
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
                                .fechaEventoIso(response.getEvento().getFechaEventoIso())
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
}
