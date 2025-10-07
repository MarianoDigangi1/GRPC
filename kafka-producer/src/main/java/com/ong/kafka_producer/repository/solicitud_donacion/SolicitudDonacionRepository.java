package com.ong.kafka_producer.repository.solicitud_donacion;

import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitudDonacionRepository extends JpaRepository<SolicitudDonacion, Integer> {
    Optional<SolicitudDonacion> findByIdSolicitud(String idSolicitud);
}
