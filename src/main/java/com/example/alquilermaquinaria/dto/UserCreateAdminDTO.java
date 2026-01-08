package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class UserCreateAdminDTO {
    private String nombreUsuario;
    private String nombreCompleto;
    private String correoElectronico;
    private Integer rolId; // ID del rol seleccionado en el formulario
}