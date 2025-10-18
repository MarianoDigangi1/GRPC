package com.ong.kafka_producer.service.producer.oferta_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.oferta_donaciones.OfertaDonacionesDto;
import com.ong.kafka_producer.repository.oferta_donacion.OfertaDonacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfertaDonacionService {

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion;

    @Value("${spring.kafka.topic.oferta.donaciones}")
    private String ofertaDonacionesTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OfertaDonacionRepository repository;

    public ResponseDto<String> crearOfertaDonacion(OfertaDonacionesDto ofertaDonacionesDto) {
        try {
            String idOferta = UUID.randomUUID().toString();
            ofertaDonacionesDto.setIdOferta(idOferta);
            ofertaDonacionesDto.setIdOrganizacionDonante(idOrganizacion);

            String mensaje = objectMapper.writeValueAsString(ofertaDonacionesDto);
            kafkaTemplate.send(ofertaDonacionesTopic, mensaje);
            log.info("Oferta publicada con exito en Kafka: {}", mensaje);

            return new ResponseDto<String>("", true, "Oferta de donaciones publicada correctamente.");
        } catch (Exception e) {
            log.error("Error al publicar la oferta de donaciones: {}", e.getMessage(), e);
            return new ResponseDto<String>("", false, "Error al publicar la oferta de donaciones.");
        }
    }
}
