package com.sistemas.distribuidos.grpc_gateway.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioViewController {

    // Vista administrativa de usuarios (solo PRESIDENTE)
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("title", "Administración de Usuarios");
        return "usuarios/index"; // src/main/resources/templates/usuarios/index.html
    }

    // Formulario para crear usuario (solo PRESIDENTE)
    @GetMapping("/usuarios/crear")
    public String crearUsuario(Model model) {
        model.addAttribute("title", "Crear usuario");
        return "usuarios/crear"; // src/main/resources/templates/usuarios/crear.html
    }
}
