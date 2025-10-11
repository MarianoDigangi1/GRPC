package com.ong.kafka_producer.dto.baja_donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BajaDonacionDto {
    private String idSolicitud;
    private Integer idOrganizacionSolicitante;
}
