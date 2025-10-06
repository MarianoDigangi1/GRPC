package com.ong.kafka_producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.DonacionDto;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.SolicitudDonacionExterna;
import com.ong.kafka_producer.entity.SolicitudDonacionExternaItem;
import com.ong.kafka_producer.repository.SolicitudExternaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudDonacionService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SolicitudExternaRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.solicitud.donaciones}")
    private String solicitudDonacionesTopic;

    @Transactional
    public ResponseDto<String> crearSolicitudDonacion(SolicitudDonacionDto solicitudDto) {
        try {
            String idSolicitud = UUID.randomUUID().toString();

            solicitudDto.setDonaciones(solicitudDto.getDonaciones());

            // TODO: Esto quizas moverlo a un converte si hay tiempo
            SolicitudDonacionExterna solicitudEntity = new SolicitudDonacionExterna();
            solicitudEntity.setIdSolicitud(idSolicitud);
            solicitudEntity.setIdOrganizacionSolicitante(solicitudDto.getIdOrganizacionSolicitante());
            solicitudEntity.setFechaSolicitud(LocalDateTime.now());// TODO: esto creo que se puede hacer con un @PrePersist en la clase entity. Tema a ver.
            solicitudEntity.setActiva(true); // TODO: esto es lo mismo que lo de la fecha

            // crear items para donacion
            List<SolicitudDonacionExternaItem> items = solicitudDto.getDonaciones().stream()
                    .map(donacion -> {
                        SolicitudDonacionExternaItem item = new SolicitudDonacionExternaItem();
                        item.setCategoria(donacion.getCategoria());
                        item.setDescripcion(donacion.getDescripcion());
                        item.setSolicitudDonacion(solicitudEntity);
                        return item;
                    })
                    .collect(Collectors.toList());

            solicitudEntity.setItems(items);

            repository.save(solicitudEntity);

            String mensaje = objectMapper.writeValueAsString(solicitudDto);
            kafkaTemplate.send(solicitudDonacionesTopic, mensaje);

            log.info("Solicitud de donación publicada: {}", idSolicitud);
            return new ResponseDto<String>("", true, "Solicitud de donacion publicada: " + idSolicitud);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }
}
