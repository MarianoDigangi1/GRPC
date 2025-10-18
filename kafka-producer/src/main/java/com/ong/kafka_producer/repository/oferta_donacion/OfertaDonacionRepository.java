package com.ong.kafka_producer.repository.oferta_donacion;

import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExterna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfertaDonacionRepository extends JpaRepository<OfertaDonacionExterna, Integer> {
    Optional<OfertaDonacionExterna> findByOfertaIdAndExternalOrgId(String ofertaId, String externalOrgId);
}
