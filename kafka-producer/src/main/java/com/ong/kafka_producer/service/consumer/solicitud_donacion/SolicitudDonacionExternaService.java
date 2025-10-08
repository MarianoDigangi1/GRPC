package com.ong.kafka_producer.service.consumer.solicitud_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacion;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacionItem;
import com.ong.kafka_producer.repository.solicitud_donacion.SolicitudDonacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SolicitudDonacionExternaService {

    @Autowired
    private SolicitudDonacionRepository solicitudDonacionRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    public void procesarSolicitudExterna(String mensaje) {
        try {

            SolicitudDonacionDto solicitudDto = objectMapper.readValue(mensaje, SolicitudDonacionDto.class);

            //TODO: capaz habria que hacer un control de lo que llega. ejemplo si es un tipo permitido.

            if (checkearSiEsSolicitudDePropiaOrganizacion(solicitudDto, idOrganizacion)) return;
            if (checkearSiSolicitudExisteEnBDD(solicitudDto)) return;

            SolicitudDonacion solicitudExterna = SolicitudDonacion.builder()
                    .idSolicitud(solicitudDto.getIdSolicitud())
                    .idOrganizacionSolicitante(solicitudDto.getIdOrganizacionSolicitante())
                    .activa(true)
                    .esExterna(true)
                    .fechaSolicitud(LocalDateTime.now())
                    .build();
            List<SolicitudDonacionItem> items = solicitudDto.getDonaciones().stream()
                    .map(donacion -> SolicitudDonacionItem.builder()
                            .categoria(donacion.getCategoria())
                            .descripcion(donacion.getDescripcion())
                            .solicitudDonacion(solicitudExterna)
                            .build())
                    .collect(Collectors.toList());
            solicitudExterna.setItems(items);

            solicitudDonacionRepository.save(solicitudExterna);

            log.info("solicitud externa guardada: {}", solicitudDto.getIdSolicitud());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private boolean checkearSiSolicitudExisteEnBDD(SolicitudDonacionDto solicitudDto) {
        if (solicitudDonacionRepository.findByIdSolicitud(solicitudDto.getIdSolicitud()).isPresent()) {
            log.info("solicitud con id {} ya existe", solicitudDto.getIdSolicitud());
            return true;
        }
        return false;
    }

    private static boolean checkearSiEsSolicitudDePropiaOrganizacion(SolicitudDonacionDto solicitudDto, Integer idOrganizacion) {
        if (solicitudDto.getIdOrganizacionSolicitante().equals(idOrganizacion.toString())) {
            log.info("Ignorando solicitud propia de organización: {}", idOrganizacion);
            return true;
        }
        return false;
    }
}
