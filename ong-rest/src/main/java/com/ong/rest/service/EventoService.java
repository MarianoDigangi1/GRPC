package com.ong.rest.service;

import com.ong.rest.dto.EventoDto;
import com.ong.rest.entity.Evento;
import com.ong.rest.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    // Traer todos los eventos como DTOs
    public List<EventoDto> obtenerTodosLosEventos() {
        return eventoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    // Conversión Entity -> DTO
    private EventoDto convertirADto(Evento e) {
        return EventoDto.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .descripcion(e.getDescripcion())
                .fechaEventoIso(e.getFechaEvento())  // <- mapear a fecha_evento
                .publicado(e.getPublicado())         // <- mapear a publicado
                .eventoIdOrganizacionExterna(e.getEventoIdOrganizacionExterna())
                .build();
    }

    // Filtrar eventos por fecha y estado (vigentes/no vigentes/todos)
    public List<EventoDto> filtrarEventos(LocalDate fechaDesde, LocalDate fechaHasta, String estado) {
        return obtenerTodosLosEventos().stream()
                .filter(e -> {
                    boolean okFecha = true;
                    if (fechaDesde != null) okFecha = !e.getFechaEventoIso().toLocalDate().isBefore(fechaDesde);
                    if (okFecha && fechaHasta != null) okFecha = !e.getFechaEventoIso().toLocalDate().isAfter(fechaHasta);

                    boolean okEstado = true;
                    if (!"todos".equalsIgnoreCase(estado)) {
                        boolean vigente = e.getFechaEventoIso() != null && e.getFechaEventoIso().isAfter(LocalDateTime.now());
                        if ("vigentes".equalsIgnoreCase(estado)) okEstado = vigente;
                        else if ("no vigentes".equalsIgnoreCase(estado)) okEstado = !vigente;
                    }

                    return okFecha && okEstado;
                })
                .collect(Collectors.toList());
    }
}

