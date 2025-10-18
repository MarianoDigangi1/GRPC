package com.ong.kafka_producer.service.consumer.oferta_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.oferta_donaciones.OfertaDonacionesDto;
import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExterna;
import com.ong.kafka_producer.repository.oferta_donacion.OfertaDonacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OfertaDonacionExternaService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfertaDonacionRepository repository;

    public void crearOfertaDonacionExterna(String mensaje) {
        try {
            OfertaDonacionesDto ofertaDonacionesDto = objectMapper.readValue(mensaje, OfertaDonacionesDto.class);

            if(checkearSiSolicitudExisteEnBDD(ofertaDonacionesDto)) return;

            String contenidoJson = objectMapper.writeValueAsString(ofertaDonacionesDto.getDonaciones());

            OfertaDonacionExterna ofertaDonacionExterna = OfertaDonacionExterna.builder()
                    .ofertaId(ofertaDonacionesDto.getIdOferta())
                    .externalOrgId(ofertaDonacionesDto.getIdOrganizacionDonante())
                    .contenido(contenidoJson)
                    .recibidaEn(LocalDateTime.now())
                    .build();

            repository.save(ofertaDonacionExterna);
        } catch (Exception e) {
            log.error("error al procesar la oferta externa: {}", e.getMessage(), e);
        }
    }

    private boolean checkearSiSolicitudExisteEnBDD(OfertaDonacionesDto ofertaDonacionesDto) {
        if (repository.findByOfertaIdAndExternalOrgId(ofertaDonacionesDto.getIdOferta(), ofertaDonacionesDto.getIdOrganizacionDonante()).isPresent()) {
            log.info("oferta con id {} ya existe", ofertaDonacionesDto.getIdOferta());
            return true;
        }
        return false;
    }
}
