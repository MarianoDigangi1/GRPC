package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.service.producer.evento_solidario.EventoSolidarioService;
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
    public ResponseEntity<?> adherirVoluntario(@RequestBody AdhesionEventoDto adhesion, @RequestParam Integer idOrganizador) {
        service.publicarAdhesion(adhesion, idOrganizador);
        return ResponseEntity.ok("Adhesión enviada.");
    }
}
