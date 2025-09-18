package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ModificarEventoResponseDto {
    private EventoDto evento;
    private String mensaje;
}
