package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.repository.evento_solidario.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class SolicitudEventoSolidarioExternalService {
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion;

    public void procesarSolicitudEventos(String mensaje) {
        try {


            EventoSolidarioDto eventoSolidarioDto = objectMapper.readValue(mensaje, EventoSolidarioDto.class);
            
            boolean vigente = eventoSolidarioDto.getFechaEvento().isAfter(LocalDateTime.now()); // ajusta según tu DTO

            if (!vigente) {
                log.info("Evento externo descartado por no estar vigente: {}", eventoSolidarioDto.getIdEvento());
                return;
            }
            Optional<Evento> eventoPrueba = eventoRepository.findByOrigenOrganizacionIdAndEventoIdOrganizacionExterna(eventoSolidarioDto.getIdOrganizacion(), String.valueOf(eventoSolidarioDto.getIdEvento()));

            if(eventoPrueba.isPresent()){
                log.info("Evento externo ya existente: {}", eventoSolidarioDto.getIdEvento());
                return;
            }

            Evento eventoSolidario = Evento.builder()
                    .nombre(eventoSolidarioDto.getNombre())
                    .descripcion(eventoSolidarioDto.getDescripcion())
                    .fechaEvento(eventoSolidarioDto.getFechaEvento())
                    .origenOrganizacionId(eventoSolidarioDto.getIdOrganizacion())
                    .publicado(true)
                    .vigente(true)
                    .build();

            if(!eventoSolidarioDto.getIdOrganizacion().equals(idOrganizacion)){
                eventoSolidario.setEventoIdOrganizacionExterna(String.valueOf(eventoSolidarioDto.getIdEvento()));
            }

            eventoRepository.save(eventoSolidario);

            log.info("solicitud externa guardada: {}", eventoSolidarioDto.getIdEvento());

        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }
}
