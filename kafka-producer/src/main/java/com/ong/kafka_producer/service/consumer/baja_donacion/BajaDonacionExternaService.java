package com.ong.kafka_producer.service.consumer.baja_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.baja_donacion.BajaDonacionDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacion;
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

            if (checkearSiEsSolicitudDePropiaOrganizacion(bajaDonacionDto, idOrganizacion)) return;

            SolicitudDonacion solicitud = checkearSiExisteEnBDD(bajaDonacionDto);

            if (solicitud == null) {
                log.warn("no existe la solicitud con id: {}", bajaDonacionDto.getIdSolicitud());
                return;
            }

            solicitud.setActiva(false);
            solicitudDonacionRepository.save(solicitud);

            log.info("se dio de baja la solicitud externa con id: {}", bajaDonacionDto.getIdSolicitud());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private SolicitudDonacion checkearSiExisteEnBDD(BajaDonacionDto bajaDonacionDto) {
        SolicitudDonacion solicitud = solicitudDonacionRepository
                .findByIdSolicitud(bajaDonacionDto.getIdSolicitud())
                .orElse(null);
        return solicitud;
    }

    private static boolean checkearSiEsSolicitudDePropiaOrganizacion(BajaDonacionDto bajaDonacionDto, Integer idOrganizacion) {
        if (bajaDonacionDto.getIdOrganizacionSolicitante().equals(idOrganizacion.toString())) {
            log.info("Ignorando baja de solicitud propia de organizacion de organización: {}", idOrganizacion);
            return true;
        }
        return false;
    }

    private boolean checkearSiSolicitudExisteEnBDD(BajaDonacionDto bajaDonacionDto) {
        if (solicitudDonacionRepository.findByIdSolicitud(bajaDonacionDto.getIdSolicitud()).isPresent()) {
            log.info("solicitud con id {} ya existe", bajaDonacionDto.getIdSolicitud());
            return true;
        }
        return false;
    }

}
