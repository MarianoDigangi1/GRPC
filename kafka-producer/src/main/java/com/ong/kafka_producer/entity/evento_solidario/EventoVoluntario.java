package com.ong.kafka_producer.entity.evento_solidario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "evento_voluntario")
public class EventoVoluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_evento", length = 100, nullable = false)
    private String idEvento;

    @Column(name = "id_organizacion_voluntario", nullable = false)
    private Integer idOrganizacionVoluntario;

    @Column(name = "id_voluntario", nullable = false)
    private Integer idVoluntario;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "telefono", length = 50)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "fecha_adhesion")
    private LocalDateTime fechaAdhesion;
}