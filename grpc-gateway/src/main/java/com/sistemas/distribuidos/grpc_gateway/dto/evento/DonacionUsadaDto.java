package com.sistemas.distribuidos.grpc_gateway.dto.evento;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DonacionUsadaDto {
    private int inventarioId;
    private int cantidadUsada;
}
