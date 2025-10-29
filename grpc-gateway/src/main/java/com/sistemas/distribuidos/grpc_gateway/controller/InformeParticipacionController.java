package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.GraphqlClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/informe/participacion")
public class InformeParticipacionController {

    private final GraphqlClientService graphqlClientService;

    @Autowired
    public InformeParticipacionController(GraphqlClientService graphqlClientService) {
        this.graphqlClientService = graphqlClientService;
    }

    /**
     * Muestra el formulario inicial para los eventos
     */
    @GetMapping
    public String mostrarFormulario(Model model, Authentication authentication) {
        CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();


        return "informe_donaciones/informe_donaciones";
    }
}
