package com.tahiri.gestiondestock.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilter securityFilter(){
        return new SecurityFilter();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/generateToken", "/register","/login").permitAll() // pour le generateToken pour toute les personnes ont accées
                        .requestMatchers("/api/v1/articles/**","/api/v1/clients/**","/api/v1/fournisseurs/**","/api/v1/commandesclient/**","/api/v1/commandesfournisseur/**").hasAnyAuthority( "ADMIN","USER") // seulement pour les ADMIN et USER
                       .requestMatchers("/api/v1/utilisateurs/**","/api/v1/categories/**").hasAnyAuthority("ADMIN") // seulement pour les ADMIN
                        .anyRequest().permitAll()


                )

                // avant chaque request on va executer ce filtre
                .addFilterAfter(securityFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }





}
