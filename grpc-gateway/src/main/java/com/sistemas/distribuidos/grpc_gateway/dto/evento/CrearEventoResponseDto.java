package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CrearEventoResponseDto {
    private EventoDto evento;
    private String mensaje;
}
