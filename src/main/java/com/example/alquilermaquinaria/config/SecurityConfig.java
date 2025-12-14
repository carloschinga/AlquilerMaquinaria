package com.example.alquilermaquinaria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.alquilermaquinaria.Services.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager para login manual
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                // Páginas públicas
                .requestMatchers("/index.html", "/api/users/login", "/api/users/register", "/api/users/me").permitAll()
                // Páginas protegidas
                .requestMatchers("/principal.html").authenticated()
                // Endpoints compartidos por todos los logueados
                .requestMatchers("/api/clientes/**", "/api/maquinarias/**", "/api/proveedores/**", "/api/choferes/**")
                .hasAnyRole("OPERADOR_ALQUILER", "OPERADOR_PATIO", "GERENTE", "CONTABILIDAD", "ADMINISTRADOR")
                // Endpoints específicos por rol, incluyendo ADMINISTRADOR
                .requestMatchers("/api/reportes/**", "/api/contratos/utilizacion")
                .hasAnyRole("GERENTE", "CONTABILIDAD", "ADMINISTRADOR")
                .requestMatchers("/api/contratos/**", "/api/asignaciones-operacion/**")
                .hasAnyRole("OPERADOR_ALQUILER", "ADMINISTRADOR")
                .requestMatchers("/api/cargas-combustible/**", "/api/pagos-proveedores/**", "/api/pagos-choferes/**", "/api/pagos-pendientes/**")
                .hasAnyRole("OPERADOR_PATIO", "CONTABILIDAD", "ADMINISTRADOR")
                .requestMatchers("/api/pagos-chofer/**", "/api/pagos-clientes/**")
                .hasAnyRole("CONTABILIDAD", "ADMINISTRADOR")
                // Cualquier otra request requiere login
                .anyRequest().authenticated()
                )
                // FormLogin para manejar sesión
                .formLogin(form -> form
                .loginPage("/index.html")
                .loginProcessingUrl("/api/users/login")
                .defaultSuccessUrl("/principal.html", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/api/users/logout")
                .logoutSuccessUrl("/index.html")
                .permitAll()
                );

        return http.build();
    }
}
