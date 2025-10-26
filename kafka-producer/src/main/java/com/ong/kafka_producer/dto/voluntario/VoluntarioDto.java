package com.ong.kafka_producer.dto.voluntario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoluntarioDto {
    private String idOrganizacion;
    private String idVoluntario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
}