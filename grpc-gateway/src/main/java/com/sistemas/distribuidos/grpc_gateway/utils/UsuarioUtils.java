package com.sistemas.distribuidos.grpc_gateway.utils;

import com.sistemas.distribuidos.grpc_gateway.filter.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsuarioUtils {
    public static CustomUserPrincipal getUsuarioLogueado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserPrincipal) auth.getPrincipal();
    }
}
