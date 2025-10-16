package com.ong.kafka_producer.service.producer.oferta_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.oferta_donaciones.OfertaDonacionesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfertaDonacionService {

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.oferta.donaciones}")
    private String ofertaDonacionesTopic;


    public ResponseDto<String> crearOfertaDonacion(OfertaDonacionesDto ofertaDonacionesDto) {
        try {
            String mensaje = objectMapper.writeValueAsString(ofertaDonacionesDto);
            kafkaTemplate.send(ofertaDonacionesTopic, mensaje);
            log.info("Baja de evento publicada en Kafka: {}", mensaje);

            return new ResponseDto<String>("", true, "Evento dado de baja y publicado correctamente.");
        } catch (Exception e) {
            log.error("Error al publicar baja de evento: {}", e.getMessage(), e);
            return new ResponseDto<String>("", false, "Error al publicar baja de evento.");
        }
    }
}
