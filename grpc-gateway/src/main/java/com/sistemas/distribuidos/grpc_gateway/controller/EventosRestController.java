package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.EventosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
public class EventosRestController {

    private final EventosService eventosService;

    @Autowired
    public EventosRestController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearEvento(@RequestBody CrearEventoRequestDto request) {
        CrearEventoResponseDto response = eventosService.crearEvento(request);

        if (response.getMensaje() != null && response.getMensaje().equals("Evento creado correctamente")) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> modificarEvento(@RequestBody ModificarEventoRequestDto request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal user = (CustomUserPrincipal) auth.getPrincipal();
        int userId = user.getId();

        ModificarEventoResponseDto response = eventosService.modificarEvento(request);

        if (response.getMensaje() != null && response.getMensaje().equals("Evento modificado correctamente")) {
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> darBajaEvento(@RequestBody BajaEventoRequestDto request) {
        String response = eventosService.darBajaEvento(request);

        if (response != null && response.equals("Evento eliminado correctamente.")) {
            return ResponseEntity.status(204).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/asignar-eliminar")
    public ResponseEntity<?> asignarOEliminar(@RequestBody AsignarQuitarRequestDto requestDto) {
        String response = eventosService.asignarOQuitarUsuario(requestDto);

        if (response != null && response.equals("Operación realizada correctamente.")) {
            return ResponseEntity.status(204).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

}
