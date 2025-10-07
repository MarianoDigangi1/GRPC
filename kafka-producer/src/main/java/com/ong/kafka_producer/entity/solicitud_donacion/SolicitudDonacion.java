package com.ong.kafka_producer.entity.solicitud_donacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitud_donacion_externa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudDonacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // esto quizas no haga falta, pero lo agrego por los dduas

    @Column(name = "id_solicitud", nullable = false)
    private String idSolicitud;

    @Column(name = "id_organizacion_solicitante",nullable = false)
    private Integer idOrganizacionSolicitante;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "activa")
    private Boolean activa = true;

    @Column(name = "es_externa", nullable = false)
    private Boolean esExterna = true;

    @OneToMany(mappedBy = "solicitudDonacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SolicitudDonacionItem> items;

}
