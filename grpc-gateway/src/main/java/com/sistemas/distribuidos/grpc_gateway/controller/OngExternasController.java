package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.OngDTO;
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.PresidenteDTO;
import com.sistemas.distribuidos.grpc_gateway.service.OngSoapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/ong-externas")
public class OngExternasController {

    private final OngSoapService ongSoapService;

    @Autowired
    public OngExternasController(OngSoapService ongSoapService) {
        this.ongSoapService = ongSoapService;
    }

    @GetMapping("/consultar")
    public String mostrarPaginaDeConsulta(
            @RequestParam(value = "ids", required = false) String idsCsv,
            Model model) {

        List<PresidenteDTO> presidentes = Collections.emptyList();
        List<OngDTO> asociaciones = Collections.emptyList();
        String error = null;

        if (idsCsv != null && !idsCsv.isBlank()) {
            try {
                List<String> idList = Arrays.asList(idsCsv.split(","));
                presidentes = ongSoapService.getPresidentes(idList);
                asociaciones = ongSoapService.getAsociaciones(idList);

                if (presidentes.isEmpty() && asociaciones.isEmpty()) {
                    error = "No se encontraron presidentes ni ONGs para los IDs ingresados.";
                } else if (presidentes.isEmpty()) {
                    error = "Se encontraron las ONGs, pero no hay presidentes asociados a esos IDs.";
                } else if (asociaciones.isEmpty()) {
                    error = "Se encontraron presidentes, pero no se hallaron datos de sus ONGs asociadas.";
                }

            } catch (RuntimeException e) {
                error = "No se pudieron obtener los datos de la red de ONGs. "
                        + "Asegúrese de que el microservicio 'ong-soap' esté funcionando. "
                        + "Error: " + e.getMessage();
            }
        }

        model.addAttribute("presidentes", presidentes);
        model.addAttribute("asociaciones", asociaciones);
        model.addAttribute("error", error);
        model.addAttribute("idsEnviados", idsCsv);

        return "informacion_externa/consultar_ong";
    }
}
