package com.ong.kafka_producer.dto.solicitud_donacion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoDto {
    @JsonProperty("categoria")
    private String categoria;

    @JsonProperty("descripcion")
    private String descripcion;
}
