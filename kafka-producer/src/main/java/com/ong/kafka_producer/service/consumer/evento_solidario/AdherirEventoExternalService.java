package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import com.ong.kafka_producer.entity.evento_solidario.EventoVoluntario;
import com.ong.kafka_producer.repository.evento_solidario.EventoRepository;
import com.ong.kafka_producer.repository.evento_solidario.EventoSolidarioRepository;
import com.ong.kafka_producer.repository.evento_solidario.EventoVoluntarioRepository;
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
    private EventoVoluntarioRepository eventoVoluntarioRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

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

            EventoVoluntario eventoVoluntario = EventoVoluntario.builder()
                    .idEvento(eventoSolidarioDto.getIdEvento())
                    .idOrganizacionVoluntario(eventoSolidarioDto.getVoluntario().getIdOrganizacion())
                    .idVoluntario(eventoSolidarioDto.getVoluntario().getIdVoluntario())
                    .nombre(eventoSolidarioDto.getVoluntario().getNombre())
                    .apellido(eventoSolidarioDto.getVoluntario().getApellido())
                    .telefono(eventoSolidarioDto.getVoluntario().getTelefono())
                    .email(eventoSolidarioDto.getVoluntario().getEmail())
                    .fechaAdhesion(LocalDateTime.now())
                    .build();

            eventoVoluntarioRepository.save(eventoVoluntario);
            /*
            *
            *  TransferenciaDonacion transferenciaExterna = TransferenciaDonacion.builder()
                    .idTransferencia(transferenciaDto.getIdTransferencia())
                    .idOrganizacionOrigen(transferenciaDto.getIdOrganizacionOrigen())
                    .idOrganizacionDestino(transferenciaDto.getIdOrganizacionDestino())
                    .fechaTransferencia(LocalDateTime.now())
                    .build();*/

            //boolean vigente = eventoSolidarioDto.getFechaEvento().isAfter(LocalDateTime.now()); // ajusta según tu DTO

            log.info("solicitud externa guardada: {}", eventoSolidarioDto.getIdEvento());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }
}

