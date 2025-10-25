package com.sistemas.distribuidos.grpc_gateway.dto.kafka.transferencia;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.DonacionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDonacionDto {
    private String idTransferencia;
    private String idOrganizacionOrigen;
    private String idOrganizacionDestino;
    private List<DonacionDto> donaciones;
}
