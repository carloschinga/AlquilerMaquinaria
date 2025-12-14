package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class PagosPendientesChoferDTO {

    private Integer choferId;
    private String nombreChofer;
    private Double montoPendientePago;
    private Integer numeroTurnosPendientes;
}
