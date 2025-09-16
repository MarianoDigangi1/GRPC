package com.sistemas.distribuidos.grpc_gateway.converter;

import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginResponseDto;
import com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse;

public class AuthConverter {

    public static LoginResponseDto convertResponseToDto(LoginResponse loginResponse){
        return new LoginResponseDto(loginResponse.getResultValue(),
                loginResponse.getMensaje(),
                UsuarioConverter.convertirUsuario(loginResponse.getUsuario())
        );
    }
}
