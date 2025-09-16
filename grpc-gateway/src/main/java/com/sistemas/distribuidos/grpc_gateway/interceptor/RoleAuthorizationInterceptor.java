package com.sistemas.distribuidos.grpc_gateway.interceptor;

import com.sistemas.distribuidos.grpc_gateway.annotation.RequireRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;

@Component
public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
            
            if (requireRole != null) {
                String requiredRole = requireRole.value();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication == null || !authentication.isAuthenticated()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"No autorizado\",\"message\":\"Token de autenticación requerido\"}");
                    return false;
                }
                
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                boolean hasRequiredRole = authorities.stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + requiredRole.toUpperCase()));
                
                if (!hasRequiredRole) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Sin autorización\",\"message\":\"No tienes permisos para realizar esta acción. Se requiere rol: " + requiredRole + "\"}");
                    return false;
                }
            }
        }
        return true;
    }
}
