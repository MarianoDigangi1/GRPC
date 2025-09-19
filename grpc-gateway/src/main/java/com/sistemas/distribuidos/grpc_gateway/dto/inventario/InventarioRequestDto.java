package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventarioRequestDto {
    private String categoria;
    private String descripcion;
    private int cantidad;
}
