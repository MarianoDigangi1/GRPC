package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioListResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.inventario.ModificarInventarioRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.EventosService;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import com.sistemas.distribuidos.grpc_gateway.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class EventosViewController {

    private final EventosService eventosService;
    private final UsuarioService usuarioService;
    private final InventarioService inventarioService;

    private static final DateTimeFormatter HTML_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter HTML_DT_SEC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    public EventosViewController(EventosService eventosService, UsuarioService usuarioService, InventarioService inventarioService) {

        this.eventosService = eventosService;
        this.usuarioService = usuarioService;
        this.inventarioService = inventarioService;
    }

    @GetMapping("/eventos")
    public String vistaEventos(Model model) {
        List<EventoDto> eventos = eventosService.listarEventos();

        List<UserResponseDto> usuarios = usuarioService.listarUsuarios().getUsuarios();

        Map<Integer, String> nombresPorId = usuarios.stream()
                .collect(Collectors.toMap(UserResponseDto::getId,
                        u -> u.getNombre() + " " + u.getApellido()));

        for (EventoDto ev : eventos) {
            List<Integer> ids = ev.getMiembrosIds();
            if (ids != null && !ids.isEmpty()) {
                List<String> nombres = ids.stream()
                        .map(nombresPorId::get)
                        .collect(Collectors.toList());
                ev.setMiembrosNombres(String.join(", ", nombres));
            } else {
                ev.setMiembrosNombres("-");
            }
        }

        model.addAttribute("eventos", eventos);
        return "eventos/eventos";

    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEvento(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "eventos/nuevo_evento";
    }

    @PostMapping("/crear")
public String crearEvento(@RequestParam String nombre,
                          @RequestParam String descripcion,
                          @RequestParam String fechaEventoIso,
                          RedirectAttributes redirectAttributes) {
    try {
        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT);
        } catch (Exception e) {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT_SEC);
        }

        CrearEventoRequestDto dto = new CrearEventoRequestDto();
        dto.setNombre(nombre);
        dto.setDescripcion(descripcion);
        dto.setFechaEventoIso(fecha);
        dto.setActorUsuarioId(1);
        dto.setActorRol("Presidente");

        eventosService.crearEvento(dto);

        redirectAttributes.addFlashAttribute("exitoMessage", "Evento creado correctamente");
        return "redirect:/eventos";

    } catch (IllegalArgumentException e) {
        // Error de negocio (ej: fecha pasada)
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/nuevo";

    } catch (GrpcConnectionException e) {
        // Error real de conexión
        redirectAttributes.addFlashAttribute("errorMessage", "Error técnico: " + e.getMessage());
        return "redirect:/nuevo";
    }
}



    @PostMapping("/eventos/editar/{id}")
    public String editarEvento(
            @PathVariable int id,
            @RequestParam (required = false) String nombre,
            @RequestParam (required = false) String fechaEventoIso,
            @RequestParam(required = false) List<Integer> agregarMiembrosIds,
            @RequestParam(required = false) List<Integer> quitarMiembrosIds,
            @RequestParam(required = false) List<Integer> donacionesInventarioIds,
            @RequestParam(required = false) List<Integer> cantidadesUsadas,
            @AuthenticationPrincipal CustomUserPrincipal user,
            Model model) {

        // Parseo de fecha
        LocalDateTime fecha;
        try {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT);
        } catch (Exception e) {
            fecha = LocalDateTime.parse(fechaEventoIso, HTML_DT_SEC);
        }

        // Donaciones
        List<DonacionUsadaDto> donaciones = new ArrayList<>();
        if (donacionesInventarioIds != null && cantidadesUsadas != null) {
            for (int i = 0; i < donacionesInventarioIds.size(); i++) {
                donaciones.add(new DonacionUsadaDto(
                        donacionesInventarioIds.get(i),
                        cantidadesUsadas.get(i)));
            }
        }

        // Construcción del DTO usando Lombok builder
        ModificarEventoRequestDto dto = ModificarEventoRequestDto.builder()
                .id(id)
                .nombre(nombre)
                .fechaEventoIso(fecha)
                .agregarMiembrosIds(agregarMiembrosIds != null ? agregarMiembrosIds : List.of())
                .quitarMiembrosIds(quitarMiembrosIds != null ? quitarMiembrosIds : List.of())
                .donacionesUsadas(donaciones)
                .actorUsuarioId(user.getId()) 
                .actorRol(user.getRole())
                .build();
        
        try {
            eventosService.modificarEvento(dto);
            return "redirect:/eventos";
        } catch (GrpcConnectionException e) {
            model.addAttribute("error", e.getMessage());
            return "eventos/editar_evento";
        }

    }

    @GetMapping("/eventos/eliminar/{id}")
    public String mostrarConfirmacionBaja(@PathVariable int id, Model model) {
        EventoDto evento = eventosService.buscarEventoPorId(id);

        if (evento == null) {
            model.addAttribute("error", "Evento no encontrado");
            return "eventos/eventos";
        }

        if (evento.getFechaEventoIso() != null && evento.getFechaEventoIso().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Solo se pueden dar de baja eventos a futuro");
            return "eventos/eventos";
        }

        model.addAttribute("evento", evento);
        return "eventos/confirmar_baja"; 
    }

    @PostMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable int id,
            @AuthenticationPrincipal CustomUserPrincipal user,
            Model model) {
        try {
            BajaEventoRequestDto dto = new BajaEventoRequestDto();
            dto.setId(id);
            dto.setActorUsuarioId(user.getId());
            dto.setActorRol(user.getRole());

        
            return "redirect:/eventos";
        } catch (GrpcConnectionException e) {
            model.addAttribute("error", e.getMessage());
            return "eventos/eventos";
        }
    }

   @GetMapping("/eventos/inventario/{id}")
