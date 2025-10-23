package com.sistemas.distribuidos.grpc_gateway.service.kafka;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.oferta.OfertaDonacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OfertaDonacionesRestService {
    private final RestTemplate restTemplate;

    @Value("${kafka.producer.server.baseUrl}")
    private String baseUrl;

    @Value("${kafka.producer.server.donaciones.oferta}")
    private String ofertaDonacionesUrl;

    @Autowired
    public OfertaDonacionesRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void ofrecerDonaciones(OfertaDonacionDto ofertaDonacionDto) throws Exception {
        String kafkaProducerUrl = baseUrl + ofertaDonacionesUrl;

        Map<String, Object> payload = new HashMap<>();
        payload.put("donaciones", ofertaDonacionDto.getDonaciones());

        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, payload, String.class);
        if(!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al enviar la oferta: " + response.getBody());
        }

    }

}
