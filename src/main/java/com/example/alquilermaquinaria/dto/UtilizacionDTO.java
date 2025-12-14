package com.example.alquilermaquinaria.dto;

import lombok.Data;

@Data
public class UtilizacionDTO {

    private Integer maquinaId;
    private Long horasUsadas;
    private Long cantidadContratos;
    private String modelo;
}
