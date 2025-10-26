package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventoParticipacionDTO {
    private String dia;
    private String nombre;
    private String descripcion;
}
