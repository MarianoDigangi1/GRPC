package com.sistemas.distribuidos.grpc_gateway.security;

import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UsuarioDto;
import com.sistemas.distribuidos.grpc_gateway.service.AuthService;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final AuthService authService;
    private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);

    @Autowired
    public CustomAuthProvider(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identificador = Objects.toString(authentication.getName(), "");
        String clave = Objects.toString(authentication.getCredentials(), "");

        LoginResponseDto response;
        try {
            response = authService.authenticate(new LoginRequestDto(identificador, clave));
        } catch (GrpcConnectionException ex) {
            log.error("Fallo de conexión gRPC durante autenticación: {}", ex.getMessage());
            throw new BadCredentialsException("No se pudo validar las credenciales (servicio de autenticación no disponible)");
        } catch (Exception ex) {
            log.error("Error inesperado durante autenticación", ex);
            throw new BadCredentialsException("Error interno durante la autenticación");
        }

        if (response == null) {
            log.warn("AuthService retornó null para usuario: {}", identificador);
            throw new BadCredentialsException("Credenciales inválidas");
        }

        if (response.getResult() != LoginResponseDto.LoginResultCode.LOGIN_OK) {
            log.info("Login rechazado para {}: result={} mensaje={} ", identificador, response.getResult(), response.getMensaje());
            throw new BadCredentialsException("Credenciales inválidas");
        }

        UsuarioDto usuario = response.getUsuario();
//        if (usuario == null || !usuario.isActivo()) {
//            throw new DisabledException("Usuario inactivo");
//        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (usuario.getRol() != null && !usuario.getRol().isBlank()) {
            // Normalizamos el rol: trim + upper
            String normalizedRole = usuario.getRol().trim().toUpperCase();
            // hasRole("PRESIDENTE") espera "ROLE_PRESIDENTE"
            authorities.add(new SimpleGrantedAuthority("ROLE_" + normalizedRole));
        }

        // Principal puede ser el nombre de usuario; podrías mapear un UserDetails si luego lo necesitas
        return new UsernamePasswordAuthenticationToken(usuario.getNombreUsuario(), null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
