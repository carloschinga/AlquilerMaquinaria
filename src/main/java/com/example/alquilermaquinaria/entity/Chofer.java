package com.example.alquilermaquinaria.entity;

import java.util.ArrayList;
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
@Table(name = "Choferes")
public class Chofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choferId;

    private String nombre;
    private String identificacion;
    private Double tarifaHora;
    private String estado;

    @OneToMany(mappedBy = "chofer")
    @JsonManagedReference("chofer-asignaciones")
    private List<AsignacionOperacion> asignaciones = new ArrayList<>();

}
