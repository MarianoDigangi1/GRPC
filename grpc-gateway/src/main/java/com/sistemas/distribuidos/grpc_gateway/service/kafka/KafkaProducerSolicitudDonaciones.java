package com.sistemas.distribuidos.grpc_gateway.service.kafka;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaProducerSolicitudDonaciones {

    private final RestTemplate restTemplate;

    @Value("${kafka.producer.server.baseUrl}")
    private String baseUrl;

    @Value("${kafka.producer.server.donaciones.solicitar}")
    private String donacionesUrl;

    @Value("${kafka.producer.server.donaciones.baja}")
    private String bajaUrl;

    @Value("${kafka.producer.server.idOrganizacion}")
    private String idOrganizacion;

    @Autowired
    public KafkaProducerSolicitudDonaciones(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void solicitarDonaciones(SolicitudDonacionDto solicitudDonacion) throws Exception {
        String kafkaProducerUrl = baseUrl + donacionesUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("contenido", solicitudDonacion.getContenido());

        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, payload, String.class);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al enviar la solicitud: " + response.getBody());
        }

    }

    public void darBajaSolicitudDonacion(String idSolicitud) throws Exception {
        String kafkaProducerUrl = baseUrl + bajaUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("idSolicitud", idSolicitud);
        payload.put("idOrganizacionSolicitante", idOrganizacion);

        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, payload, String.class);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al enviar la solicitud: " + response.getBody());
        }
    }
}
