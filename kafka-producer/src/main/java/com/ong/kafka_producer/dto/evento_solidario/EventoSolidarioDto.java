package com.ong.kafka_producer.dto.evento_solidario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoSolidarioDto {
    private Long idOrganizacion;
    private String idEvento;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaEvento;
    //private Boolean vigente;
}
