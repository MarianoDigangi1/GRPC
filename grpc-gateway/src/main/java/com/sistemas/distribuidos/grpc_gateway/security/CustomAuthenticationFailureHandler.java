package com.sistemas.distribuidos.grpc_gateway.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Credenciales invalidas";

        if (exception instanceof BadCredentialsException) {
            errorMessage = exception.getMessage();
        }

        setDefaultFailureUrl("/login?error=true&message=" + errorMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
