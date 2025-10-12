package com.ong.kafka_producer.dto.evento_solidario;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoSolidarioDto {
    private Integer idOrganizacion;
    private Integer idEvento;
    private String nombre;
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaEvento;
    //private Boolean vigente;
}
