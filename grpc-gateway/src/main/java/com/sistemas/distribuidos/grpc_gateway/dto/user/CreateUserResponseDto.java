package com.sistemas.distribuidos.grpc_gateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponseDto {
    private UsuarioDto usuario;
    private String generatedPassword;
    private String mensaje;
}