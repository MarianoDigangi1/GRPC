package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.solicitud_donacion.SolicitudDonacionDto;
import com.ong.kafka_producer.service.producer.solicitud_donacion.SolicitudDonacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solicitudes-donacion")
@RequiredArgsConstructor
public class SolicitudDonacionController {

    private final SolicitudDonacionService service;

    @PostMapping
    public ResponseEntity<String> crearSolicitud(@RequestBody SolicitudDonacionDto solicitud) {
        ResponseDto<String> response = service.crearSolicitudDonacion(solicitud);

        if(response.isOk()){
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
