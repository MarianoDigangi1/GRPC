package com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionExternaItemResponseDto {
    private String categoria;
    private String descripcion;
}
