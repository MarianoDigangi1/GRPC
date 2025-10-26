package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReporteDonacionDTO {
    private String categoria;
    private Boolean eliminado;
    @JsonProperty("total_cantidad")
    private Long totalCantidad;
}