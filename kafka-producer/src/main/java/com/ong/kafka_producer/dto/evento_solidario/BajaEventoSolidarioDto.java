package com.ong.kafka_producer.dto.evento_solidario;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BajaEventoSolidarioDto {
    private Integer idEvento;
    private String idOrganizacion;
}

