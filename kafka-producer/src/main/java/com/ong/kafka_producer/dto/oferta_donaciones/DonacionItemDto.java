package com.ong.kafka_producer.dto.oferta_donaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonacionItemDto {
    private String categoria;
    private String descripcion;
    private String cantidad;
}
