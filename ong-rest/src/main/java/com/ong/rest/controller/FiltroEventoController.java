package com.ong.rest.controller;

import com.ong.rest.entity.FiltroEvento;
import com.ong.rest.entity.Usuario;
import com.ong.rest.dto.FiltroEventoConsultaDto;
import com.ong.rest.dto.EventoDto;
import com.ong.rest.repository.UsuarioRepository;
import com.ong.rest.service.FiltroEventoService;
import com.ong.rest.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "Filtros",
        description = "Operaciones disponibles para gestionar los filtros"
)
@RestController
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

    @Operation(summary = "Crear filtro por usuario", description = "Creacion de filtro de acuerdo a las caracteristicas elegidas por el usuario")
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<FiltroEvento> crearFiltro(
            @PathVariable Integer usuarioId,
            @RequestBody FiltroEvento filtroEvento) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));

        FiltroEvento nuevo = filtroEventoService.crearFiltro(filtroEvento, usuario);
        return ResponseEntity.status(201).body(nuevo);
    }

    @Operation(summary = "Obtener filtro por usuario", description = "Obtener filtro de acuerdo al id del usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<FiltroEvento>> obtenerFiltrosPorUsuario(@PathVariable Integer usuarioId) {
        List<FiltroEvento> filtros = filtroEventoService.obtenerFiltrosPorUsuario(usuarioId);
        return ResponseEntity.ok(filtros);
    }

    @Operation(summary = "Actualizar filtro", description = "Actualiza un filtro de acuerdo al id, y los datos ingresados")
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

    @Operation(summary = "Eliminar filtro", description = "Elimina un filtro determinado de acuerdo al id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFiltro(@PathVariable Integer id) {
        try {
            filtroEventoService.eliminarFiltro(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @Operation(summary = "Obtener filtro por id", description = "Retorna un filtro de acuerdo al id ingresado")
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
    @Operation(summary = "Filtrar eventos", description = "Filtra eventos entre fechas o de acuerdo al estado")
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





