package com.sistemas.distribuidos.grpc_gateway.dto.auth;

import com.sistemas.distribuidos.grpc_gateway.dto.user.UsuarioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private LoginResultCode result;
    private String mensaje;
    private UsuarioDto usuario;

    public LoginResponseDto(int resultCode, String mensaje, UsuarioDto usuario) {
        this.result = LoginResultCode.fromCode(resultCode);
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public enum LoginResultCode {
        LOGIN_OK(0),
        LOGIN_USER_NOT_FOUND(1),
        LOGIN_INVALID_CREDENTIALS(2),
        LOGIN_INACTIVE_USER(3);

        private int code;

        LoginResultCode(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static LoginResultCode fromCode(int code) {
            for (LoginResultCode value : LoginResultCode.values()) {
                if (value.code == code) {
                    return value;
                }
            }
            throw new IllegalArgumentException("Codigo no válido: " + code);
        }


    }
}