package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.inventario.*;
import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.InventarioService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.KafkaProducerSolicitudDonaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventario")
public class InventarioViewController {

    private final InventarioService inventarioService;
    private final KafkaProducerSolicitudDonaciones kafkaProducerSolicitudDonaciones;

    @Autowired
    public InventarioViewController(InventarioService inventarioService, KafkaProducerSolicitudDonaciones kafkaProducerSolicitudDonaciones) {
        this.inventarioService = inventarioService;
        this.kafkaProducerSolicitudDonaciones = kafkaProducerSolicitudDonaciones;
    }

    @GetMapping
    public String mostrarInventario(Model model) {
        List<InventarioListResponseDto> inventarioListResponseDto = inventarioService.listarInventario();
        model.addAttribute("listaInventario", inventarioListResponseDto);
        return "inventario/listar";
    }

    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("inventario", new InventarioRequestDto());
        return "inventario/crear";
    }

    @PostMapping("/crear")
    public String crearInventario(@ModelAttribute InventarioRequestDto inventarioRequestDto,
                                  @AuthenticationPrincipal CustomUserPrincipal user,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            inventarioService.registrarInventario(inventarioRequestDto, user);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/inventario";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al crear inventario");
            model.addAttribute("tipo", "error");
            model.addAttribute("inventario", inventarioRequestDto);
            return "inventario/crear";
        }
    }

    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificar(@PathVariable int id, Model model) {
        try {
            InventarioDto inventario = inventarioService.obtenerInventarioPorId(id);
            model.addAttribute("inventario",  inventario);
            return "inventario/modificar";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al cargar inventario");
            model.addAttribute("tipo",  "error");
            return "redirect:/inventario";
        }
    }

    @PutMapping("/modificar/{id}")
    public String modificarInventario(@PathVariable int id,
                                    @ModelAttribute ModificarInventarioRequestDto modificarInventarioRequestDto,
                                    @AuthenticationPrincipal CustomUserPrincipal user,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            modificarInventarioRequestDto.setId(id);
            inventarioService.modificarInventario(modificarInventarioRequestDto, user);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario modificado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/inventario";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al modificar inventario");
            model.addAttribute("tipo", "error");
            model.addAttribute("inventario", modificarInventarioRequestDto);
            return "inventario/modificar";
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarInventario(@PathVariable int id,
                                   @AuthenticationPrincipal CustomUserPrincipal user,
                                   RedirectAttributes redirectAttributes) {
        try {
            BajaInventarioRequestDto bajaDto = BajaInventarioRequestDto.builder().id(id).build();
            inventarioService.eliminarInventario(bajaDto, user);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar inventario");
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/inventario";
        }
    }

    // TODO: aca tendria que ir la vista de solicitudes externas
    @GetMapping("/solicitudes-externas")
    public String visualizarSolicitudesExternas(Model model) {
        return "inventario/externas/listarSolicitudesExternas";
    }

    @GetMapping("/solicitar-donacion")
    public String solicitarDonacion(Model model) {
        model.addAttribute("solicitudDonacion", new SolicitudDonacionDto());
        return "inventario/externas/solicitarDonaciones";
    }

    @PostMapping("/solicitar-donacion")
    public String solicitarDonaciones(@ModelAttribute SolicitudDonacionDto solicitudDonacion,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        try {
            kafkaProducerSolicitudDonaciones.solicitarDonaciones(solicitudDonacion);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de donación enviada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/inventario/solicitudes-externas";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al enviar la solicitud: ");
            model.addAttribute("tipo", "error");
            model.addAttribute("solicitudDonacion", solicitudDonacion);
            return "inventario/externas/solicitarDonaciones";
        }
    }

    @GetMapping("/baja-solicitud-donacion")
    public String darBajaSolicitudDonacion(Model model) {
        return "inventario/externas/bajaSolicitudDonacion";
    }

    @PostMapping("/baja-solicitud-donacion")
    public String procesarBajaSolicitudDonacion(@RequestParam("solicitudId") String solicitudId,
                                                RedirectAttributes redirectAttributes,
                                                Model model) {
        try {
            kafkaProducerSolicitudDonaciones.darBajaSolicitudDonacion(solicitudId);

            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de donación dada de baja exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/inventario/solicitudes-externas";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al dar de baja la solicitud: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "inventario/externas/bajaSolicitudDonacion";
        }
    }

}
