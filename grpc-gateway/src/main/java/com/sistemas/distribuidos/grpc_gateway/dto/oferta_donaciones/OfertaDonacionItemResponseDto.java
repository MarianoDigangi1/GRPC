package com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDonacionItemResponseDto {
    private String categoria;
    private String descripcion;
    private Integer cantidad;
}
