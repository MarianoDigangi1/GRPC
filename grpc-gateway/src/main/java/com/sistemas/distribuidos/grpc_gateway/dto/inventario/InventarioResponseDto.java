package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventarioResponseDto {
    private boolean success;
    private String message;
    private int id;
}
