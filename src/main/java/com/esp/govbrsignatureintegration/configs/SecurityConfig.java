package com.esp.govbrsignatureintegration.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Desativa CSRF (opcional para APIs REST)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/login", "/assinar/**", "/assinar/lote/**").permitAll() // Permite acesso público ao Swagger
                        .anyRequest().authenticated() // Requer autenticação para outros endpoints
                );
        return http.build();
    }
}
