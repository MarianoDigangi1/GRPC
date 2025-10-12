package com.ong.kafka_producer.repository.oferta_donacion;

import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExterna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfertaDonacionRepository extends JpaRepository<OfertaDonacionExterna, Integer> {
}
