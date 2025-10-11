package com.ong.kafka_producer.service.producer.solicitud_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.baja_donacion.BajaDonacionDto;
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
import java.util.Optional;
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

    @Value("${spring.kafka.topic.solicitud.donaciones.baja}")
    private String bajaDonacionesTopic;

    @Transactional
    public ResponseDto<String> crearSolicitudDonacion(SolicitudDonacionDto solicitudDto) {
        try {
            String idSolicitud = UUID.randomUUID().toString();

            solicitudDto.setIdSolicitud(idSolicitud);
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

    public ResponseDto<String> bajaSolicitudDonacion(BajaDonacionDto bajaDonacionDto) {
        try {
            Optional<SolicitudDonacion> solicitudOpt = repository.findByIdSolicitud(bajaDonacionDto.getIdSolicitud());

            if (solicitudOpt.isEmpty()) {
                log.warn("solicitud con id {} no existe en la bdd", bajaDonacionDto.getIdSolicitud());
                return new ResponseDto<String>("", false, "solicitud no encontrada");
            }

            SolicitudDonacion solicitud = solicitudOpt.get();

            if (!solicitud.getIdOrganizacionSolicitante().equals(bajaDonacionDto.getIdOrganizacionSolicitante())) {
                log.warn("intenta dar de baja solicitud de otra organizacion con id {}", solicitud.getIdOrganizacionSolicitante());
                return new ResponseDto<String>("", false, "no tenes permisos para dar de baja esta solicitud");
            }

            if (!solicitud.getActiva()) {
                log.info("solicitud con id {} ya esta dada de baja en BDD", bajaDonacionDto.getIdSolicitud());
                return new ResponseDto<String>("", false, "la solicitud ya esta dada de baja");
            }

            solicitud.setActiva(false);
            repository.save(solicitud);

            String mensajeJson = objectMapper.writeValueAsString(bajaDonacionDto);
            kafkaTemplate.send(bajaDonacionesTopic, mensajeJson);

            log.info("baja de solicitud con id: {} publicada exitosamente", bajaDonacionDto.getIdSolicitud());
        } catch (Exception e) {
            log.error("Error al dar de baja solicitud: {}", e.getMessage(), e);
            return new ResponseDto<String>("", true, "error al dar de baja la solicitud");
        }

        return new ResponseDto<String>("", true, "la solicitud se dio de baja correctamente");

    }
}
