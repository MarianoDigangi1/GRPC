package com.ong.kafka_producer.service.producer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoSolidarioService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.baja.eventos}")
    private String bajaEventosTopic;

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion; // ej: "ong-2"

    public ResponseDto<String> darDeBajaEvento(Integer idEvento) {
        try {
            BajaEventoSolidarioDto dto = new BajaEventoSolidarioDto();
            dto.setIdEvento(idEvento);
            dto.setIdOrganizacion(idOrganizacion);

            String mensaje = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(bajaEventosTopic, mensaje);

            log.info("📤 Publicada baja de evento en Kafka: {}", mensaje);
            return new ResponseDto<>("", true, "Evento dado de baja y publicado correctamente.");
        } catch (Exception e) {
            log.error("❌ Error al publicar baja de evento: {}", e.getMessage(), e);
            return new ResponseDto<>("", false, "Error al publicar baja de evento.");
        }
    }
}
