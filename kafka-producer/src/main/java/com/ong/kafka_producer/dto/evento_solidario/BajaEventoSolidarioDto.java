package com.ong.kafka_producer.dto.evento_solidario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BajaEventoSolidarioDto {
    private Integer idEvento;
    private String idOrganizacion;
}

