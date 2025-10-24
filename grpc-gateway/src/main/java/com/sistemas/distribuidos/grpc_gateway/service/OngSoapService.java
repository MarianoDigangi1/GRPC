package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.OngSoapDTO;
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.OngDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OngSoapService {

    private final RestTemplate restTemplate;
    private final String soapServiceBaseUrl;

    @Autowired
    public OngSoapService(RestTemplate restTemplate,
                          @Value("${soap.service.url:http://localhost:8080/soap}") String soapServiceBaseUrl) {
        this.restTemplate = restTemplate;
        this.soapServiceBaseUrl = soapServiceBaseUrl;
    }


    public List<PresidenteDTO> getPresidentes(List<String> ids) {
        

        String url = UriComponentsBuilder.fromHttpUrl(soapServiceBaseUrl)
                .path("/presidentes")
                .queryParam("ids", String.join(",", ids))
                .toUriString();

        try {

            ResponseEntity<PresidenteSoapResponse> response = 
                restTemplate.getForEntity(url, PresidenteSoapResponse.class);

            PresidenteSoapResponse soapResponse = response.getBody();


            if (soapResponse == null || soapResponse.getPresidentType() == null) {
                return Collections.emptyList();
            }

            return soapResponse.getPresidentType().stream()
                    .map(this::convertPresidente)
                    .collect(Collectors.toList());

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

            ResponseEntity<OngSoapResponse> response = 
                restTemplate.getForEntity(url, OngSoapResponse.class);

            OngSoapResponse soapResponse = response.getBody();


            if (soapResponse == null || soapResponse.getOrganizationType() == null) {
                return Collections.emptyList();
            }

            return soapResponse.getOrganizationType().stream()
                    .map(this::convertAsociacion)
                    .collect(Collectors.toList());

        } catch (HttpClientErrorException e) {
            System.err.println("Error del microservicio SOAP: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error al consultar asociaciones: " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            System.err.println("Error de conexión con ong-soap: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio SOAP: " + e.getMessage(), e);
        }
    }



    private PresidenteDTO convertPresidente(PresidenteSoapDTO soapDto) {
        return PresidenteDTO.builder()
                .id(soapDto.getId().getValue())
                .name(soapDto.getName().getValue())
                .address(soapDto.getAddress().getValue())
                .phone(soapDto.getPhone().getValue())
                .organizationId(soapDto.getOrganizationId().getValue())
                .build();
    }


    private OngDTO convertAsociacion(OngSoapDTO soapDto) {
        return OngDTO.builder()
                .id(soapDto.getId().getValue())
                .name(soapDto.getName().getValue())
                .address(soapDto.getAddress().getValue())
                .phone(soapDto.getPhone().getValue())
                .build();
    }
}

