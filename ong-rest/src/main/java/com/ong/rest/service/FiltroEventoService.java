package com.ong.rest.service;

import com.ong.rest.dto.EventoDto;
import com.ong.rest.entity.Evento;
import com.ong.rest.entity.FiltroEvento;
import com.ong.rest.entity.Usuario;
import com.ong.rest.repository.EventoRepository;
import com.ong.rest.repository.FiltroEventoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FiltroEventoService {

    private final FiltroEventoRepository filtroEventoRepository;
    private final EventoRepository eventoRepository;

    public FiltroEventoService(FiltroEventoRepository filtroEventoRepository,
                               EventoRepository eventoRepository) {
        this.filtroEventoRepository = filtroEventoRepository;
        this.eventoRepository = eventoRepository;
    }

    // -----------------------------
    // CRUD filtros
    // -----------------------------
    public FiltroEvento crearFiltro(FiltroEvento filtro, Usuario usuario) {
        filtro.setUsuario(usuario);
        return filtroEventoRepository.save(filtro);
    }

    public List<FiltroEvento> obtenerFiltrosPorUsuario(Integer usuarioId) {
        return filtroEventoRepository.findByUsuarioId(usuarioId);
    }

    public FiltroEvento obtenerFiltroPorId(Integer id) {
        return filtroEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filtro no encontrado con id: " + id));
    }

    public FiltroEvento actualizarFiltro(Integer id, FiltroEvento filtroActualizado) {
        FiltroEvento existente = filtroEventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filtro no encontrado con id: " + id));

        existente.setNombre(filtroActualizado.getNombre());
        existente.setParametros(filtroActualizado.getParametros());
        return filtroEventoRepository.save(existente);
    }

    public void eliminarFiltro(Integer id) {
        if (!filtroEventoRepository.existsById(id)) {
            throw new EntityNotFoundException("Filtro no encontrado con id: " + id);
        }
        filtroEventoRepository.deleteById(id);
    }

    public boolean existeFiltroPorNombreYUsuario(String nombre, Integer usuarioId) {
        return filtroEventoRepository.existsByNombreAndUsuarioId(nombre, usuarioId);
    }

    // -----------------------------
    // FILTRAR EVENTOS
    // -----------------------------
    public List<EventoDto> filtrarEventos(LocalDate fechaDesde, LocalDate fechaHasta, String estado) {
        List<Evento> todosEventos = eventoRepository.findAll(); // Traer todos los eventos desde DB

        return todosEventos.stream()
                .map(this::convertirADto)
                .filter(e -> filtrarPorFechaYEstado(e, fechaDesde, fechaHasta, estado))
                .collect(Collectors.toList());
    }

    // -----------------------------
    // Helper: convertir Evento a EventoDto
    // -----------------------------
    private EventoDto convertirADto(Evento evento) {
        return EventoDto.builder()
                .id(evento.getId())
                .nombre(evento.getNombre())
                .descripcion(evento.getDescripcion())
                .fechaEventoIso(evento.getFechaEvento())
                .publicado(evento.getPublicado())
                .eventoIdOrganizacionExterna(evento.getEventoIdOrganizacionExterna())
                .build();
    }

    // -----------------------------
    // Helper: filtrar por fecha y estado
    // -----------------------------
    private boolean filtrarPorFechaYEstado(EventoDto e, LocalDate fechaDesde, LocalDate fechaHasta, String estado) {
        boolean okFecha = true;
        LocalDateTime fechaEvento = e.getFechaEventoIso();
        if (fechaDesde != null) okFecha = !fechaEvento.toLocalDate().isBefore(fechaDesde);
        if (okFecha && fechaHasta != null) okFecha = !fechaEvento.toLocalDate().isAfter(fechaHasta);

        boolean okEstado = true;
        if (!"todos".equalsIgnoreCase(estado)) {
            boolean vigente = e.getFechaEventoIso() != null && e.getFechaEventoIso().isAfter(LocalDateTime.now());
            if ("vigentes".equalsIgnoreCase(estado)) okEstado = vigente;
            else if ("no vigentes".equalsIgnoreCase(estado)) okEstado = !vigente;
        }

        return okFecha && okEstado;
    }
}








