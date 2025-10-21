package com.sistemas.distribuidos.grpc_gateway.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaItemResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaResponseDto;
import com.sistemas.distribuidos.grpc_gateway.stubs.solicitudes_externas.ListarSolicitudesExternasResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.solicitudes_externas.SolicitudesExternasResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitudDonacionesExternasConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<SolicitudDonacionExternaResponseDto> convertSolicitudListResponseToDto(ListarSolicitudesExternasResponse response) {
        return response.getSolicitudesList().stream()
                .map(SolicitudDonacionesExternasConverter::convertSolicitudtoResponseDto)
                .collect(Collectors.toList());
    }

    private static SolicitudDonacionExternaResponseDto convertSolicitudtoResponseDto(SolicitudesExternasResponse solicitud) {
        SolicitudDonacionExternaResponseDto dto = new SolicitudDonacionExternaResponseDto();
        dto.setId(solicitud.getId());
        dto.setIdOrganizacionExterna(solicitud.getExternalOrgId());
        dto.setIdSolicitud(solicitud.getSolicitudId());
        dto.setContenido(convertContenido(solicitud.getContenido()));
        dto.setRecibidaEn(solicitud.getRecibidaEn());
        dto.setVigente(solicitud.getVigente());
        return dto;
    }

    private static List<SolicitudDonacionExternaItemResponseDto> convertContenido(String contenidoJson) {
        try {
            // Convertir comillas simples a comillas dobles para que sea JSON válido
            //String jsonValido = contenidoJson.replace("'", "\"");
            
            return objectMapper.readValue(contenidoJson,
                    new TypeReference<List<SolicitudDonacionExternaItemResponseDto>>() {}
            );
        } catch (Exception e) {
            // Log del error para debugging
            System.err.println("Error parseando contenido: " + e.getMessage());
            System.err.println("Contenido recibido: " + contenidoJson);
            return new ArrayList<>();
        }
    }
}
