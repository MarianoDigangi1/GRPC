package com.ong.kafka_producer.repository.evento_solidario;

import com.ong.kafka_producer.entity.evento_solidario.AdhesionEventoExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdhesionEventoRepository extends JpaRepository<AdhesionEventoExterno, Integer> {

}
