package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.evento.*;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.EventosService;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import com.sistemas.distribuidos.grpc_gateway.service.InventarioService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.EventosRestService;
import org.springframework.web.bind.annotation.ModelAttribute;



import com.sistemas.distribuidos.grpc_gateway.service.FiltroEventoService;

import org.springframework.http.HttpStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
public class EventosViewController {

    

private final EventosService eventosService;
private final UsuarioService usuarioService;
private final InventarioService inventarioService;
private final EventosRestService eventosRestService;
private final FiltroEventoService filtroEventoService;




private static final DateTimeFormatter HTML_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
private static final DateTimeFormatter HTML_DT_SEC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

@Autowired
public EventosViewController(
        EventosService eventosService,
        UsuarioService usuarioService,
        InventarioService inventarioService,
        EventosRestService eventosRestService,
        FiltroEventoService filtroEventoService
       ) {

    this.eventosService = eventosService;
    this.usuarioService = usuarioService;
    this.inventarioService = inventarioService;
    this.eventosRestService = eventosRestService;
    this.filtroEventoService = filtroEventoService;

}


 @GetMapping("/eventos")
public String vistaEventos(
        @AuthenticationPrincipal CustomUserPrincipal user,
        Model model,
        @RequestParam(required = false) String fechaDesde,
        @RequestParam(required = false) String fechaHasta,
        @RequestParam(required = false, defaultValue = "todos") String estado
) {
    // Listar todos los eventos
    List<EventoDto> eventos = eventosService.listarEventos();

// Aplicar filtros si vienen
if ((fechaDesde != null && !fechaDesde.isBlank()) || (fechaHasta != null && !fechaHasta.isBlank()) || (estado != null && !estado.equalsIgnoreCase("todos"))) {
    LocalDate desde = null;
    LocalDate hasta = null;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // o el formato exacto que viene en el JSON

    try {
        if (fechaDesde != null && !fechaDesde.isBlank()) {
            desde = LocalDate.parse(fechaDesde, formatter);
        }
    } catch (Exception e) {
        desde = null;
    }

    try {
        if (fechaHasta != null && !fechaHasta.isBlank()) {
            hasta = LocalDate.parse(fechaHasta, formatter);
        }
    } catch (Exception e) {
        hasta = null;
    }

    LocalDate finalDesde = desde;
    LocalDate finalHasta = hasta;

    eventos = eventos.stream()
            .filter(ev -> finalDesde == null || !ev.getFechaEventoIso().toLocalDate().isBefore(finalDesde))
            .filter(ev -> finalHasta == null || !ev.getFechaEventoIso().toLocalDate().isAfter(finalHasta))
            /*.filter(ev -> {
                //if ("propios".equalsIgnoreCase(estado)) return ev.getEventoIdOrganizacionExterna() == null || ev.getEventoIdOrganizacionExterna().isBlank();
                //if ("externos".equalsIgnoreCase(estado)) return ev.getEventoIdOrganizacionExterna() != null && !ev.getEventoIdOrganizacionExterna().isBlank();
                return true;
            })*/
            .collect(Collectors.toList());

}
      
    

    // Mapear nombres de miembros activos
    List<UserResponseDto> usuarios = usuarioService.listarUsuarios().getUsuarios();
    Map<Integer, String> nombresPorId = usuarios.stream()
            .collect(Collectors.toMap(UserResponseDto::getId, u -> u.getNombre() + " " + u.getApellido()));
    Set<Integer> idsActivos = usuarios.stream()
            .filter(UserResponseDto::isActivo)
            .map(UserResponseDto::getId)
            .collect(Collectors.toSet());

    for (EventoDto ev : eventos) {
        if (ev.getMiembrosIds() != null) {
            List<Integer> filtrados = ev.getMiembrosIds().stream()
                    .filter(idsActivos::contains)
                    .collect(Collectors.toList());
            ev.setMiembrosIds(filtrados);
        }
        List<Integer> ids = ev.getMiembrosIds();
        if (ids != null && !ids.isEmpty()) {
            List<String> nombres = ids.stream()
                    .map(nombresPorId::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            ev.setMiembrosNombres(String.join(", ", nombres));
        } else {
            ev.setMiembrosNombres("-");
        }
    }

    // Particionar eventos: propios vs externos
    List<EventoDto> eventosPropios = eventos.stream()
            .filter(ev -> ev.getEventoIdOrganizacionExterna() == null || ev.getEventoIdOrganizacionExterna().isBlank())
            .collect(Collectors.toList());
    List<EventoDto> eventosExternos = eventos.stream()
            .filter(ev -> ev.getEventoIdOrganizacionExterna() != null && !ev.getEventoIdOrganizacionExterna().isBlank())
            .collect(Collectors.toList());

    // Roles del usuario
    String role = (user != null ? user.getRole() : null);
    model.addAttribute("isPresiOrCoord", "Presidente".equalsIgnoreCase(role) || "Coordinador".equalsIgnoreCase(role));
    model.addAttribute("isVocal", "Vocal".equalsIgnoreCase(role));
    model.addAttribute("isVoluntario", "Voluntario".equalsIgnoreCase(role));
    model.addAttribute("userId", (user != null ? user.getId() : null));

    // Agregar eventos al modelo
    model.addAttribute("eventosPropios", eventosPropios);
    model.addAttribute("eventosExternos", eventosExternos);
    model.addAttribute("eventos", eventos);

    // ==== CARGAR FILTROS DEL USUARIO ====
    try {
        List<FiltroEventoDto> filtrosUsuario = (user != null) ? filtroEventoService.obtenerFiltrosPorUsuario(user.getId()) : Collections.emptyList();
        model.addAttribute("filtrosUsuario", filtrosUsuario);
    } catch (GrpcConnectionException e) {
        model.addAttribute("errorFiltros", "No se pudieron cargar los filtros del usuario.");
        model.addAttribute("filtrosUsuario", Collections.emptyList());
    }

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
            model.addAttribute("evento", dto);

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

            eventosService.darBajaEvento(dto);
        
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

    var usuarios = usuarioService.listarUsuarios().getUsuarios();

    // 🧩 Filtrar solo los activos
    var usuariosActivos = usuarios.stream()
            .filter(UserResponseDto::isActivo)
            .collect(Collectors.toList());

    // Miembros actuales (de los activos)
    var miembrosActuales = usuariosActivos.stream()
            .filter(u -> evento.getMiembrosIds().contains(u.getId()))
            .collect(Collectors.toList());

    // Usuarios disponibles (activos y que no estén ya en el evento)
    var usuariosDisponibles = usuariosActivos.stream()
            .filter(u -> !evento.getMiembrosIds().contains(u.getId()))
            .collect(Collectors.toList());

    model.addAttribute("evento", evento);
    model.addAttribute("miembrosActuales", miembrosActuales);
    model.addAttribute("usuariosDisponibles", usuariosDisponibles);

    return "eventos/editar_evento";
}


    @PostMapping("/eventos/unirse/{id}")
public String unirseEvento(@PathVariable int id,
                           @AuthenticationPrincipal CustomUserPrincipal user,
                           RedirectAttributes redirectAttributes) {
    try {
        ModificarEventoRequestDto dto = ModificarEventoRequestDto.builder()
                .id(id)
                .agregarMiembrosIds(List.of(user.getId()))
                .quitarMiembrosIds(List.of())
                .donacionesUsadas(List.of())   // ✅ aseguramos lista vacía
                .actorUsuarioId(user.getId())
                .actorRol(user.getRole())
                .build();

        eventosService.modificarEvento(dto);
        redirectAttributes.addFlashAttribute("exitoMessage", "Te uniste correctamente al evento.");
    } catch (GrpcConnectionException e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al conectar con gRPC: " + e.getMessage());
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al unirse: " + e.getMessage());
    }
    return "redirect:/eventos";
}

@PostMapping("/eventos/salir/{id}")
public String salirEvento(@PathVariable int id,
                          @AuthenticationPrincipal CustomUserPrincipal user,
                          RedirectAttributes redirectAttributes) {
    try {
        ModificarEventoRequestDto dto = ModificarEventoRequestDto.builder()
                .id(id)
                .agregarMiembrosIds(List.of())
                .quitarMiembrosIds(List.of(user.getId()))
                .donacionesUsadas(List.of())   // ✅ importante: lista vacía, no null
                .actorUsuarioId(user.getId())
                .actorRol(user.getRole())
                .build();

        eventosService.modificarEvento(dto);
        redirectAttributes.addFlashAttribute("exitoMessage", "Has salido del evento correctamente.");
    } catch (GrpcConnectionException e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al conectar con gRPC: " + e.getMessage());
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error al salir: " + e.getMessage());
    }
    return "redirect:/eventos";
}

    @GetMapping("/publicarEvento")
    public String mostrarFormularioNuevoEventoExterno(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "eventos/externos/publicar_evento";
    }

    @PostMapping("/publicarEvento")
    public String publicarEvento(@RequestParam String nombre,
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

            eventosRestService.publicarEvento(dto);


            redirectAttributes.addFlashAttribute("exitoMessage", "Evento creado correctamente");
            return "redirect:/eventos";

        } catch (Exception e) {
            // Error de negocio (ej: fecha pasada)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/publicarEvento";

        }
    }


