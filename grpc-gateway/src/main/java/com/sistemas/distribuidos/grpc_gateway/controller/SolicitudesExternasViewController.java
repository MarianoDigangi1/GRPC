package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudesExternasAgrupadas;
import com.sistemas.distribuidos.grpc_gateway.service.SolicitudDonacionesExternasService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.KafkaProducerSolicitudDonaciones;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solicitudes-externas")
public class SolicitudesExternasViewController {
    private final KafkaProducerSolicitudDonaciones kafkaProducerSolicitudDonaciones;
    private final SolicitudDonacionesExternasService service;

    public SolicitudesExternasViewController(KafkaProducerSolicitudDonaciones kafkaProducerSolicitudDonaciones, SolicitudDonacionesExternasService service) {
        this.kafkaProducerSolicitudDonaciones = kafkaProducerSolicitudDonaciones;
        this.service = service;
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
            kafkaProducerSolicitudDonaciones.solicitarDonaciones(solicitudDonacion);
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
            kafkaProducerSolicitudDonaciones.darBajaSolicitudDonacion(solicitudId);

            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de donación dada de baja exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/solicitudes-externas/listar";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al dar de baja la solicitud: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "solicitudes_externas/bajaSolicitudDonacion";
        }
    }
}
