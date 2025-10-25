package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.inventario.InventarioListResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.kafka.transferencia.TransferenciaDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudesExternasAgrupadas;
import com.sistemas.distribuidos.grpc_gateway.service.InventarioService;
import com.sistemas.distribuidos.grpc_gateway.service.SolicitudDonacionesExternasService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.SolicitudDonacionesRestService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.TransferenciaDonacionRestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.UUID;

@Controller
@RequestMapping("/solicitudes-externas")
public class SolicitudesExternasViewController {
    private final SolicitudDonacionesRestService solicitudDonacionesRestService;
    private final TransferenciaDonacionRestService transferenciaDonacionRestService;
    private final SolicitudDonacionesExternasService service;
    private final InventarioService inventarioService;

    @Value("${kafka.producer.server.idOrganizacion}")
    private String idOrganizacion;

    public SolicitudesExternasViewController(
            SolicitudDonacionesRestService solicitudDonacionesRestService,
            TransferenciaDonacionRestService transferenciaDonacionRestService,
            SolicitudDonacionesExternasService service,
            InventarioService inventarioService) {
        this.solicitudDonacionesRestService = solicitudDonacionesRestService;
        this.transferenciaDonacionRestService = transferenciaDonacionRestService;
        this.service = service;
        this.inventarioService = inventarioService;
    }

    // TODO: aca tendria que ir la vista de solicitudes externas
    @GetMapping("/listar")
    public String visualizarSolicitudesExternas(Model model) {
        SolicitudesExternasAgrupadas solicitudesAgrupadas = service.listarSolicitudesAgrupadas();
        model.addAttribute("solicitudesPropias", solicitudesAgrupadas.getSolicitudesPropias());
        model.addAttribute("solicitudesExternas", solicitudesAgrupadas.getSolicitudesExternas());
        return "solicitudes_externas/listarSolicitudesExternas";
    }

    @GetMapping("/solicitar-donacion")
    public String solicitarDonacion(Model model) {
        model.addAttribute("solicitudDonacion", new SolicitudDonacionDto());
        return "solicitudes_externas/solicitarDonaciones";
    }

    @PostMapping("/solicitar-donacion")
    public String solicitarDonaciones(@ModelAttribute SolicitudDonacionDto solicitudDonacion,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            solicitudDonacionesRestService.solicitarDonaciones(solicitudDonacion);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de donación enviada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/solicitudes-externas/listar";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al enviar la solicitud: ");
            model.addAttribute("tipo", "error");
            model.addAttribute("solicitudDonacion", solicitudDonacion);
            return "solicitudes_externas/solicitarDonaciones";
        }
    }

    @GetMapping("/baja-solicitud-donacion/{idSolicitud}")
    public String darBajaSolicitudDonacion(@PathVariable String idSolicitud, Model model) {
        model.addAttribute("solicitudId", idSolicitud);
        return "solicitudes_externas/bajaSolicitudDonacion";
    }

    @PostMapping("/baja-solicitud-donacion")
    public String procesarBajaSolicitudDonacion(@RequestParam("solicitudId") String solicitudId,
                                                RedirectAttributes redirectAttributes,
                                                Model model) {
        try {
            solicitudDonacionesRestService.darBajaSolicitudDonacion(solicitudId);

            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de donación dada de baja exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/solicitudes-externas/listar";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al dar de baja la solicitud: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "solicitudes_externas/bajaSolicitudDonacion";
        }
    }

    @GetMapping("/transferir/{idSolicitud}")
    public String transferirDonacion(@PathVariable String idSolicitud, Model model, RedirectAttributes redirectAttributes) {
        try {
            SolicitudDonacionExternaResponseDto solicitud = service.buscaSolicitudPorId(idSolicitud);

            if (solicitud == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Solicitud no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
                return "redirect:/solicitudes-externas/listar";
            }

            List<InventarioListResponseDto> listaInventario = inventarioService.listarInventario();

            //TODO: Si da tiempo moverlo a algun service/metodo para uqe no esté todo acoplado en el controller
            if (solicitud.getContenido() != null && !solicitud.getContenido().isEmpty()) {
                Set<String> disponibles = listaInventario == null ? new HashSet<>() :
                        listaInventario.stream()
                                .filter(inv -> inv.getCantidad() > 0)
                                .map(inv -> (inv.getCategoria() + "\u0000" + inv.getDescripcion()).toLowerCase()) // esto lo hago asi para tener mayor control
                                .collect(Collectors.toSet());

                List<com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaItemResponseDto> original = solicitud.getContenido();
                List<com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaItemResponseDto> filtrado = original.stream()
                        .filter(item -> item != null
                                && item.getCategoria() != null
                                && item.getDescripcion() != null
                                && disponibles.contains((item.getCategoria() + "\u0000" + item.getDescripcion()).toLowerCase()))
                        .collect(Collectors.toList());

                if (filtrado.size() == 0) {
                    redirectAttributes.addFlashAttribute("mensaje", "No se puede transferir debido a que ese contenido no existe en el inventario");
                    redirectAttributes.addFlashAttribute("tipo", "error");
                    return "redirect:/solicitudes-externas/listar";
                }

                int removidos = original.size() - filtrado.size();
                solicitud.setContenido(filtrado);
                if (removidos > 0) {
                    model.addAttribute("mensajeInfo", String.format("Se descartaron algunas donaciones de la solicitud por no existir en el inventario."));
                    model.addAttribute("tipoInfo", "info");
                }
            }

            model.addAttribute("solicitud", solicitud);
            model.addAttribute("solicitudDonacion", new SolicitudDonacionDto());
            return "transferencias_externas";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cargar la solicitud: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/solicitudes-externas/listar";
        }
    }

    @PostMapping("/confirmar-transferencia")
    public String confirmarTransferencia(
            @RequestParam("idSolicitud") String idSolicitud,
            @ModelAttribute SolicitudDonacionDto solicitudDonacion,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            SolicitudDonacionExternaResponseDto solicitud = service.buscaSolicitudPorId(idSolicitud);
            
            if (solicitud == null) {
                redirectAttributes.addFlashAttribute("mensaje", "Solicitud no encontrada");
                redirectAttributes.addFlashAttribute("tipo", "error");
                return "redirect:/solicitudes-externas/listar";
            }

            //TODO: Faltaria un control de cantidad contra el inventario

            TransferenciaDonacionDto transferencia = new TransferenciaDonacionDto();
            transferencia.setIdTransferencia(UUID.randomUUID().toString());
            transferencia.setIdOrganizacionOrigen(idOrganizacion);
            transferencia.setIdOrganizacionDestino(solicitud.getIdOrganizacionExterna());
            transferencia.setDonaciones(solicitudDonacion.getContenido());

            transferenciaDonacionRestService.transferirDonacion(transferencia);

            redirectAttributes.addFlashAttribute("mensaje", "Transferencia realizada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/solicitudes-externas/listar";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al realizar la transferencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "error");
            return "redirect:/solicitudes-externas/listar";
        }
    }
}
