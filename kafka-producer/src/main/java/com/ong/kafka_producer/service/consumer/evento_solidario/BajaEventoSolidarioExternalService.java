package com.ong.kafka_producer.service.consumer.evento_solidario;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import com.ong.kafka_producer.entity.evento_solidario.Evento;
import com.ong.kafka_producer.repository.evento_solidario.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BajaEventoSolidarioExternalService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.idOrganizacion}")
    private String idOrganizacion; // debe ser String (ej: "ong-2")

    public void procesarBajaExterna(String mensaje) {
        try {
                    /*
            // Parsear el mensaje recibido
            BajaEventoSolidarioDto dto = objectMapper.readValue(mensaje, BajaEventoSolidarioDto.class);
            log.info("📥 Procesando baja de evento externo: {}", dto);

            // Ignorar si la baja es propia
            if (dto.getIdOrganizacion().equals(idOrganizacion)) {
                log.info("🟡 Ignorando baja propia de la organización {}", idOrganizacion);
                return;
            }

            // Buscar evento por id y organización origen
            Optional<Evento> eventoOpt = eventoRepository.findByIdAndOrigenOrganizacionId(
                    dto.getIdEvento(),
                    dto.getIdOrganizacion()
            );

            if (eventoOpt.isPresent()) {
                Evento evento = eventoOpt.get();
                evento.setVigente(false);
                eventoRepository.save(evento);
                log.info("✅ Evento externo dado de baja correctamente: id={}, org={}",
                        dto.getIdEvento(), dto.getIdOrganizacion());
                                
                     */
            } else {
                log.warn("⚠️ Evento externo no encontrado con id={} y organizacion={}",
                        dto.getIdEvento(), dto.getIdOrganizacion());
            }

        } catch (Exception e) {
            log.error("❌ Error al procesar baja de evento externo: {}", e.getMessage(), e);
        }
    }
}
