package com.example.alquilermaquinaria.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Contratos_Alquiler")
public class ContratoAlquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contratoId;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "maquina_id")
    private Maquinaria maquinaria;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinEstimada;
    private LocalDateTime fechaFinReal;

    private Double horometroInicial;
    private Double horometroFinal;
    private Double tiempoUsoHrs;

    private Double tarifaAplicada;
    private Double montoTotalCobrar;

    private String estadoPago;
    private String condicionEntrega;
    private String condicionDevolucion;

    @OneToMany(mappedBy = "contrato")
    private List<AsignacionOperacion> asignaciones;
}
