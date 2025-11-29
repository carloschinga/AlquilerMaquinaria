package com.example.alquilermaquinaria.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clienteId;

    private String nombre;
    private String rucDni;
    private String telefono;
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    private List<ContratoAlquiler> contratos;
}
