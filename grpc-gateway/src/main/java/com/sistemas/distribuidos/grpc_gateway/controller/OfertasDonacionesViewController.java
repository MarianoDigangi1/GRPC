package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.SolicitudDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.kafka.donacion.oferta.OfertaDonacionDto;
import com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones.OfertaDonacionResponseDto;
import com.sistemas.distribuidos.grpc_gateway.service.OfertaDonacionesService;
import com.sistemas.distribuidos.grpc_gateway.service.kafka.OfertaDonacionesRestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ofertas-donaciones")
public class OfertasDonacionesViewController {
    
    private final OfertaDonacionesService service;
    private final OfertaDonacionesRestService restService;

    public OfertasDonacionesViewController(OfertaDonacionesService service, OfertaDonacionesRestService restService) {
        this.service = service;
        this.restService = restService;
    }

    @GetMapping("/listar")
    public String listarOfertasDonaciones(Model model) {
        try {
            List<OfertaDonacionResponseDto> ofertas = service.listarSolicitudes();
            model.addAttribute("ofertas", ofertas);
            return "ofertas_donaciones/listarOfertasDonaciones";
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al cargar las ofertas de donaciones: " + e.getMessage());
            model.addAttribute("tipo", "error");
            return "ofertas_donaciones/listarOfertasDonaciones";
        }
    }

    @GetMapping("/ofrecer")
    public String ofrecerDonaciones(Model model) {
        model.addAttribute("ofertaDonacion", new OfertaDonacionDto());
        return "ofertas_donaciones/ofrecerDonaciones";
    }

    @PostMapping("/ofrecer")
    public String ofrecerDonaciones(@ModelAttribute OfertaDonacionDto ofertaDonacion,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            restService.ofrecerDonaciones(ofertaDonacion);
            redirectAttributes.addFlashAttribute("mensaje", "Oferta de donación enviada exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
            return "redirect:/ofertas-donaciones/listar";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al enviar la oferta: ");
            model.addAttribute("tipo", "error");
            model.addAttribute("solicitudDonacion", ofertaDonacion);
            return "ofertas-donaciones/ofrecer";
        }
    }

}
