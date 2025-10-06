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

    @Autowired
    public KafkaProducerSolicitudDonaciones(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public void solicitarDonaciones(SolicitudDonacionDto solicitudDonacion) throws Exception {
        String kafkaProducerUrl = baseUrl + donacionesUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("donaciones", solicitudDonacion.getDonaciones());
        payload.put("idOrganizacionSolicitante", 1); // TODO: obtener del usuario logueado

        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, payload, String.class);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al enviar la solicitud: " + response.getBody());
        }


    }
}
