package com.sistemas.distribuidos.grpc_gateway.web;

import com.sistemas.distribuidos.grpc_gateway.dto.user.ListarUsuariosResponseDto;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;
    @Autowired
    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "Spring Boot + Thymeleaf");
        model.addAttribute("message", "¡Hola! Esta es una vista renderizada con Thymeleaf.");
        return "index"; // src/main/resources/templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // src/main/resources/templates/login.html
    }

    @GetMapping("/administrar")
    public String administrar(Model model) {
        ListarUsuariosResponseDto response = usuarioService.listarUsuarios();

        model.addAttribute("usuarios", response.getUsuarios());
        model.addAttribute("title", "Administración de Usuarios");
        return "usuarios/index";
    }
}
