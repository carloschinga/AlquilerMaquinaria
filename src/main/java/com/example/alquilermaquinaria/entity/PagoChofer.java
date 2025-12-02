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
@Table(name = "Pagos_Choferes")
public class PagoChofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pagoChoferId;

    @ManyToOne
    @JoinColumn(name = "chofer_id")
    private Chofer chofer;

    private LocalDateTime fechaPago;

    private Double montoPagado;

    private String metodoPago; // efectivo, transferencia, etc.

    private String descripcion;

    private String estado; // pagado, pendiente, observado
}
