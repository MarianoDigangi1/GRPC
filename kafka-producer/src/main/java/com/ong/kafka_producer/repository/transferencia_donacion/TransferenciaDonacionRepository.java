package com.ong.kafka_producer.repository.transferencia_donacion;

import com.ong.kafka_producer.entity.transferencia_donacion.TransferenciaDonacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferenciaDonacionRepository extends JpaRepository<TransferenciaDonacion, String> {
    Optional<TransferenciaDonacion> findByIdTransferencia(String idTransferencia);
}
