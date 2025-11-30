package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MaquinariaDTO {

    private Integer maquinaId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotBlank(message = "El serial interno es obligatorio")
    private String serialInterno;

    @NotNull(message = "Las horas acumuladas son obligatorias")
    private Double horasAcumuladas;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
