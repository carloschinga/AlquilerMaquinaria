package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class DashboardContratosDTO {

    private Long totalContratos;
    private Long contratosActivos;
    private Long contratosPorCobrar;
    private Double montoPorCobrar;
}
