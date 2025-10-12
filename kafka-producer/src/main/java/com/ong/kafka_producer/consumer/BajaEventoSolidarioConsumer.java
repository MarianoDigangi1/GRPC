package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.evento_solidario.BajaEventoSolidarioExternalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BajaEventoSolidarioConsumer {

    private final BajaEventoSolidarioExternalService bajaEventoService;

    @KafkaListener(topics = "${spring.kafka.topic.baja.eventos}", groupId = "ong-group")
    public void consumirBajaEvento(String mensaje) {
        log.info("Mensaje recibido en topic baja-evento-solidario: {}", mensaje);
        bajaEventoService.procesarBajaExterna(mensaje);
    }
}
