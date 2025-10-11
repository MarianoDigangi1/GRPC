package com.ong.kafka_producer.entity.evento_solidario;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud_evento_solidario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudEventoSolidario {
}
