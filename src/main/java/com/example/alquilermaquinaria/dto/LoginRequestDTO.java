package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String nombreUsuario;
    private String contrasena; // Contraseña plana para la verificación
}