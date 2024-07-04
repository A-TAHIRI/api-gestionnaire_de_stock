package com.tahiri.gestiondestock.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfigurer   {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilter securityFilter(){
        return new SecurityFilter();
    }


    /**
     * Cette méthode configure la chaîne de filtres de sécurité pour l'application.
     * @param http l'objet HttpSecurity utilisé pour configurer la sécurité HTTP.
     * @return une instance de SecurityFilterChain configurée selon les paramètres spécifiés.
     * @throws Exception en cas de problème lors de la configuration de la sécurité HTTP.
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                // Désactive la protection CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)
                // Configure les autorisations pour les différentes requêtes HTTP
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.PUT, "/api/v1/update/password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/generateToken", "/register","/login").permitAll()
                        .requestMatchers("/api/v1/articles/**","/api/v1/clients/**","/api/v1/fournisseurs/**","/api/v1/commandesclient/**","/api/v1/commandesfournisseur/**")
                        .hasAnyAuthority( "ADMIN","USER")
                       .requestMatchers("/api/v1/utilisateurs","/api/v1/categories/**").hasAnyAuthority("ADMIN")
                        .anyRequest().permitAll()
                )
                // Active le support CORS (Cross-Origin Resource Sharing) avec les paramètres par défaut
                .cors(Customizer.withDefaults())
                .cors(Customizer.withDefaults())
                // Ajoute un filtre de sécurité personnalisé après le filtre UsernamePasswordAuthenticationFilter
                .addFilterAfter(securityFilter(), UsernamePasswordAuthenticationFilter.class)
                // Construit et retourne la chaîne de filtres de sécurité
                .build();
    }

    /**
     * Cette méthode configure la source de configuration CORS pour l'application.
     * @return une instance de CorsConfigurationSource configurée avec les paramètres spécifiés.
     */
    @Bean(name = "corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        // Création d'une nouvelle source de configuration CORS basée sur les URL
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Création d'une nouvelle configuration CORS
        CorsConfiguration config = new CorsConfiguration();
        // Ajout d'un en-tête autorisé spécifique
        config.addAllowedHeader("X-XSRF-TOKEN");
        // Définition des en-têtes autorisés
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        // Définition des méthodes HTTP autorisées
        config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "PATCH" ,"OPTIONS"));
        // Définition des origines autorisées
        config.setAllowedOrigins(List.of("http://localhost:4200","https://gestostock.fr"));
        // Autorisation de l'envoi des cookies de session
        config.setAllowCredentials(true);
        // Enregistrement de la configuration CORS pour toutes les URL
        source.registerCorsConfiguration("/**", config);
        // Retourne la source de configuration CORS configurée
        return source;
    }



}
