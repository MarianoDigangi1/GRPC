package com.ong.kafka_producer.dto.solicitud_donacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonacionDto {
    private String categoria;
    private String descripcion;
}
