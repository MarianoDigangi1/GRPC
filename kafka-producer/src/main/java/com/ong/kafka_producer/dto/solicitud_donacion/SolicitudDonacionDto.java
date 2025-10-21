package com.ong.kafka_producer.dto.solicitud_donacion;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitudDonacionDto {
    private String idSolicitud;
    private String idOrganizacionSolicitante;
    private List<ContenidoDto> contenido;
}
