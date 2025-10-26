package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.entity.evento_solidario.AdhesionEventoExterno;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.repository.evento_solidario.AdhesionEventoRepository;
import com.ong.kafka_producer.repository.evento_solidario.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AdherirEventoExternalService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private AdhesionEventoRepository adhesionEventoRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public void procesarAdherirUsuario(String mensaje) {
        try {
            AdhesionEventoDto eventoSolidarioDto = objectMapper.readValue(mensaje, AdhesionEventoDto.class);

            Optional<Evento> evento = eventoRepository.findById(Integer.valueOf(eventoSolidarioDto.getIdEvento()));

            if (evento.isEmpty()) {
                log.error("El evento con id {} no existe", eventoSolidarioDto.getIdEvento());
                return;
            }

            // Fix para evitar NullPointerException si vigente es null
            Evento ev = evento.orElseThrow();
            if (!Boolean.TRUE.equals(ev.getVigente())) {
                log.error("El evento con id {} no está activo o el campo 'vigente' es null", eventoSolidarioDto.getIdEvento());
                return;
            }
            AdhesionEventoExterno adhesion = AdhesionEventoExterno.builder()
                    .eventoId(eventoSolidarioDto.getIdEvento())
                    .organizacionEventoId(evento.get().getOrigenOrganizacionId())
                    .organizacionParticipanteId(eventoSolidarioDto.getVoluntario().getIdOrganizacion())
                    .idVoluntarioExterno(eventoSolidarioDto.getVoluntario().getIdVoluntario())
                    .nombre(eventoSolidarioDto.getVoluntario().getNombre())
                    .apellido(eventoSolidarioDto.getVoluntario().getApellido())
                    .telefono(eventoSolidarioDto.getVoluntario().getTelefono())
                    .email(eventoSolidarioDto.getVoluntario().getEmail())
                    .fechaAdhesion(LocalDateTime.now())
                    .build();

            adhesionEventoRepository.save(adhesion);
        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }
}

