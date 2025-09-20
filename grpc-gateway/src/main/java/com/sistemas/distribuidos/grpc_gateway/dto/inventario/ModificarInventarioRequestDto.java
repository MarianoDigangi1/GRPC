package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificarInventarioRequestDto {
    private int id;
    private String descripcion;
    private int cantidad;
}
