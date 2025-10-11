package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import com.ong.kafka_producer.repository.evento_solidario.EventoSolidarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SolicitudEventoSolidarioExternalService {
    @Autowired
    private EventoSolidarioRepository eventoSolidarioRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    public void procesarSolicitudEventos(String mensaje) {
        try {
            log.info("Procesando solicitud externa de evento solidario...");
            EventoSolidarioDto eventoSolidarioDto = objectMapper.readValue(mensaje, EventoSolidarioDto.class);

            if (checkearSiEsSolicitudDePropiaOrganizacion(eventoSolidarioDto, idOrganizacion)) return;
            if (checkearSiElEventoSolidarioEstaVigente(eventoSolidarioDto)) return;

            EventoSolidario eventoSolidario = EventoSolidario.builder()
                    .idEvento(eventoSolidarioDto.getIdEvento())
                    .idOrganizacion(eventoSolidarioDto.getIdOrganizacion().intValue())
                    .nombre(eventoSolidarioDto.getNombre())
                    .descripcion(eventoSolidarioDto.getDescripcion())
                    .fechaEvento(eventoSolidarioDto.getFechaEvento())
                    .vigente(true)
                    .build();

            eventoSolidarioRepository.save(eventoSolidario);

            log.info("solicitud externa guardada: {}", eventoSolidarioDto.getIdEvento());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private static boolean checkearSiEsSolicitudDePropiaOrganizacion(EventoSolidarioDto eventoSolidarioDto, Integer idOrganizacion) {
        if (eventoSolidarioDto.getIdOrganizacion().equals(idOrganizacion.toString())) {
            log.info("Ignorando solicitud propia de organización: {}", idOrganizacion);
            return true;
        }
        return false;
    }

    private boolean checkearSiElEventoSolidarioEstaVigente(EventoSolidarioDto solicitudDto) {
        if (eventoSolidarioRepository.findByIdEvento(solicitudDto.getIdEvento()).isPresent()) {
            log.info("solicitud con id {} ya existe", solicitudDto.getIdEvento());
            return true;
        }
        EventoSolidario eventoSolidario = eventoSolidarioRepository.findByIdEvento(solicitudDto.getIdEvento()).orElse(null);
        if (eventoSolidario != null && !eventoSolidario.getVigente()) {
            log.info("El evento solidario con id {} no está vigente", solicitudDto.getIdEvento());
            return true;
        }
        return false;
    }
}
