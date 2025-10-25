package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.evento_solidario.AdhesionEventoDto;
import com.ong.kafka_producer.dto.evento_solidario.BajaEventoSolidarioDto;
import com.ong.kafka_producer.dto.evento_solidario.EventoSolidarioDto;
import com.ong.kafka_producer.service.producer.evento_solidario.EventoSolidarioService;
import com.ong.kafka_producer.service.producer.evento_solidario.SolicitudEventoSolidarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evento-solidario")
@RequiredArgsConstructor
public class EventoSolidarioController {

    @Autowired
    private SolicitudEventoSolidarioService service;

    @Autowired
    private final EventoSolidarioService eventoSolidarioService;

    @PostMapping
    public ResponseEntity<String> crearSolicitud(@RequestBody EventoSolidarioDto solicitud) {

        ResponseDto<String> response = service.crearSolicitudEvento(solicitud);

        if(response.isOk()){
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    // Nueva baja de evento
    @DeleteMapping("/{idEvento}/baja")
    public ResponseEntity<String> darDeBajaEvento(@PathVariable String idEvento) {
        ResponseDto<String> response = eventoSolidarioService.darDeBajaEvento(Integer.valueOf(idEvento)); //FIXEADO PARA PRUEBAS, REVISAR

        if (response.isOk()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
    @DeleteMapping("/baja")
    public ResponseEntity<?> bajaEvento(@RequestBody BajaEventoSolidarioDto solicitud) {

        ResponseDto<String> response = service.darDeBajaEvento(solicitud);

        if (response.isOk()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

}
