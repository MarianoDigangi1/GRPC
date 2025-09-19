package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BajaInventarioRequestDto {
    private int id;
}
