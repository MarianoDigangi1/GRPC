package com.ong.kafka_producer.service.consumer.baja_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.baja_donacion.BajaDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudExterna;
import com.ong.kafka_producer.repository.solicitud_donacion.SolicitudDonacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BajaDonacionExternaService {

    @Autowired
    private SolicitudDonacionRepository solicitudDonacionRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    public void darDeBajaSolicitudDonacionExterna(String mensaje) {
        try {
            BajaDonacionDto bajaDonacionDto = objectMapper.readValue(mensaje, BajaDonacionDto.class);

            SolicitudExterna solicitud = checkearSiExisteEnBDD(bajaDonacionDto);

            if (solicitud == null) {
                log.warn("no existe la solicitud con id: {}", bajaDonacionDto.getIdSolicitud());
                return;
            }

            solicitud.setVigente(false);
            solicitudDonacionRepository.save(solicitud);

            log.info("se dio de baja la solicitud externa con id: {}", bajaDonacionDto.getIdSolicitud());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private SolicitudExterna checkearSiExisteEnBDD(BajaDonacionDto bajaDonacionDto) {
        SolicitudExterna solicitud = solicitudDonacionRepository
                .findBySolicitudIdAndExternalOrgId(bajaDonacionDto.getIdSolicitud(), bajaDonacionDto.getIdOrganizacionSolicitante())
                .orElse(null);
        return solicitud;
    }

}
