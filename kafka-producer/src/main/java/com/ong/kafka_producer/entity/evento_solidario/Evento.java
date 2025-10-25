package com.ong.kafka_producer.entity.evento_solidario;

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
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(name = "origen_organizacion_id", length = 100)
    private String origenOrganizacionId;

    @Column(name = "evento_id_organizacion_externa", length = 100)
    private String eventoIdOrganizacionExterna;

    @Column(name = "vigente")
    private Boolean vigente;

    @Column(name = "publicado")
    private Boolean publicado;
}
