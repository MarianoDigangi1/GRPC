package com.ong.rest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(name = "origen_organizacion_id", length = 100)
    private String origenOrganizacionId;

    @Column(nullable = false)
    private Boolean vigente = true;

    @Column(name = "evento_id_organizacion_externa", length = 100)
    private String eventoIdOrganizacionExterna;

    @Column(nullable = false)
    private Boolean publicado = false;

}

