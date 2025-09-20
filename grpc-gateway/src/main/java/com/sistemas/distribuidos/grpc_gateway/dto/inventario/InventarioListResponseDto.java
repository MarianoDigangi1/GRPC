package com.sistemas.distribuidos.grpc_gateway.dto.inventario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InventarioListResponseDto {
    @JsonIgnore
    private int id;
    private String categoria;
    private String descripcion;
    private int cantidad;
}
