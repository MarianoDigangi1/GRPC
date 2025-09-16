package com.sistemas.distribuidos.grpc_gateway.configuration;

import com.sistemas.distribuidos.grpc_gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( (authz) -> authz
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/usuarios/crear").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/modificar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/eliminar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/**").permitAll() // Para otras operaciones como consultar
                        .anyRequest().authenticated())
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
