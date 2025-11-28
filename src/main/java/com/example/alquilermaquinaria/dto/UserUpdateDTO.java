package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nombreCompleto;
    private String correoElectronico;
    // No se incluye nombreUsuario ni contrasena para evitar cambios accidentales
}