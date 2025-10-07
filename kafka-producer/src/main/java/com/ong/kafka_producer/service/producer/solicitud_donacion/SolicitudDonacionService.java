package com.ong.kafka_producer.service.producer.solicitud_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.solicitud_donacion.ResponseDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacion;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacionItem;
import com.ong.kafka_producer.repository.solicitud_donacion.SolicitudDonacionRepository;
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
    private final SolicitudDonacionRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.solicitud.donaciones}")
    private String solicitudDonacionesTopic;

    @Transactional
    public ResponseDto<String> crearSolicitudDonacion(SolicitudDonacionDto solicitudDto) {
        try {
            String idSolicitud = UUID.randomUUID().toString();

            solicitudDto.setDonaciones(solicitudDto.getDonaciones());

            // TODO: Esto quizas moverlo a un converter si hay tiempo
            SolicitudDonacion solicitudEntity = SolicitudDonacion.builder().idSolicitud(idSolicitud)
                    .idOrganizacionSolicitante(solicitudDto.getIdOrganizacionSolicitante())
                    .fechaSolicitud(LocalDateTime.now())
                    .activa(true)
                    .esExterna(false).build();

            // crear items para donacion
            List<SolicitudDonacionItem> items = solicitudDto.getDonaciones().stream()
                    .map(donacion -> {
                        SolicitudDonacionItem item = new SolicitudDonacionItem();
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
