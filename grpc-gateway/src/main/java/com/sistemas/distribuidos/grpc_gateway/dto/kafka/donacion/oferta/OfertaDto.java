package com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.oferta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDto {
    private String categoria;
    private String descripcion;
    private Integer cantidad;
}
