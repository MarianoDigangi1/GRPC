package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.service.EventosService;
import com.sistemas.distribuidos.grpc_gateway.dto.evento.CrearEventoRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.evento.EventoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.List;

@Controller
public class EventosViewController {

    private final EventosService eventosService;

    private static final DateTimeFormatter HTML_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter HTML_DT_SEC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    @Autowired
    public EventosViewController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @GetMapping("/eventos")
    public String vistaEventos(Model model) {
        // llamar al servicio gRPC para traer eventos
        List<EventoDto> eventos = eventosService.listarEventos();
        model.addAttribute("eventos", eventos);
        return "eventos/eventos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEvento() {
        return "eventos/nuevo_evento"; // templates/eventos/nuevo_evento.html
    }

    @PostMapping("/crear")
    public String crearEvento(@RequestParam String nombre,
                              @RequestParam String descripcion,
                              @RequestParam String fechaEventoIso) {

        // Parse del valor del <input type="datetime-local">
        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT);
        } catch (Exception e) {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT_SEC);
        }

        CrearEventoRequestDto dto = new CrearEventoRequestDto();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setFechaEventoIso(fecha);  // ahora LocalDateTime
        dto.setActorUsuarioId(1);      // temporal para probar
        dto.setActorRol("Presidente"); // temporal para probar

        System.out.println("➡ Controller -> DTO listo: " + dto);

        eventosService.crearEvento(dto);
        return "redirect:/eventos"; // después de crear, vuelve a la lista
    
    }
}