public String mostrarFormularioInventario(@PathVariable int id, Model model) {
    EventoDto evento = eventosService.buscarEventoPorId(id);
   

    if (evento == null) {
        model.addAttribute("error", "Evento no encontrado");
        return "eventos/eventos";
    }

    if (evento.getFechaEventoIso() != null && evento.getFechaEventoIso().isAfter(LocalDateTime.now())) {
        model.addAttribute("error", "Solo se puede actualizar inventario de eventos ya realizados");
        return "eventos/eventos";
    }

    
    var inventario = inventarioService.listarInventario();

    model.addAttribute("evento", evento);
    model.addAttribute("inventario", inventario);

    return "eventos/actualizar_inventario";
}


@PostMapping("/eventos/inventario/{id}/donar/{inventarioId}")
public String registrarDonacion(
        @PathVariable int id,
        @PathVariable int inventarioId,
        @RequestParam("donacion") int donacion,
        @AuthenticationPrincipal CustomUserPrincipal user,
        RedirectAttributes redirectAttributes) {
    try {
        ModificarEventoRequestDto dto = ModificarEventoRequestDto.builder()
                .id(id)
                .donacionesUsadas(List.of(new DonacionUsadaDto(inventarioId, donacion)))
                .actorUsuarioId(user.getId())
                .actorRol(user.getRole())
                .build();

        System.out.println("➡️ Donación enviada: " + dto);
        eventosService.modificarEvento(dto);

        redirectAttributes.addFlashAttribute("mensaje", "Donación registrada correctamente");
        redirectAttributes.addFlashAttribute("tipo", "success");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("mensaje", "Error al registrar donación: " + e.getMessage());
        redirectAttributes.addFlashAttribute("tipo", "error");
    }
    return "redirect:/eventos/inventario/" + id;
}

@GetMapping("/eventos/editar/{id}")
public String mostrarFormularioEditar(@PathVariable int id, Model model) {
    EventoDto evento = eventosService.buscarEventoPorId(id);
    if (evento == null) {
        model.addAttribute("error", "Evento no encontrado");
        return "redirect:/eventos";
    }

    // cargar miembros actuales y disponibles
    var usuarios = usuarioService.listarUsuarios().getUsuarios();
    var miembrosActuales = usuarios.stream()
            .filter(u -> evento.getMiembrosIds().contains(u.getId()))
            .collect(Collectors.toList());
    var usuariosDisponibles = usuarios.stream()
            .filter(u -> !evento.getMiembrosIds().contains(u.getId()))
            .collect(Collectors.toList());

    model.addAttribute("evento", evento);
    model.addAttribute("miembrosActuales", miembrosActuales);
    model.addAttribute("usuariosDisponibles", usuariosDisponibles);

    return "eventos/editar_evento"; 
}


}
