package com.ong.kafka_producer.consumer;

import com.ong.kafka_producer.service.consumer.evento_solidario.SolicitudEventoSolidarioService;
import com.ong.kafka_producer.service.consumer.solicitud_donacion.SolicitudDonacionExternaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoSolidarioExternalConsumer {
    @Value("${spring.kafka.topic.publicar.eventos}")
    private String solicitudEvento;

    private final SolicitudEventoSolidarioService solicitudEventosService;

    @KafkaListener(topics = "${spring.kafka.topic.publicar.eventos}", groupId = "ong-group")
    public void consumirSolicitudEventos(String mensaje) {
        log.info("Mensaje recibido en topic solicitud-eventos: {}", mensaje);
        solicitudEventosService.procesarSolicitudEventos(mensaje);
    }
}
