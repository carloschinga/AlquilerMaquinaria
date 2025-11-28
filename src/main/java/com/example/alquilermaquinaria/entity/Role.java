package com.example.alquilermaquinaria.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer id;

    @Column(name = "nombre_rol", unique = true, nullable = false, length = 50)
    private String nombreRol;

    @Column(name = "descripcion", length = 255)
    private String descripcion;
}