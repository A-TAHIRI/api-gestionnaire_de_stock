package com.tahiri.gestiondestock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@SpringBootApplication
public class GestionDeStockApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionDeStockApplication.class, args);
	}

}
