package com.ong.rest.controller;

import com.ong.rest.entity.FiltroEvento;
import com.ong.rest.entity.Usuario;
import com.ong.rest.dto.FiltroEventoConsultaDto;
import com.ong.rest.dto.EventoDto;
import com.ong.rest.repository.UsuarioRepository;
import com.ong.rest.service.FiltroEventoService;
import com.ong.rest.service.EventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ong.rest.dto.EventoDto;
import com.ong.rest.service.EventoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/filtros-eventos")
@CrossOrigin(origins = "*")
public class FiltroEventoController {

    private final FiltroEventoService filtroEventoService;
    private final UsuarioRepository usuarioRepository;
    private final EventoService eventoService;

    public FiltroEventoController(FiltroEventoService filtroEventoService, UsuarioRepository usuarioRepository,
                                  EventoService eventoService) {
        this.filtroEventoService = filtroEventoService;
        this.usuarioRepository = usuarioRepository;
        this.eventoService = eventoService;
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<FiltroEvento> crearFiltro(
            @PathVariable Integer usuarioId,
            @RequestBody FiltroEvento filtroEvento) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));

        FiltroEvento nuevo = filtroEventoService.crearFiltro(filtroEvento, usuario);
        return ResponseEntity.status(201).body(nuevo);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FiltroEvento>> obtenerFiltrosPorUsuario(@PathVariable Integer usuarioId) {
        List<FiltroEvento> filtros = filtroEventoService.obtenerFiltrosPorUsuario(usuarioId);
        return ResponseEntity.ok(filtros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarFiltro(
            @PathVariable Integer id,
            @RequestBody FiltroEvento filtroActualizado) {

        try {
            FiltroEvento actualizado = filtroEventoService.actualizarFiltro(id, filtroActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFiltro(@PathVariable Integer id) {
        try {
            filtroEventoService.eliminarFiltro(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FiltroEvento> obtenerFiltroPorId(@PathVariable Integer id) {
        try {
            FiltroEvento filtro = filtroEventoService.obtenerFiltroPorId(id);
            return ResponseEntity.ok(filtro);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // =========================
    // FILTRADO DE EVENTOS
    // =========================
    @GetMapping("/eventos/filtrar")
    public String filtrarEventos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false, defaultValue = "todos") String estado,
            Model model) {

        List<EventoDto> eventosFiltrados = filtroEventoService.filtrarEventos(fechaDesde, fechaHasta, estado);
        model.addAttribute("eventosPropios", eventosFiltrados); // según tu vista
        model.addAttribute("eventosExternos", eventosFiltrados); // ejemplo: si querés separar propios y externos
        return "eventos/index"; // nombre de tu vista Thymeleaf
    }

}





