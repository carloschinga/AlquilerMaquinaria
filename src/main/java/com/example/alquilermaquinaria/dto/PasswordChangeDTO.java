package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String nombreUsuario;
    private String contrasenaActual;
    private String nuevaContrasena;
}