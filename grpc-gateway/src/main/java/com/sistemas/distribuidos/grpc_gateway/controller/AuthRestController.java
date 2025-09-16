package com.sistemas.distribuidos.grpc_gateway.controller;

import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginRequestDto;
import com.sistemas.distribuidos.grpc_gateway.service.AuthService;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginResponseDto;
import com.sistemas.distribuidos.grpc_gateway.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/login")
public class AuthRestController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthRestController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto loginResponseDto = authService.authenticate(loginRequestDto);

            // Valida que el login fue Ok
            if (!loginResponseDto.getResult().equals(LoginResponseDto.LoginResultCode.LOGIN_OK)) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of(
                                "error", "Credenciales inválidas",
                                "message", loginResponseDto.getMensaje()
                        ));
            }

            // Genera token JWT con el rol del usuario
            String token = jwtUtil.generateToken(
                loginResponseDto.getUsuario().getNombreUsuario(),
                loginResponseDto.getUsuario().getRol()
            );

            // Devuelve el token JWT junto con la información del usuario
            return ResponseEntity.ok(
                java.util.Map.of(
                    "token", token,
                    "username", loginResponseDto.getUsuario().getNombreUsuario(),
                    "role", loginResponseDto.getUsuario().getRol(),
                    "type", "Bearer",
                    "message", "Autenticación exitosa"
                )
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(java.util.Map.of(
                    "error", "Error interno del servidor",
                    "message", "Ocurrió un error durante la autenticación"
                ));
        }
    }
}
