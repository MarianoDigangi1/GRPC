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
        System.out.println("Controller: Recibida solicitud para /presidentes con IDs: " + ids);
        try {
            PresidentTypeArray respuesta = soapClient.listarPresidentes(ids);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            System.err.println("Controller: Error al llamar a listarPresidentes: " + e.getMessage());
            e.printStackTrace();

            // Devolver JSON con tipo application/json
            return ResponseEntity
                    .internalServerError()
                    .header("Content-Type", "application/json")
                    .body(Map.of(
                            "error", "Error al obtener presidentes",
                            "message", e.getMessage()
                    ));
        }
    }

    @GetMapping("/asociaciones")
    public ResponseEntity<?> listarAsociaciones(@RequestParam(value = "ids") List<String> ids) {
        System.out.println("Controller: Recibida solicitud para /asociaciones con IDs: " + ids);
        try {
            OrganizationTypeArray respuesta = soapClient.listarAsociaciones(ids);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            System.err.println("Controller: Error al llamar a listarAsociaciones: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .header("Content-Type", "application/json")
                    .body(Map.of(
                            "error", "Error al obtener asociaciones",
                            "message", e.getMessage()
                    ));
        }
    }

}


