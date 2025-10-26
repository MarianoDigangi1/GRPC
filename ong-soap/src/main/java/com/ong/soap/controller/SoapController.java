package com.ong.soap.controller;

import com.ong.soap.client.SoapClient;
import com.ong.soap.wsdl.OrganizationTypeArray;
import com.ong.soap.wsdl.PresidentTypeArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/soap")
public class SoapController {

    private final SoapClient soapClient;

    @Autowired
    public SoapController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/presidentes")
    public ResponseEntity<?> listarPresidentes(@RequestParam(value = "ids") List<String> ids) {
        try {
            PresidentTypeArray respuesta = soapClient.listarPresidentes(ids);
            List<Map<String, Object>> presidentes = respuesta.getPresidentType().stream()
                    .map(p -> Map.<String, Object>of(
                            "id", p.getId().getValue(),
                            "name", p.getName().getValue(),
                            "address", p.getAddress().getValue(),
                            "phone", p.getPhone().getValue(),
                            "organizationId", p.getOrganizationId().getValue()
                    ))
                    .toList();
            return ResponseEntity.ok(presidentes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al obtener presidentes",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/asociaciones")
    public ResponseEntity<?> listarAsociaciones(@RequestParam(value = "ids") List<String> ids) {
        try {
            OrganizationTypeArray respuesta = soapClient.listarAsociaciones(ids);
            List<Map<String, Object>> asociaciones = respuesta.getOrganizationType().stream()
                    .map(o -> Map.<String, Object>of(
                            "id", o.getId().getValue(),
                            "name", o.getName().getValue(),
                            "address", o.getAddress().getValue(),
                            "phone", o.getPhone().getValue()
                    ))
                    .toList();
            return ResponseEntity.ok(asociaciones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Error al obtener asociaciones",
                    "message", e.getMessage()
            ));
        }
    }
}
