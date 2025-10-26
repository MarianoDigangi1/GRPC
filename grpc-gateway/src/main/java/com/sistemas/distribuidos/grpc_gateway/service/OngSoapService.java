package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.OngDTO;
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.PresidenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class OngSoapService {

    private final RestTemplate restTemplate;
    private final String soapServiceBaseUrl;

    @Autowired
    public OngSoapService(RestTemplate restTemplate,
                          @Value("${soap.service.url:http://localhost:8081/soap}") String soapServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.soapServiceBaseUrl = soapServiceBaseUrl;
    }

    public List<PresidenteDTO> getPresidentes(List<String> ids) {
        String url = UriComponentsBuilder.fromHttpUrl(soapServiceBaseUrl)
                .path("/presidentes")
                .queryParam("ids", String.join(",", ids))
                .toUriString();
        try {
            ResponseEntity<List<PresidenteDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            System.err.println("Error del microservicio SOAP: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al consultar presidentes: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            System.err.println("Error de conexión con ong-soap: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio SOAP: " + e.getMessage(), e);
        }
    }

    public List<OngDTO> getAsociaciones(List<String> ids) {
        String url = UriComponentsBuilder.fromHttpUrl(soapServiceBaseUrl)
                .path("/asociaciones")
                .queryParam("ids", String.join(",", ids))
                .toUriString();
        try {
            ResponseEntity<List<OngDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            System.err.println("Error del microservicio SOAP: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al consultar asociaciones: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            System.err.println("Error de conexión con ong-soap: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio SOAP: " + e.getMessage(), e);
        }
    }
}
