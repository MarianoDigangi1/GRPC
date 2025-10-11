package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.baja_donacion.BajaDonacionExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BajaSolicitudDonacionConsumer {

    private final BajaDonacionExternaService bajaDonacionExternaService;

    @KafkaListener(topics = "${spring.kafka.topic.solicitud.donaciones.baja}", groupId = "ong-group")
    public void bajaSolicitudDonacionExterna(String mensaje) {
        log.info("Mensaje recibido en topic baja-solicitud-donaciones: {}", mensaje);
        bajaDonacionExternaService.darDeBajaSolicitudDonacionExterna(mensaje);
    }
}
