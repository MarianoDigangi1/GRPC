package com.sistemas.distribuidos.grpc_gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistemas.distribuidos.grpc_gateway.dto.graphql.ReporteDonacionDTO;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.GraphqlClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/informes/donaciones")
public class InformeDonacionesController {

    private final GraphqlClientService graphqlClientService;

    @Autowired
    public InformeDonacionesController(GraphqlClientService graphqlClientService) {
        this.graphqlClientService = graphqlClientService;
    }

    /**
     * Muestra el formulario inicial con los filtros guardados del usuario.
     */
    @GetMapping
    public String mostrarFormulario(Model model, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();

        List<Map<String, Object>> filtrosGuardados = graphqlClientService
                .getMisFiltros(user.getId())
                .block();

        model.addAttribute("usuario", user.getUsername());
        model.addAttribute("filtrosGuardados", filtrosGuardados);
        model.addAttribute("resultados", null);
        model.addAttribute("param", Collections.emptyMap());
        return "informe_donaciones/informe_donaciones";
    }

    /**
     * Consulta los datos del informe de donaciones según filtros aplicados.
     */
    @GetMapping("/consultar")
    public String consultarInforme(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            @RequestParam(required = false) String eliminado,
            Model model,
            Authentication authentication
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("categoria", categoria);
        params.put("fechaInicio", fechaInicio);
        params.put("fechaFin", fechaFin);
        params.put("eliminado", eliminado);

        try {
            CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
            List<ReporteDonacionDTO> resultados = graphqlClientService
                    .getInformeDonaciones(categoria, fechaInicio, fechaFin, eliminado)
                    .block();

            List<Map<String, Object>> filtrosGuardados = graphqlClientService
                    .getMisFiltros(user.getId())
                    .block();

            model.addAttribute("resultados", resultados != null ? resultados : Collections.emptyList());
            model.addAttribute("filtrosGuardados", filtrosGuardados);
            model.addAttribute("usuario", user.getUsername());
            model.addAttribute("param", params);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al obtener el informe: " + e.getMessage());
            model.addAttribute("resultados", Collections.emptyList());
            model.addAttribute("param", params);
        }

        return "informe_donaciones/informe_donaciones";
    }

    /**
     * Guarda un filtro de búsqueda.
     */
    @PostMapping("/filtros/guardar")
public String guardarFiltro(
        @RequestParam String nombreFiltro,
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) String fechaInicio,
        @RequestParam(required = false) String fechaFin,
        @RequestParam(required = false) String eliminado,
        Authentication authentication,
        RedirectAttributes redirectAttributes
) {
    try {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        long userId = user.getId();

        Boolean eliminadoBool = null;
        if (eliminado != null && !eliminado.isBlank()) {
            if (eliminado.equalsIgnoreCase("si")) eliminadoBool = true;
            else if (eliminado.equalsIgnoreCase("no")) eliminadoBool = false;
        }

        graphqlClientService.guardarFiltro(
                userId,
                nombreFiltro,
                categoria,
                fechaInicio,
                fechaFin,
                eliminadoBool
        ).block();

        redirectAttributes.addFlashAttribute("successMessage", "Filtro '" + nombreFiltro + "' guardado exitosamente.");
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el filtro: " + e.getMessage());
    }
    return "redirect:/informes/donaciones";
}



    @GetMapping("/filtros/aplicar")
public String aplicarFiltro(
        @RequestParam("id") long filtroId,
        Authentication authentication,
        Model model
) {
    CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();

    List<Map<String, Object>> filtrosGuardados = graphqlClientService
            .getMisFiltros(user.getId())
            .block();

    if (filtrosGuardados == null || filtrosGuardados.isEmpty()) {
        model.addAttribute("errorMessage", "No se encontraron filtros guardados.");
        model.addAttribute("resultados", null);
        return "informe_donaciones/informe_donaciones";
    }

    Map<String, Object> filtroSeleccionado = filtrosGuardados.stream()
            .filter(f -> {
                Object idObj = f.get("id");
                if (idObj == null) return false;
                try {
                    return Long.parseLong(idObj.toString()) == filtroId;
                } catch (NumberFormatException e) {
                    return false;
                }
            })
            .findFirst()
            .orElse(null);

    if (filtroSeleccionado == null) {
        model.addAttribute("errorMessage", "No se encontró el filtro seleccionado.");
        model.addAttribute("filtrosGuardados", filtrosGuardados);
        model.addAttribute("resultados", null);
        return "informe_donaciones/informe_donaciones";
    }

    try {
        String filtrosJson = (String) filtroSeleccionado.get("filtrosJson");
        Map<String, Object> map = new ObjectMapper().readValue(filtrosJson, Map.class);

        String categoria = (String) map.getOrDefault("categoria", "");
        String fechaInicio = (String) map.getOrDefault("fechaInicio", "");
        String fechaFin = (String) map.getOrDefault("fechaFin", "");
        Object eliminadoObj = map.get("eliminado");
        String eliminado = eliminadoObj == null ? "" : (Boolean.TRUE.equals(eliminadoObj) ? "si" : "no");

        List<ReporteDonacionDTO> resultados = graphqlClientService
                .getInformeDonaciones(categoria, fechaInicio, fechaFin, eliminado)
                .block();

        model.addAttribute("resultados", resultados != null ? resultados : Collections.emptyList());
        model.addAttribute("filtrosGuardados", filtrosGuardados);
        model.addAttribute("param", Map.of(
                "categoria", categoria,
                "fechaInicio", fechaInicio,
                "fechaFin", fechaFin,
                "eliminado", eliminado
        ));
        model.addAttribute("usuario", user.getUsername());
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("errorMessage", "Error al aplicar el filtro: " + e.getMessage());
        model.addAttribute("resultados", Collections.emptyList());
    }

    return "informe_donaciones/informe_donaciones";
}

@PostMapping("/filtros/eliminar")
public String eliminarFiltro(
        @RequestParam("id") long filtroId,
        Authentication authentication,
        RedirectAttributes redirectAttributes
) {
    try {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        graphqlClientService.eliminarFiltro(user.getId(), filtroId).block();
        redirectAttributes.addFlashAttribute("successMessage", "Filtro eliminado correctamente.");
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el filtro: " + e.getMessage());
    }

    return "redirect:/informes/donaciones";
}

@PostMapping("/filtros/editar")
public String editarFiltro(
        @RequestParam("id") long filtroId,
        @RequestParam("nuevoNombre") String nuevoNombre,
        Authentication authentication,
        RedirectAttributes redirectAttributes
) {
    try {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();
        graphqlClientService.editarFiltro(user.getId(), filtroId, nuevoNombre).block();
        redirectAttributes.addFlashAttribute("successMessage", "Filtro editado correctamente.");
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("errorMessage", "Error al editar el filtro: " + e.getMessage());
    }

    return "redirect:/informes/donaciones";
}


}
