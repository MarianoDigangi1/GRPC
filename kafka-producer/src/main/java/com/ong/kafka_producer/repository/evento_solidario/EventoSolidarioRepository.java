package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventoSolidarioRepository extends JpaRepository<EventoSolidario, Long> {
    Optional<EventoSolidario> findByIdEvento(String idEvento);
    Optional<EventoSolidario> findByIdEventoAndIdOrganizacion(String idEvento, Integer idOrganizacion);
    List<EventoSolidario> findAllByOrderByFechaEventoAsc();
}