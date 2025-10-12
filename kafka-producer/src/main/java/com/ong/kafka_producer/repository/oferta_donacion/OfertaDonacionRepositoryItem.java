package com.ong.kafka_producer.repository.oferta_donacion;

import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExternaItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfertaDonacionRepositoryItem extends JpaRepository<OfertaDonacionExternaItem, Integer> {
}
