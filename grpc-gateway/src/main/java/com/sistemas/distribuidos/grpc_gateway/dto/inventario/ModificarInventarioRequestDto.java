package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ModificarInventarioRequestDto {
    private int id;
    private String descripcion;
    private int cantidad;
}
