package com.ong.kafka_producer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionDto {
    private Integer idOrganizacionSolicitante;
    private List<DonacionDto> donaciones;
}
