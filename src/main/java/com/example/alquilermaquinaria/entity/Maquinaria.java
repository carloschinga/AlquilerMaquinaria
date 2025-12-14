package com.example.alquilermaquinaria.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @JsonManagedReference("maquina-carga")
    private List<CargaCombustible> cargasCombustible;
}
