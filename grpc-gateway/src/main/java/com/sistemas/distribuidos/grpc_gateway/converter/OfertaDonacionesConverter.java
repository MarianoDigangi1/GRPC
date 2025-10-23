package com.sistemas.distribuidos.grpc_gateway.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones.OfertaDonacionItemResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones.OfertaDonacionResponseDto;
import com.sistemas.distribuidos.grpc_gateway.stubs.donaciones_ofrecidas.DonacionesOfrecidasResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.donaciones_ofrecidas.ListarDonacionesOfrecidasResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfertaDonacionesConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<OfertaDonacionResponseDto> convertSolicitudListResponseToDto(ListarDonacionesOfrecidasResponse response) {
        return response.getSolicitudesList().stream()
                .map(OfertaDonacionesConverter::convertDonacionToResponseDto)
                .collect(Collectors.toList());
    }

    private static OfertaDonacionResponseDto convertDonacionToResponseDto(DonacionesOfrecidasResponse donacion) {
        OfertaDonacionResponseDto dto = new OfertaDonacionResponseDto();
        dto.setId(donacion.getId());
        dto.setExternalOrgId(donacion.getExternalOrgId());
        dto.setOfertaId(donacion.getOfertaId());
        dto.setItems(convertContenido(donacion.getContenido()));
        return dto;
    }

    private static List<OfertaDonacionItemResponseDto> convertContenido(String contenidoJson) {
        try {
            return objectMapper.readValue(contenidoJson,
                    new TypeReference<List<OfertaDonacionItemResponseDto>>() {}
            );
        } catch (Exception e) {
            // Log del error para debugging
            System.err.println("Error parseando contenido: " + e.getMessage());
            System.err.println("Contenido recibido: " + contenidoJson);
            return new ArrayList<>();
        }
    }
}
