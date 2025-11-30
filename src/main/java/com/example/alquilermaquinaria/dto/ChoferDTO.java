package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChoferDTO {

    private Integer choferId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @NotNull(message = "La tarifa por hora es obligatoria")
    private Double tarifaHora;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
