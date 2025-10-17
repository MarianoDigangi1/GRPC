package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.solicitud_donacion.SolicitudDonacionExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudDonacionExternaConsumer {

    private final SolicitudDonacionExternaService solicitudDonacionExternaService;

    @KafkaListener(topics = "${spring.kafka.topic.solicitud.donaciones}", groupId = "ong-group")
    public void consumirSolicitudDonacion(String mensaje) {
        log.info("Mensaje recibido en topic solicitud-donaciones: {}", mensaje);
        solicitudDonacionExternaService.procesarSolicitudExterna(mensaje);
    }
}
