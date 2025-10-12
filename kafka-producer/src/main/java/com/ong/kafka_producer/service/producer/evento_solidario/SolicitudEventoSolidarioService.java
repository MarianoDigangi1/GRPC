package com.ong.kafka_producer.service.producer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import com.ong.kafka_producer.repository.evento_solidario.EventoSolidarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudEventoSolidarioService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventoSolidarioRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.publicar.eventos}")
    private String solicitudEventosTopic;

    @Transactional
    public ResponseDto<String> crearSolicitudEvento(EventoSolidarioDto solicitudDto) {
        try {

            String mensaje = objectMapper.writeValueAsString(solicitudDto);
            kafkaTemplate.send(solicitudEventosTopic, mensaje);

            log.info("Solicitud de donación publicada: {}", solicitudDto.getIdEvento());
            return new ResponseDto<String>("", true, "Solicitud de crear evento: " + solicitudDto.getIdEvento());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }
}

