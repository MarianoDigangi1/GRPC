package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.EventoExternoDto;
import com.ong.kafka_producer.entity.evento_solidario.EventoExterno;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacion;
import com.ong.kafka_producer.entity.solicitud_donacion.SolicitudDonacionItem;
import com.ong.kafka_producer.repository.evento_solidario.EventoExternoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SolicitudEventoSolidarioService {
    @Autowired
    private EventoExternoRepository eventoSolidarioRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    public void procesarSolicitudEventos(String mensaje) {
        try {
            EventoExternoDto eventoSolidarioDto = objectMapper.readValue(mensaje, EventoExternoDto.class);

            if (checkearSiEsSolicitudDePropiaOrganizacion(eventoSolidarioDto, idOrganizacion)) return;
            if (checkearSiElEventoSolidarioEstaVigente(eventoSolidarioDto)) return;

            EventoExterno eventoExterno = EventoExterno.builder()
                    .idEvento(eventoSolidarioDto.getIdEvento())
                    .idOrganizacion(eventoSolidarioDto.getIdOrganizacion().intValue())
                    .nombre(eventoSolidarioDto.getNombre())
                    .descripcion(eventoSolidarioDto.getDescripcion())
                    .fechaEvento(eventoSolidarioDto.getFechaEvento())
                    .vigente(true)
                    .build();

            eventoSolidarioRepository.save(eventoExterno);

            log.info("solicitud externa guardada: {}", eventoSolidarioDto.getIdEvento());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }

    private static boolean checkearSiEsSolicitudDePropiaOrganizacion(EventoExternoDto eventoSolidarioDto, Integer idOrganizacion) {
        if (eventoSolidarioDto.getIdOrganizacion().equals(idOrganizacion.toString())) {
            log.info("Ignorando solicitud propia de organización: {}", idOrganizacion);
            return true;
        }
        return false;
    }

    private boolean checkearSiElEventoSolidarioEstaVigente(EventoExternoDto solicitudDto) {
        if (eventoSolidarioRepository.findByIdEvento(solicitudDto.getIdEvento()).isPresent()) {
            log.info("solicitud con id {} ya existe", solicitudDto.getIdEvento());
            return true;
        }
        EventoExterno eventoSolidario = eventoSolidarioRepository.findByIdEvento(solicitudDto.getIdEvento()).orElse(null);
        if (eventoSolidario != null && !eventoSolidario.getVigente()) {
            log.info("El evento solidario con id {} no está vigente", solicitudDto.getIdEvento());
            return true;
        }
        return false;
    }
}
