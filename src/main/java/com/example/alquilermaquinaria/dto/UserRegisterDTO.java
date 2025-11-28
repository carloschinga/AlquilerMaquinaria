package com.example.alquilermaquinaria.dto;


import lombok.Data;

@Data
public class UserRegisterDTO {
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasena; // Contraseña plana que se hasheará
    private String correoElectronico;
    private Integer rolId; // El ID del rol que se asignará
}