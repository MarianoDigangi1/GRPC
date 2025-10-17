package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    Optional<Evento> findByIdAndOrigenOrganizacionId(Integer id, String origenOrganizacionId);
}
