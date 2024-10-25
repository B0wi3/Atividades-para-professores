package com.atividades.bowie.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativa CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/register").permitAll() // Permite acesso ao endpoint de registro
                        .requestMatchers("/auth/login").permitAll() // Permite acesso ao endpoint de login
                        .anyRequest().authenticated() // Exige autenticação para todas as outras rotas
                );

        return http.build();
    }
}
