package com.sistemas.distribuidos.grpc_gateway.web;

import com.sistemas.distribuidos.grpc_gateway.dto.user.ListarUsuariosResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateAndDeleteUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    /*
    @PutMapping("/modificar/{idUsuario}")
    public ResponseEntity<?> editarUsuario(
            @PathVariable int idUsuario,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        UpdateAndDeleteUserResponseDto response = usuarioService.modificarUsuario(updateUserRequestDto, idUsuario);

        if (response.getMensaje() != null && response.getMensaje().toLowerCase().contains("editado")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }*/

    @DeleteMapping("/usuarios/eliminar/{idUsuario}")
    public ResponseEntity<?> darBajaUsuario(@PathVariable int idUsuario) {
        UpdateAndDeleteUserResponseDto response = usuarioService.bajaUsuario(idUsuario);
        String mensaje = response.getMensaje() != null ? response.getMensaje().toLowerCase() : "";

        if (mensaje.contains("Usuario dado de baja")) {
            return ResponseEntity.status(204).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


}
