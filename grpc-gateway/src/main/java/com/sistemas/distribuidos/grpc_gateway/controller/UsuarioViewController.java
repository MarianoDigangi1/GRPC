package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateAndDeleteUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ✅ Vista principal de administración
    @GetMapping
    public String listarUsuarios(Model model,
                                 @RequestParam(required = false) String exitoMessage,
                                 @RequestParam(required = false) String errorMessage) {
        var usuarios = usuarioService.listarUsuarios().getUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("title", "Administración de Usuarios");
        if (exitoMessage != null) model.addAttribute("exitoMessage", exitoMessage);
        if (errorMessage != null) model.addAttribute("errorMessage", errorMessage);
        return "usuarios/usuarios";
    }

    // ✅ Formulario para crear usuario
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("title", "Crear usuario");
        return "usuarios/crear";
    }

    // ✅ Formulario para editar usuario
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
    try {
        var usuario = usuarioService.traerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("title", "Editar Usuario");
        return "usuarios/modificar";
    } catch (Exception e) {
        model.addAttribute("errorMessage", "No se pudo obtener el usuario: " + e.getMessage());
        return "redirect:/usuarios";
    }
}


    // ✅ Crear usuario (POST)
    @PostMapping("/crear")
    public String crearUsuario(@ModelAttribute CreateUserRequestDto createUserRequestDto,
                               RedirectAttributes redirectAttributes) {
        try {
            CreateUserResponseDto response = usuarioService.crearUsuario(createUserRequestDto);
            if (response.getMensaje() != null && response.getMensaje().toLowerCase().contains("correctamente")) {
                redirectAttributes.addFlashAttribute("exitoMessage", response.getMensaje());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMensaje());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }

    // ✅ Baja lógica del usuario (POST)
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id,
                                  @AuthenticationPrincipal CustomUserPrincipal user,
                                  RedirectAttributes redirectAttributes) {
        try {
            UpdateAndDeleteUserResponseDto response = usuarioService.bajaUsuario(id);
            if (response != null && response.getMensaje() != null) {
                redirectAttributes.addFlashAttribute("exitoMessage", response.getMensaje());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el usuario.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
