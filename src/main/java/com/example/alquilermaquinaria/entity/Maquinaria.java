package com.example.alquilermaquinaria.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Maquinaria")
public class Maquinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maquinaId;

    private String tipo;
    private String modelo;
    private String serialInterno;
    private Double horasAcumuladas;
    private String estado;

    @OneToMany(mappedBy = "maquinaria")
    private List<ContratoAlquiler> contratos;

    @OneToMany(mappedBy = "maquinaria")
    private List<CargaCombustible> cargasCombustible;
}
