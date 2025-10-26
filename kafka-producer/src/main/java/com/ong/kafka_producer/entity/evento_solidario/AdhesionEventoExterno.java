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
@Table(name = "adhesion_evento_externo")
public class AdhesionEventoExterno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "evento_id", nullable = false)
    private Integer eventoId;

    @Column(name = "organizacion_evento_id", length = 100, nullable = false)
    private String organizacionEventoId;

    @Column(name = "organizacion_participante_id", length = 100, nullable = false)
    private String organizacionParticipanteId;

    @Column(name = "id_voluntario_externo", length = 100, nullable = false)
    private String idVoluntarioExterno;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "fecha_adhesion", nullable = false)
    private LocalDateTime fechaAdhesion;
}
