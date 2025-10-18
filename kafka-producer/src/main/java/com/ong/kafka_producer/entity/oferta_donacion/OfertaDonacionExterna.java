package com.ong.kafka_producer.entity.oferta_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "oferta_externa")
public class OfertaDonacionExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "external_org_id")
    private String externalOrgId;

    @Column(name = "oferta_id", nullable = false, length = 100)
    private String ofertaId;

    @Column(columnDefinition = "json", nullable = false)
    private String contenido;

    @Column(name = "recibida_en")
    private LocalDateTime recibidaEn;


}
