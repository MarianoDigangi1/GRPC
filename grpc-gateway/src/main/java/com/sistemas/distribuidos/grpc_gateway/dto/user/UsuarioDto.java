package com.sistemas.distribuidos.grpc_gateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String claveHash;
    private String email;
    private String rol;
    private boolean activo;
}