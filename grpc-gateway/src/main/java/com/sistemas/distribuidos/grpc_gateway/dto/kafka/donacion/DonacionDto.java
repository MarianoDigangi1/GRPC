package com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonacionDto {
    private String categoria;
    private String descripcion;
}
