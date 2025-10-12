package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.evento_solidario.AdherirEventoExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdherirEventoConsumer {

    private final AdherirEventoExternalService adherirEventoService;

    @KafkaListener(topics = "${spring.kafka.topic.adhesion.eventos}", groupId = "ong-group")
    public void consumirAdherirEvento(String mensaje) {
        log.info("Mensaje recibido en topic baja-evento-solidario: {}", mensaje);
        adherirEventoService.procesarAdherirUsuario(mensaje);
    }
}
