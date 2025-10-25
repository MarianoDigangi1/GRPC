package com.sistemas.distribuidos.grpc_gateway.service.kafka;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.CrearEventoRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EventosRestService {
    private final RestTemplate restTemplate;

    @Value("${kafka.producer.server.baseUrl}")
    private String baseUrl;

    @Value("${kafka.producer.server.eventos.publicar}")
    private String publicarEventoUrl;

    @Value("${kafka.producer.server.eventos.baja}")
    private String bajaEventoUrl;

    @Value("${kafka.producer.server.idOrganizacion}")
    private String idOrganizacion;

    @Autowired
    public EventosRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void publicarEvento(CrearEventoRequestDto crearEventoRequestDto) throws Exception {
        String kafkaProducerUrl = baseUrl + publicarEventoUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("idOrganizacion", idOrganizacion);
        payload.put("nombre", crearEventoRequestDto.getNombre());
        payload.put("descripcion", crearEventoRequestDto.getDescripcion());
        payload.put("fechaEvento", crearEventoRequestDto.getFechaEventoIso());


        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, payload, String.class);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al publicar el evento: " + response.getBody());
        }

    }

    public void darBajaEvento(int idEvento) {
        String kafkaProducerUrl = baseUrl + bajaEventoUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("idEvento", idEvento);
        payload.put("idOrganizacion", idOrganizacion);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.exchange(kafkaProducerUrl, HttpMethod.DELETE, request, String.class);
    }
}
