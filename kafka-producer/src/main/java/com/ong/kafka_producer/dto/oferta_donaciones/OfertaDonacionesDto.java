package com.ong.kafka_producer.dto.oferta_donaciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfertaDonacionesDto {
    private String idOferta;
    private String idOrganizacionDonante;
    private List<DonacionItemDto> donaciones;
}
