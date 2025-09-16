package com.sistemas.distribuidos.grpc_gateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAndDeleteUserResponseDto {
    private UsuarioDto usuario;
    private String mensaje;
}