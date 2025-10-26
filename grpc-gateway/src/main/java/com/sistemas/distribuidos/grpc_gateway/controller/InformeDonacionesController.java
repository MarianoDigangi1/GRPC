package com.sistemas.distribuidos.grpc_gateway.controller;

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
                    .getInformeDonaciones(categoria, fechaInicio, fechaFin, null)
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
                eliminadoBool = eliminado.equalsIgnoreCase("true");
            }

            graphqlClientService.guardarFiltro(
                    userId,
                    nombreFiltro,
                    categoria,
                    fechaInicio,
                    fechaFin,
                    eliminadoBool
            ).block();

            redirectAttributes.addFlashAttribute("successMessage",
                    "Filtro '" + nombreFiltro + "' guardado exitosamente.");

        } catch (WebClientResponseException.BadRequest e) {
            System.err.println("Error 400 de GraphQL al guardar el filtro:");
            System.err.println(e.getResponseBodyAsString());

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al guardar el filtro: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al guardar el filtro: " + e.getMessage());
        }

        return "redirect:/informes/donaciones";
    }
}
