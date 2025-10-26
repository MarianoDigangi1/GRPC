package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroGuardadoInputDTO {
    private String nombre;
    private String filtros;
}