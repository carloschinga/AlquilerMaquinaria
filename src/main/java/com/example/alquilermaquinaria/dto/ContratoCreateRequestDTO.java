package com.example.alquilermaquinaria.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContratoCreateRequestDTO {

    private Integer clienteId;
    private Integer maquinaId;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinEstimada;

    private Double horometroInicial;
    private Double tarifaAplicada;
    private String condicionEntrega;
    private String estadoPago;

    // CAMPOS SOLO PARA LA ASIGNACIÓN INICIAL
    private Integer choferId;            // opcional
    private Double horasIniciales;       // opcional
    private String fechaTurnoInicial;    // opcional
}
