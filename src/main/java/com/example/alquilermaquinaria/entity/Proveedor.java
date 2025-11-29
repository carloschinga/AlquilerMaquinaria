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
@Table(name = "Proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proveedorId;

    private String nombre;
    private String ruc;
    private String contacto;

    @OneToMany(mappedBy = "proveedor")
    private List<CargaCombustible> cargasCombustible;
}