    @GetMapping("/darBajaEventoAlExterior/{id}")
    public String mostrarBajaEventoExterior(@PathVariable int id, Model model) {
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
        return "eventos/externos/dar_baja_evento";
    }

    @PostMapping("/darBajaEventoAlExterior/{id}")
    public String eliminarEventoAlExterior(@PathVariable int id,
                                 @AuthenticationPrincipal CustomUserPrincipal user,
                                 Model model) {
        try {
            eventosRestService.darBajaEvento(id);
            //eventosService.darBajaEvento(dto);

            return "redirect:/eventos";
        } catch (GrpcConnectionException e) {
            model.addAttribute("error", e.getMessage());
            return "eventos/eventos";
        }
    }

// =========================
// VISTA CREAR FILTRO (FORMULARIO)
// =========================
@GetMapping("/filtros-eventos/nuevo")
public String nuevoFiltro(Model model, @AuthenticationPrincipal CustomUserPrincipal user) {
    model.addAttribute("userId", user.getId());
    model.addAttribute("filtroEventoDto", new FiltroEventoDto());
    return "eventos/nuevo_filtro"; // Ruta del template
}

// =========================
// CREAR FILTRO - FORMULARIO
// =========================
@PostMapping("/filtros-eventos/usuario/{usuarioId}")
public String crearFiltroVista(@PathVariable int usuarioId,
                               @ModelAttribute FiltroEventoDto filtroEventoDto,
                               Model model) {
    try {
        if (filtroEventoDto.getParametros() == null || filtroEventoDto.getParametros().isBlank()) {
            filtroEventoDto.setParametros("{}");
        }
        filtroEventoDto.setUsuarioId(usuarioId);
        filtroEventoService.crearFiltro(filtroEventoDto);
        model.addAttribute("exitoMessage", "Filtro creado correctamente: " + filtroEventoDto.getNombre());
        return "redirect:/eventos";
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("errorMessage", "Error al crear filtro: " + e.getMessage());
        return "eventos/nuevo_filtro";
    }
}

@GetMapping("/filtros-eventos/ver/{id}")
public String verFiltro(@PathVariable int id, Model model) {
    try {
        FiltroEventoDto filtro = filtroEventoService.obtenerFiltroPorId(id);
        System.out.println("DEBUG - filtro obtenido: " + filtro); // <--- esto
        model.addAttribute("filtroEventoDto", filtro);
        return "eventos/ver_filtro";
    } catch (Exception e) {
        System.out.println("DEBUG - Exception: " + e.getMessage());
        model.addAttribute("errorMessage", "No se pudo cargar el filtro: " + e.getMessage());
        return "redirect:/eventos";
    }
}




// =========================
// EDITAR FILTRO (formulario)
// =========================
@GetMapping("/filtros-eventos/editar/{id}")
public String editarFiltro(@PathVariable int id, Model model) {
    try {
        FiltroEventoDto filtro = filtroEventoService.obtenerFiltroPorId(id);
        model.addAttribute("filtroEventoDto", filtro);
        return "eventos/editar_filtro";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "No se pudo cargar el filtro para editar: " + e.getMessage());
        return "redirect:/eventos";
    }
}

