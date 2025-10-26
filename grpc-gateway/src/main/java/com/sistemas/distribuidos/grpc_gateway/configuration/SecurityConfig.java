package com.sistemas.distribuidos.grpc_gateway.configuration;

import com.sistemas.distribuidos.grpc_gateway.security.CustomAuthProvider;
import com.sistemas.distribuidos.grpc_gateway.security.CustomAuthenticationFailureHandler;
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
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(authz -> authz
                    .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**", "/api/kafka/**").permitAll()
                    .requestMatchers("/usuarios/**").hasRole("PRESIDENTE")
                    .requestMatchers("/api/usuarios/crear").hasRole("PRESIDENTE")
                    .requestMatchers("/api/usuarios/modificar/**").hasRole("PRESIDENTE")
                    .requestMatchers("/api/usuarios/eliminar/**").hasRole("PRESIDENTE")
                    .requestMatchers("/api/eventos/crear").hasAnyRole("PRESIDENTE", "COORDINADOR")
                    .requestMatchers("/inventario/**").hasAnyRole("PRESIDENTE", "VOCAL")
                    .requestMatchers("/informes/**").hasAnyRole("PRESIDENTE", "VOCAL")
                    .anyRequest().authenticated()
            )
            // ✅ Mantiene protección CSRF (obligatoria para logout POST)
            .csrf(Customizer.withDefaults())
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("identificador")
                    .passwordParameter("clave")
                    .defaultSuccessUrl("/", true)
                    .failureHandler(customAuthenticationFailureHandler)
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout")                    // endpoint del form
                    .logoutSuccessUrl("/login?logout=true")  // mensaje de sesión cerrada
                    .invalidateHttpSession(true)             // invalida la sesión
                    .deleteCookies("JSESSIONID")             // borra la cookie
                    .permitAll()
            )
            .authenticationProvider(customAuthProvider)
            .build();
}

}
