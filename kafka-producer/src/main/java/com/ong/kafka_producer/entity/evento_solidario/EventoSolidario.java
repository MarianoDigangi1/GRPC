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
@Table(name = "evento_externo", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_evento", "id_organizacion"})
})
public class EventoSolidario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "id_evento", length = 100, nullable = false)
    private String idEvento;

    @Column(name = "id_organizacion", nullable = false)
    private Integer idOrganizacion;

    @Column(name = "nombre", length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_evento")
    private LocalDateTime fechaEvento;

    @Column(name = "vigente")
    private Boolean vigente;
}
