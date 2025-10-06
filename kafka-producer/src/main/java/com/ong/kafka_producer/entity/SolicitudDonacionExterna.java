package com.ong.kafka_producer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solicitud_donacion_externa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDonacionExterna {
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

    @OneToMany(mappedBy = "solicitudDonacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SolicitudDonacionExternaItem> items;

}
