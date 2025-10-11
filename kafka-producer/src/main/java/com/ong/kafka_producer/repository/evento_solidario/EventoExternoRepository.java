package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.EventoExterno;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface EventoExternoRepository extends JpaRepository<EventoExterno, Long> {
    Optional<EventoExterno> findByIdEvento(String idEvento);
    Optional<EventoExterno> findByIdEventoAndIdOrganizacion(String idEvento, Long idOrganizacion);
    List<EventoExterno> findAllByOrderByFechaEventoAsc();
}