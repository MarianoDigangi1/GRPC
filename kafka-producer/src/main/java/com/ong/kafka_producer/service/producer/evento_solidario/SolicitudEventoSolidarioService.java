package com.ong.kafka_producer.service.producer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.AdhesionEventoExterno;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.repository.evento_solidario.AdhesionEventoRepository;
import com.ong.kafka_producer.repository.evento_solidario.EventoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudEventoSolidarioService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final EventoRepository eventoRepository;
    private final AdhesionEventoRepository adhesionEventoRepository;

    @Value("${spring.kafka.topic.publicar.eventos}")
    private String solicitudEventosTopic;
    @Value("${spring.kafka.topic.baja.eventos}")
    private String bajaEventoTopic;
    @Value("${spring.kafka.topic.adhesion.eventos}")
    private String adhesionEventoTopic;
    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion;

    @Transactional
    public ResponseDto<String> crearSolicitudEvento(EventoSolidarioDto solicitudDto) {
        try {
            solicitudDto.setIdOrganizacion(idOrganizacion);

            Evento evento = eventoRepository.save(Evento.builder().nombre(solicitudDto.getNombre()).descripcion(solicitudDto.getDescripcion())
                    .fechaEvento(solicitudDto.getFechaEvento())
                            .origenOrganizacionId(idOrganizacion)
                            .vigente(true)
                            .publicado(true)
                    .build());

            solicitudDto.setIdEvento(evento.getId()); // Esto es temporal
            String mensaje = objectMapper.writeValueAsString(solicitudDto);
            kafkaTemplate.send(solicitudEventosTopic, mensaje);

            log.info("Solicitud de donación publicada: {}", solicitudDto.getIdEvento());
            return new ResponseDto<String>("", true, "Solicitud de crear evento: " + solicitudDto.getIdEvento());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }


    @Transactional
    public ResponseDto<String> darDeBajaEvento(BajaEventoSolidarioDto bajaEventoDto) {
        try {
            Optional<Evento> eventoOpt = eventoRepository.findById(bajaEventoDto.getIdEvento());

            if (eventoOpt.isPresent()) {
                Evento evento = eventoOpt.get();
                evento.setPublicado(false);
                eventoRepository.save(evento);
                log.info("Evento externo dado de baja correctamente: id={}, org={}",
                        bajaEventoDto.getIdEvento(), bajaEventoDto.getIdOrganizacion());


            } else {
                log.warn("Evento externo no encontrado con id={} y organizacion={}",
                        bajaEventoDto.getIdEvento(), bajaEventoDto.getIdOrganizacion());
            }

            String mensaje = objectMapper.writeValueAsString(bajaEventoDto);
            kafkaTemplate.send(bajaEventoTopic, mensaje);

            log.info("Solicitud de baja publicada: {}", bajaEventoDto.getIdEvento());
            return new ResponseDto<String>("", true, "Solicitud de baja de evento: " + bajaEventoDto.getIdEvento());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }

    @Transactional
    public ResponseDto<String> publicarAdhesion(AdhesionEventoDto adhesionEventoDto) {
        try {
            // Chequear si el evento existe
            // y tambien si el id de evento_id_organizacion_externa coincide con el id de organizacion son los mismo

            Optional<Evento> evento = eventoRepository.findByOrigenOrganizacionIdAndEventoIdOrganizacionExterna(adhesionEventoDto.getIdOrganizacionEvento(), adhesionEventoDto.getIdEvento().toString());

            if (evento.isEmpty()) {
                log.error("El evento con id {} no existe", adhesionEventoDto.getIdEvento());
                return new ResponseDto<>("", false, "El evento no existe");
            }

            // Fix para evitar NullPointerException si vigente es null
            //Evento ev = evento.orElseThrow();
            if (!Boolean.TRUE.equals(evento.get().getVigente())) {
                log.error("El evento con id {} no está activo o el campo 'vigente' es null", adhesionEventoDto.getIdEvento());
                return new ResponseDto<>("", false, "El evento no esta vigente");
            }
            AdhesionEventoExterno adhesion = AdhesionEventoExterno.builder()
                    .eventoId(evento.get().getId())
                    .organizacionEventoId(adhesionEventoDto.getIdOrganizacionEvento())
                    .organizacionParticipanteId(idOrganizacion)
                    .idVoluntarioExterno(adhesionEventoDto.getVoluntario().getIdVoluntario())
                    .nombre(adhesionEventoDto.getVoluntario().getNombre())
                    .apellido(adhesionEventoDto.getVoluntario().getApellido())
                    .telefono(adhesionEventoDto.getVoluntario().getTelefono())
                    .email(adhesionEventoDto.getVoluntario().getEmail())
                    .fechaAdhesion(java.time.LocalDateTime.now())
                    .build();

            // Guardar la adhesión en la base de datos
            // Suponiendo que existe un repository para AdhesionEventoExterno
            adhesionEventoRepository.save(adhesion);

            String topic = adhesionEventoTopic + "-" + adhesionEventoDto.getIdOrganizacionEvento();
            String mensaje = objectMapper.writeValueAsString(adhesionEventoDto);
            kafkaTemplate.send(topic, mensaje);

            log.info("Solicitud de donación publicada: {}", adhesionEventoDto.getIdEvento());
            return new ResponseDto<String>("", true, "Solicitud de crear evento: " + adhesionEventoDto.getIdEvento());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }

}