// =========================
// ACTUALIZAR FILTRO (procesar formulario)
// =========================
@PostMapping("/filtros-eventos/actualizar/{id}")
public String actualizarFiltro(@PathVariable int id,
                               @ModelAttribute FiltroEventoDto filtroEventoDto,
                               Model model) {
    try {
        if (filtroEventoDto.getParametros() == null || filtroEventoDto.getParametros().isBlank()) {
            filtroEventoDto.setParametros("{}");
        }
        filtroEventoService.actualizarFiltro(id, filtroEventoDto);
        model.addAttribute("exitoMessage", "Filtro actualizado correctamente: " + filtroEventoDto.getNombre());
        return "redirect:/eventos";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al actualizar filtro: " + e.getMessage());
        return "eventos/editar_filtro";
    }
}


// =========================
// ELIMINAR FILTRO (botón desde vista)
// =========================
@PostMapping("/filtros-eventos/eliminar/{id}")
public String eliminarFiltroVista(@PathVariable int id, Model model) {
    try {
        filtroEventoService.eliminarFiltro(id);
        model.addAttribute("exitoMessage", "Filtro eliminado correctamente.");
    } catch (Exception e) {
        model.addAttribute("errorMessage", "Error al eliminar filtro: " + e.getMessage());
    }
    return "redirect:/eventos";
}

