package com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionDto {
    private List<DonacionDto> contenido;
}
