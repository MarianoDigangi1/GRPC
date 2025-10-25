package com.sistemas.distribuidos.grpc_gateway.service.kafka;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.transferencia.TransferenciaDonacionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransferenciaDonacionRestService {

    private final RestTemplate restTemplate;

    @Value("${kafka.producer.server.baseUrl}")
    private String baseUrl;

    public TransferenciaDonacionRestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void transferirDonacion(TransferenciaDonacionDto transferencia) throws Exception {
        String kafkaProducerUrl = baseUrl + "/api/transferencias-donacion";

        ResponseEntity<String> response = restTemplate.postForEntity(kafkaProducerUrl, transferencia, String.class);
        
        if (!response.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            throw new RuntimeException("Error al realizar la transferencia: " + response.getBody());
        }
    }
}
