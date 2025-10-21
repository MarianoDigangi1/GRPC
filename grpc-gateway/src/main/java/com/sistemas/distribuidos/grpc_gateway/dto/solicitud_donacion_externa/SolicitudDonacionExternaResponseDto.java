package com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionExternaResponseDto {
    private Integer id;
    private String idOrganizacionExterna;
    private String idSolicitud;
    private List<SolicitudDonacionExternaItemResponseDto> contenido;
    private String recibidaEn;
    private boolean vigente;
}
