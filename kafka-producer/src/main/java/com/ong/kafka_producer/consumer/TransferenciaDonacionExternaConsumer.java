package com.ong.kafka_producer.consumer;



import com.ong.kafka_producer.service.consumer.transferencia_donacion.TransferenciaDonacionExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferenciaDonacionExternaConsumer {
   private final TransferenciaDonacionExternaService transferenciaDonacionExternaService;

    @KafkaListener(topics = "${spring.kafka.topic.transferencia.donaciones}-${spring.kafka.idOrganizacion}", groupId = "ong-group")
    public void consumirTransferenciaDonacion(String mensaje) {
        log.info("Mensaje recibido en topic transferencia-donaciones: {}", mensaje);
        transferenciaDonacionExternaService.procesarTransferenciaExterna(mensaje);
    }
}