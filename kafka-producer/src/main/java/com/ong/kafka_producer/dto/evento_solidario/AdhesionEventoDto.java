package com.ong.kafka_producer.dto.evento_solidario;

import com.ong.kafka_producer.dto.voluntario.VoluntarioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdhesionEventoDto {
    private Integer idEvento;
    private String idOrganizacionEvento;
    private VoluntarioDto voluntario;
}
