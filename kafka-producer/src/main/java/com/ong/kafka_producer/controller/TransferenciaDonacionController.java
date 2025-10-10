package com.ong.kafka_producer.controller;

import com.ong.kafka_producer.dto.ResponseDto;
import com.ong.kafka_producer.dto.transferencia_donacion.TransferenciaDonacionDto;
import com.ong.kafka_producer.service.producer.transferencia_donacion.TransferenciaDonacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias-donacion")
@RequiredArgsConstructor
public class TransferenciaDonacionController {

    private final TransferenciaDonacionService service;

    @PostMapping
    public ResponseEntity<String> crearTransferencia(@RequestBody TransferenciaDonacionDto transferencia) {
        ResponseDto<String> response = service.transferirDonacion(transferencia);

        if (response.isOk()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            // Devolvemos 500 si el error viene del servidor (no 400, ya que es inesperado)
            return ResponseEntity.internalServerError().body(response.getMessage());
        }
    }
}





