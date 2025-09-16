package com.sistemas.distribuidos.grpc_gateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    private String nombreUsuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String rol;
    private boolean activo;
}