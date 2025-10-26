package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroGuardadoDTO {
    private String id;
    private String nombre;
    private String filtros;
}