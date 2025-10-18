package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.oferta_donacion.OfertaDonacionExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfertaDonacionesConsumer {
    private final OfertaDonacionExternaService ofertaDonacionExternaService;

    @KafkaListener(topics = "${spring.kafka.topic.oferta.donaciones}", groupId = "ong-group")
    public void consumirSolicitudDonacion(String mensaje) {
        log.info("Mensaje recibido en topic oferta-donaciones: {}", mensaje);
        ofertaDonacionExternaService.crearOfertaDonacionExterna(mensaje);
    }
}
