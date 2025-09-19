package com.sistemas.distribuidos.grpc_gateway.web;

import com.sistemas.distribuidos.grpc_gateway.dto.user.ListarUsuariosResponseDto;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioViewController {

    private final UsuarioService usuarioService;
    @Autowired
    public UsuarioViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Vista administrativa de usuarios (solo PRESIDENTE)
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        ListarUsuariosResponseDto response = usuarioService.listarUsuarios();

        model.addAttribute("usuarios", response.getUsuarios());
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
