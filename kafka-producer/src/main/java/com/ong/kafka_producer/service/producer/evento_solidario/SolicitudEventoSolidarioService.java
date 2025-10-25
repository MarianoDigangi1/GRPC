package com.ong.kafka_producer.service.producer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
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
            Random aleatorio = new Random(System.currentTimeMillis());
            int intAletorio = aleatorio.nextInt();

            solicitudDto.setIdOrganizacion(idOrganizacion);
            solicitudDto.setIdEvento(intAletorio); // Esto es temporal
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
    public ResponseDto<String> publicarAdhesion(AdhesionEventoDto adhesionEventoDto, @RequestParam Integer idOrganizador) {
        try {

            String mensaje = objectMapper.writeValueAsString(adhesionEventoDto);
            kafkaTemplate.send(adhesionEventoTopic, mensaje);

            log.info("Solicitud de donación publicada: {}", adhesionEventoDto.getIdEvento());
            return new ResponseDto<String>("", true, "Solicitud de crear evento: " + adhesionEventoDto.getIdEvento());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseDto<>("", false, "Ocurrio un error inesperado");
        }
    }

}

