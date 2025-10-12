package com.ong.kafka_producer.service.consumer.oferta_donacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ong.kafka_producer.dto.oferta_donaciones.OfertaDonacionesDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExterna;
import com.ong.kafka_producer.entity.oferta_donacion.OfertaDonacionExternaItem;
import com.ong.kafka_producer.repository.oferta_donacion.OfertaDonacionRepository;
import com.ong.kafka_producer.repository.oferta_donacion.OfertaDonacionRepositoryItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OfertaDonacionExternaService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OfertaDonacionRepository repository;

    @Autowired
    private OfertaDonacionRepositoryItem repositoryItem;

    public void crearOfertaDonacionExterna(String mensaje) {
        try {
            OfertaDonacionesDto ofertaDonacionesDto = objectMapper.readValue(mensaje, OfertaDonacionesDto.class);
            OfertaDonacionExterna ofertaDonacionExterna = OfertaDonacionExterna.builder()
                    .idOferta(ofertaDonacionesDto.getIdOferta())
                    .idOrganizacionDonante(Integer.valueOf(ofertaDonacionesDto.getIdOrganizacionDonante()))
                    .build();
            List<OfertaDonacionExternaItem> ofertaItems = ofertaDonacionesDto.getDonaciones().stream()
                    .map(itemDto -> OfertaDonacionExternaItem.builder()
                            .idOferta(ofertaDonacionesDto.getIdOferta())
                            .categoria(itemDto.getCategoria())
                            .descripcion(itemDto.getDescripcion())
                            .cantidad(itemDto.getCantidad())
                            .build())
                    .collect(Collectors.toList());


            repository.save(ofertaDonacionExterna);
            repositoryItem.saveAll(ofertaItems);
        } catch (Exception e) {
            log.error("error al procesar solicitud externa: {}", e.getMessage(), e);
        }
    }
}
