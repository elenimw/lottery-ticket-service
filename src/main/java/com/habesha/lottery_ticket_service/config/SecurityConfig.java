package com.habesha.lottery_ticket_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/lottery/login", "/lottery/login/**", "/lottery/player/register", "/lottery/player/register/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/lottery/player/register", "/lottery/login").permitAll()
                        .requestMatchers("/lottery/tickets/purchase", "/lottery/tickets/player").authenticated()
                        .requestMatchers("/lottery/tickets/draw/**").hasRole("ADMIN").anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for testing
        return http.build();
    }


}