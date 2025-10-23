package com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.oferta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDonacionDto {
    private List<OfertaDto> donaciones;
}
