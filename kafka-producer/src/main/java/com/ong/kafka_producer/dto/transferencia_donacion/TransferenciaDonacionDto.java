package com.ong.kafka_producer.dto.transferencia_donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDonacionDto {
    private String idTransferencia;
    private Integer idOrganizacionOrigen;
    private Integer idOrganizacionDestino;
    private List<DonacionDto> donaciones;
}