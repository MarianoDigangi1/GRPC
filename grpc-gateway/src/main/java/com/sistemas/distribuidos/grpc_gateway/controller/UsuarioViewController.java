package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.user.*;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 🔹 Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        ListarUsuariosResponseDto response = usuarioService.listarUsuarios();
        model.addAttribute("usuarios", response.getUsuarios());
        model.addAttribute("title", "Administración de Usuarios");
        return "usuarios/usuarios"; // <-- Ajuste: tu archivo se llama usuarios.html
    }

    // 🔹 Formulario para crear usuario
    @GetMapping("/crear")
    public String crearUsuario(Model model) {
        model.addAttribute("title", "Crear usuario");
        model.addAttribute("usuario", new UpdateUserRequestDto());
        return "usuarios/crear";
    }

    // 🔹 Formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        try {
            UserResponseDto usuario = usuarioService.traerUsuarioPorId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("title", "Editar Usuario");
            return "usuarios/modificar";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "No se pudo obtener el usuario: " + e.getMessage());
            return "redirect:/usuarios";
        }
    }

    // 🔹 Guardar cambios de edición (POST)
    @PostMapping("/editar")
public String editarUsuario(
        @ModelAttribute UpdateUserRequestDto updateUserRequestDto,
        RedirectAttributes redirectAttributes
) {
    try {
        // 🧩 Debug: Ver qué llega desde el formulario
        System.out.println("🟢 ID recibido en formulario: " + updateUserRequestDto.getId());
        System.out.println("🟢 Nombre recibido: " + updateUserRequestDto.getNombre());
        System.out.println("🟢 Apellido recibido: " + updateUserRequestDto.getApellido());
        System.out.println("🟢 Email recibido: " + updateUserRequestDto.getEmail());
        System.out.println("🟢 Rol recibido: " + updateUserRequestDto.getRol());
        System.out.println("🟢 Activo: " + updateUserRequestDto.isActivo());
        UpdateAndDeleteUserResponseDto response =
                usuarioService.modificarUsuario(updateUserRequestDto, updateUserRequestDto.getId());

        if (response.getMensaje() != null &&
            response.getMensaje().toLowerCase().contains("editado")) {

            redirectAttributes.addFlashAttribute("mensaje", "✅ Usuario editado correctamente");
            return "redirect:/usuarios";
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "⚠️ No se pudo actualizar el usuario: " + response.getMensaje());
            return "redirect:/usuarios";
        }

    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error",
                "❌ Error al actualizar usuario: " + e.getMessage());
        return "redirect:/usuarios";
    }
}


    // 🔹 Dar de baja un usuario (POST con método oculto)
    @PostMapping("/eliminar/{id}")
    public String darBajaUsuario(@PathVariable int id, Model model) {
        try {
            UpdateAndDeleteUserResponseDto response = usuarioService.bajaUsuario(id);

            if (response.getMensaje() != null && response.getMensaje().toLowerCase().contains("baja")) {
                return "redirect:/usuarios";
            } else {
                model.addAttribute("errorMessage", response.getMensaje());
                return "redirect:/usuarios";
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al dar de baja usuario: " + e.getMessage());
            return "redirect:/usuarios";
        }
    }
}
