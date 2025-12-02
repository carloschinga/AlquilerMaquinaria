package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AsignacionOperacionDTO {

    private Integer asignacionId;

    @NotNull(message = "El contrato es obligatorio")
    private Integer contratoId;

    @NotNull(message = "El chofer es obligatorio")
    private Integer choferId;

    @NotNull(message = "La fecha del turno es obligatoria")
    private String fechaTurno; // formato ISO (yyyy-MM-dd)

    @NotNull(message = "Las horas trabajadas son obligatorias")
    @Positive(message = "Las horas trabajadas deben ser mayor a 0")
    private Double horasTrabajadas;

    private Double pagoCalculado;

    private String estadoPago;
}
