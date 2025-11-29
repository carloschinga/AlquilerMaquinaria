package com.example.alquilermaquinaria.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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
    private List<AsignacionOperacion> asignaciones;
}
