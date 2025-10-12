package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.oferta_donaciones.OfertaDonacionesDto;
import com.ong.kafka_producer.service.producer.oferta_donacion.OfertaDonacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oferta-donaciones")
@RequiredArgsConstructor
public class OfertaDonacionesController {

    private final OfertaDonacionService ofertaDonacionService;

    @PostMapping
    public ResponseEntity<String> ofertaDonacion(@RequestBody OfertaDonacionesDto solicitud) {
        ResponseDto<String> response = ofertaDonacionService.crearOfertaDonacion(solicitud);

        if(response.isOk()){
            return ResponseEntity.ok().body(response.getMessage());
        } else {
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }
}
