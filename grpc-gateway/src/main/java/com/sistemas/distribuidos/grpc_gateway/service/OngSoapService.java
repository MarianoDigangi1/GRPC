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
import org.springframework.web.client.HttpServerErrorException;
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
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            System.err.println("Error del servicio SOAP (Presidentes): " + e.getResponseBodyAsString());
            return Collections.emptyList(); // No rompe la app
        } catch (RestClientException e) {
            throw new RuntimeException("No se pudo conectar con el servicio SOAP.");
        }
    }

    public List<OngDTO> getAsociaciones(List<String> ids) {
        String url = UriComponentsBuilder.fromHttpUrl(soapServiceBaseUrl)
                .path("/asociaciones")
                .queryParam("ids", String.join(",", ids))
                .toUriString();

        try {
            ResponseEntity<List<OngDTO>> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            System.err.println("Error del servicio SOAP (Asociaciones): " + e.getResponseBodyAsString());
            return Collections.emptyList();
        } catch (RestClientException e) {
            throw new RuntimeException("No se pudo conectar con el servicio SOAP.");
        }
    }

    public String buildErrorMessage(List<PresidenteDTO> presidentes, List<OngDTO> asociaciones) {
        if (presidentes.isEmpty() && asociaciones.isEmpty()) {
            return "No se encontraron presidentes ni asociaciones para los IDs ingresados.";
        }
        if (presidentes.isEmpty()) {
            return "No se encontraron presidentes para los IDs ingresados.";
        }
        if (asociaciones.isEmpty()) {
            return "No se encontraron asociaciones para los IDs ingresados.";
        }
        return null; // todo OK
    }
}
