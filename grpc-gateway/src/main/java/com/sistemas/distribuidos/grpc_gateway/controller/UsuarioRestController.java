package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.*;
import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateAndDeleteUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import com.sistemas.distribuidos.grpc_gateway.service.UsuarioService;
import com.sistemas.distribuidos.grpc_gateway.utils.UsuarioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<CreateUserResponseDto> crearUsuario(@RequestBody CreateUserRequestDto createUserRequestDto) {
        CreateUserResponseDto response = usuarioService.crearUsuario(createUserRequestDto);

        if (response.getMensaje() != null && response.getMensaje().equalsIgnoreCase("Usuario creado correctamente")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/modificar/{idUsuario}")
    public ResponseEntity<?> editarUsuario(
            @PathVariable int idUsuario,
            @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        UpdateAndDeleteUserResponseDto response = usuarioService.modificarUsuario(updateUserRequestDto, idUsuario);

        if (response.getMensaje() != null && response.getMensaje().toLowerCase().contains("modificado")) {
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<?> darBajaUsuario(@PathVariable int idUsuario) {
        UpdateAndDeleteUserResponseDto response = usuarioService.eliminarUsuario(idUsuario);
        String mensaje = response.getMensaje() != null ? response.getMensaje().toLowerCase() : "";

        if (mensaje.contains("usuario dado de baja correctamente")) {
            return ResponseEntity.status(204).body(response);
        } else if (mensaje.contains("ya está inactivo")) {
            // Mejor respuesta para usuario ya inactivo
            return ResponseEntity.status(409).body(
                new ErrorResponseDto("El usuario ya está inactivo", response.getMensaje())
            );
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
