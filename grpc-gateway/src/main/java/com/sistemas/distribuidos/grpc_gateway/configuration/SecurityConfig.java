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
                        .requestMatchers("/login", "/logout", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/usuarios/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/crear").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/modificar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/usuarios/eliminar/**").hasRole("PRESIDENTE")
                        .requestMatchers("/api/eventos/crear").hasAnyRole("PRESIDENTE", "COORDINADOR")
                        .anyRequest().authenticated()
                )
                .csrf(Customizer.withDefaults())
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
                .authenticationProvider(customAuthProvider)
                .build();
    }
}
