package com.example.alquilermaquinaria.config;

import com.example.alquilermaquinaria.Repository.RoleRepository;
import com.example.alquilermaquinaria.Repository.UserRepository;
import com.example.alquilermaquinaria.Services.UserService;
import com.example.alquilermaquinaria.dto.UserCreateAdminDTO;
import com.example.alquilermaquinaria.entity.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository,
                                   UserService userService,
                                   UserRepository userRepository) {
        return args -> {
            // 1. Crear Roles si no existen
            List<String> rolesNombres = Arrays.asList(
                    "ADMINISTRADOR",
                    "GERENTE",
                    "CONTABILIDAD",
                    "OPERADOR_PATIO",
                    "OPERADOR_ALQUILER",
                    "SUPER_ADMINISTRADOR"
            );

            for (String nombreRol : rolesNombres) {
                if (roleRepository.findAll().stream().noneMatch(r -> r.getNombreRol().equals(nombreRol))) {
                    Role rol = new Role();
                    rol.setNombreRol(nombreRol);
                    rol.setDescripcion("Rol de sistema " + nombreRol);
                    roleRepository.save(rol);
                    System.out.println(">> Rol creado: " + nombreRol);
                }
            }

            // 2. Crear Usuario Admin por defecto si no existe
            if (userRepository.findByNombreUsuario("admin").isEmpty()) {
                // Buscamos el rol de ADMINISTRADOR que acabamos de asegurar que existe
                Role adminRole = roleRepository.findAll().stream()
                        .filter(r -> r.getNombreRol().equals("ADMINISTRADOR"))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Error fatal: Rol ADMINISTRADOR no encontrado"));

                UserCreateAdminDTO adminDTO = new UserCreateAdminDTO();
                adminDTO.setNombreUsuario("admin");
                adminDTO.setNombreCompleto("Administrador del Sistema");
                adminDTO.setCorreoElectronico("admin@empresa.com");
                adminDTO.setRolId(adminRole.getId());

                // Usamos el servicio que ya programaste, el cual asigna la contraseña "1234"
                userService.crearUsuarioAdministrativo(adminDTO);

                System.out.println(">> Usuario ADMIN creado por defecto (User: admin / Pass: 1234)");
            }
        };
    }
}