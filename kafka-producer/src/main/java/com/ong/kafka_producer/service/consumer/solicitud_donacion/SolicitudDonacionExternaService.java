package com.ong.kafka_producer.service.consumer.solicitud_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudExterna;
import com.ong.kafka_producer.repository.solicitud_donacion.SolicitudDonacionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SolicitudDonacionExternaService {

    @Autowired
    private SolicitudDonacionRepository solicitudDonacionRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion;

    @Transactional
    public void procesarSolicitudExterna(String mensaje) {
        try {
            SolicitudDonacionDto solicitudDto = objectMapper.readValue(mensaje, SolicitudDonacionDto.class);

            if (checkearSiSolicitudExisteEnBDD(solicitudDto)) return;

            String contenidoJson = objectMapper.writeValueAsString(solicitudDto.getContenido());

            SolicitudExterna solicitudExterna = SolicitudExterna.builder()
                    .externalOrgId(solicitudDto.getIdOrganizacionSolicitante())
                    .solicitudId(solicitudDto.getIdSolicitud())
                    .vigente(true)
                    .recibidaEn(LocalDateTime.now())
                    .contenido(contenidoJson)
                    .build();

            solicitudDonacionRepository.save(solicitudExterna);

            log.info("solicitud externa guardada: {}", solicitudDto.getIdSolicitud());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private boolean checkearSiSolicitudExisteEnBDD(SolicitudDonacionDto solicitudDto) {
        if (solicitudDonacionRepository.findBySolicitudIdAndExternalOrgId(solicitudDto.getIdSolicitud(), solicitudDto.getIdOrganizacionSolicitante()).isPresent()) {
            log.info("solicitud con id {} ya existe", solicitudDto.getIdSolicitud());
            return true;
        }
        return false;
    }
}
