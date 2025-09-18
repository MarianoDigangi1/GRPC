package com.sistemas.distribuidos.grpc_gateway.configuration;

import com.sistemas.distribuidos.grpc_gateway.security.CustomAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthProvider customAuthProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz
                        // Vistas públicas y recursos estáticos
                        .requestMatchers("/", "/login", "/logout", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // Sección de vistas de usuarios (solo PRESIDENTE)
                        .requestMatchers("/usuarios/**").hasRole("PRESIDENTE")
                        // Rutas protegidas por rol (usarán sesión)
                        .requestMatchers("/api/usuarios/crear").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/modificar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/eliminar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/eventos/crear").hasAnyRole("PRESIDENTE", "COORDINADOR")
                        .anyRequest().authenticated()
                )
                // CSRF habilitado por defecto. Si necesitas deshabilitarlo para ciertos endpoints, configúralo aquí.
                .csrf(Customizer.withDefaults())
                // Autenticación con formulario
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("identificador")
                        .passwordParameter("clave")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Usar nuestro AuthenticationProvider personalizado
                .authenticationProvider(customAuthProvider)
                .build();
    }
}