@GetMapping("/filtros-eventos/aplicar/{id}")
public String aplicarFiltro(@PathVariable int id, Model model) {
    try {
        FiltroEventoDto filtro = filtroEventoService.obtenerFiltroPorId(id);
        if (filtro == null || filtro.getParametros() == null || filtro.getParametros().isBlank()) {
            model.addAttribute("errorMessage", "El filtro no tiene parámetros válidos.");
            return "redirect:/eventos";
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> params = mapper.readValue(filtro.getParametros(), Map.class);

        String fechaDesde = params.get("fechaDesde") != null ? params.get("fechaDesde").toString() : "";
        String fechaHasta = params.get("fechaHasta") != null ? params.get("fechaHasta").toString() : "";
        String estado = params.get("estado") != null ? params.get("estado").toString() : "todos";

        // Construimos la URL solo con parámetros válidos
        StringBuilder redirectUrl = new StringBuilder("redirect:/eventos?");
        if (!fechaDesde.isBlank()) redirectUrl.append("fechaDesde=").append(fechaDesde).append("&");
        if (!fechaHasta.isBlank()) redirectUrl.append("fechaHasta=").append(fechaHasta).append("&");
        if (!estado.isBlank() && !estado.equalsIgnoreCase("todos")) redirectUrl.append("estado=").append(estado).append("&");

        // Quitar el último "&" si existe
        if (redirectUrl.charAt(redirectUrl.length() - 1) == '&') {
            redirectUrl.deleteCharAt(redirectUrl.length() - 1);
        }

        return redirectUrl.toString();
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("errorMessage", "Error al aplicar el filtro: " + e.getMessage());
        return "redirect:/eventos";
    }
}







}







