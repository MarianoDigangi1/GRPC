package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    Optional<Evento> findById(Integer id);
}
