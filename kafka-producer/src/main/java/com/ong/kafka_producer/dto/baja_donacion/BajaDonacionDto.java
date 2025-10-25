package com.ong.kafka_producer.dto.baja_donacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BajaDonacionDto {
    private String idSolicitud;
    private String idOrganizacionSolicitante;
}
