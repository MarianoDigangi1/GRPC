package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.service.producer.evento_solidario.SolicitudEventoSolidarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/adhesion-eventos")
@RequiredArgsConstructor
public class AdhesionController {

    @Autowired
    private SolicitudEventoSolidarioService service;

    @PostMapping
    public ResponseEntity<?> adherirVoluntario(@RequestBody AdhesionEventoDto adhesion) {
        ResponseDto<String> response = service.publicarAdhesion(adhesion);

        if (response.isOk()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.internalServerError().body(response.getMessage());
        }
    }
}
