package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String nombreCompleto;
    private String correoElectronico;
    private Integer rolId;
    private String nuevaContrasena;
    // No se incluye nombreUsuario para evitar cambios accidentales
}