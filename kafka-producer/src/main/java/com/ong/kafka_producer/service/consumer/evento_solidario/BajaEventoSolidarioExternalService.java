package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.EventoSolidario;
import com.ong.kafka_producer.repository.evento_solidario.EventoSolidarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BajaEventoSolidarioExternalService {

    @Autowired
    private EventoSolidarioRepository eventoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private Integer idOrganizacion;

    public void procesarBajaExterna(String mensaje) {
        try {
            // Parsear el mensaje recibido
            BajaEventoSolidarioDto eventoSolidarioDto = objectMapper.readValue(mensaje, BajaEventoSolidarioDto.class);
            log.info("Procesando baja de evento: {}", eventoSolidarioDto);

            // Ignorar si la baja es propia
            if (eventoSolidarioDto.getIdOrganizacion().equals(idOrganizacion)) {
                log.info("Ignorando baja propia de la organización {}", idOrganizacion);
                return;
            }

            // Buscar el evento externo por id_evento
            Optional<EventoSolidario> eventoOpt = eventoRepository.findByIdEventoAndIdOrganizacion(eventoSolidarioDto.getIdEvento(), eventoSolidarioDto.getIdOrganizacion());

            if (eventoOpt.isPresent()) {
                EventoSolidario evento = eventoOpt.get();
                evento.setVigente(false);
                eventoRepository.save(evento);
                log.info("✅ Evento externo dado de baja correctamente: {}", eventoSolidarioDto.getIdEvento());
            } else {
                log.warn("⚠️ Evento externo no encontrado con id_evento = {}", eventoSolidarioDto.getIdEvento());
            }

        } catch (Exception e) {
            log.error("❌ Error al procesar baja de evento externo", e);
        }
    }
}

/*
*
*  if (checkearSiEsSolicitudDePropiaOrganizacion(eventoSolidarioDto, idOrganizacion)) return;
            boolean vigente = eventoSolidarioDto.getFechaEvento().isAfter(LocalDateTime.now()); // ajusta según tu DTO

            if (!vigente) {
                log.info("Evento externo descartado por no estar vigente: {}", eventoSolidarioDto.getIdEvento());
                return;
            }

            EventoSolidario eventoSolidario = EventoSolidario.builder()
                    .idEvento(String.valueOf(eventoSolidarioDto.getIdEvento()))
                    .idOrganizacion(eventoSolidarioDto.getIdOrganizacion().intValue())
                    .nombre(eventoSolidarioDto.getNombre())
                    .descripcion(eventoSolidarioDto.getDescripcion())
                    .fechaEvento(eventoSolidarioDto.getFechaEvento())
                    .vigente(vigente)
                    .build();

            eventoSolidarioRepository.save(eventoSolidario);

            log.info("solicitud externa guardada: {}", eventoSolidarioDto.getIdEvento());*/