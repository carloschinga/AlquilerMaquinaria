package com.example.alquilermaquinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AlquilerMaquinariaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AlquilerMaquinariaApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AlquilerMaquinariaApplication.class);
	}
}