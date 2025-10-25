package com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonacionDto {
    private String categoria;
    private String descripcion;
    private Integer cantidad;
}
