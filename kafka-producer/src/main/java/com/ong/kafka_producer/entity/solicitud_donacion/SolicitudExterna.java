package com.ong.kafka_producer.entity.solicitud_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud_externa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // esto quizas no haga falta, pero lo agrego por los dduas

    @Column(name = "external_org_id")
    private String externalOrgId;

    @Column(name = "solicitud_id", nullable = false)
    private String solicitudId;

    @Column(columnDefinition = "json", nullable = false)
    private String contenido;

    @Column(name = "recibida_en")
    private LocalDateTime recibidaEn;

    @Column(name = "vigente")
    private Boolean vigente = true;

}
