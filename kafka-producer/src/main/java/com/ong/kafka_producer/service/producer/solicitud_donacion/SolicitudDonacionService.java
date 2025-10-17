package com.ong.kafka_producer.service.producer.solicitud_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.baja_donacion.BajaDonacionDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudExterna;
import com.ong.kafka_producer.repository.solicitud_donacion.SolicitudDonacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudDonacionService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final SolicitudDonacionRepository repository;

    @Value("${spring.kafka.topic.solicitud.donaciones}")
    private String solicitudDonacionesTopic;

    @Value("${spring.kafka.topic.solicitud.donaciones.baja}")
    private String bajaDonacionesTopic;

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion;

    @Transactional
    public ResponseDto<String> crearSolicitudDonacion(SolicitudDonacionDto solicitudDto) {
        try {
            String idSolicitud = UUID.randomUUID().toString();
            solicitudDto.setIdSolicitud(idSolicitud);
            solicitudDto.setIdOrganizacionSolicitante(idOrganizacion);

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
            Optional<SolicitudExterna> solicitudOpt = repository.findBySolicitudIdAndExternalOrgId(bajaDonacionDto.getIdSolicitud(), idOrganizacion);

            if (solicitudOpt.isEmpty()) {
                log.warn("solicitud con id {} no existe en la bdd", bajaDonacionDto.getIdSolicitud());
                return new ResponseDto<String>("", false, "solicitud no encontrada");
            }

            SolicitudExterna solicitud = solicitudOpt.get();

            if (!solicitud.getVigente()) {
                log.info("solicitud con id {} ya esta dada de baja en BDD", bajaDonacionDto.getIdSolicitud());
                return new ResponseDto<String>("", false, "la solicitud ya esta dada de baja");
            }

            bajaDonacionDto.setIdOrganizacionSolicitante(idOrganizacion);

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
