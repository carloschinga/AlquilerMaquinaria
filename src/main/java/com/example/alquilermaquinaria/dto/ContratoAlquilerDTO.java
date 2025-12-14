package com.example.alquilermaquinaria.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContratoAlquilerDTO {

    private Integer contratoId;

    private Integer clienteId;
    private String clienteNombre;

    private Integer maquinaId;
    private String maquinaModelo;
    private String maquinaTipo;
    private String maquinaEstado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinEstimada;
    private LocalDateTime fechaFinReal;

    private Double horometroInicial;
    private Double horometroFinal;
    private Double tiempoUsoHrs;
    private Double tarifaAplicada;
    private Double montoTotalCobrar;
    private String estadoPago;
    private String condicionEntrega;
    private String condicionDevolucion;

    private Integer choferId;
    private String choferEstado;
    private String choferNombre;             // Para mostrar en tabla
    private Double choferTarifaHora;
    private Double horasIniciales;
    private String fechaTurnoInicial;

}
