package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PagoProveedorDTO {

    private Integer pagoProveedorId;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer proveedorId;

    @NotNull(message = "La fecha de pago es obligatoria")
    private String fechaPago; // ISO 8601

    @NotNull(message = "El monto pagado es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double montoPagado;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    private String descripcion;
    private String estado;
}
