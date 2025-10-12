package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.entity.evento_solidario.EventoVoluntario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoVoluntarioRepository extends JpaRepository<EventoVoluntario, Integer> {
}
