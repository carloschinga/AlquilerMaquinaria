package com.example.alquilermaquinaria.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Cargas_Combustible")
public class CargaCombustible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cargaId;

    @ManyToOne
    @JoinColumn(name = "maquina_id")
    private Maquinaria maquinaria;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    private LocalDateTime fechaCarga;
    private Double litrosCargados;
    private Double costoTotal;
    private Double costoUnitario;
    private Double lecturaHorometro;

    private String facturaNum;
    private String estadoPago;
}
