package com.example.alquilermaquinaria.entity;

import java.time.LocalDate;

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
@Table(name = "Asignacion_Operaciones")
public class AsignacionOperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer asignacionId;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private ContratoAlquiler contrato;

    @ManyToOne
    @JoinColumn(name = "chofer_id")
    private Chofer chofer;

    private LocalDate fechaTurno;
    private Double horasTrabajadas;
    private Double pagoCalculado;
    private String estadoPago;
}
