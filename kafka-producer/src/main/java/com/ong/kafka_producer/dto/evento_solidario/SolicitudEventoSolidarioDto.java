package com.ong.kafka_producer.dto.evento_solidario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEventoSolidarioDto {
    private String idSolicitud;
    private Integer idOrganizacionSolicitante;
    private List<EventoExternoDto> donaciones;
}
