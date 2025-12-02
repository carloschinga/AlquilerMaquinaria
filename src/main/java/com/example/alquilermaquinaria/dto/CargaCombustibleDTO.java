package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CargaCombustibleDTO {

    private Integer cargaId;

    @NotNull(message = "La maquinaria es obligatoria")
    private Integer maquinariaId;

    @NotNull(message = "El proveedor es obligatorio")
    private Integer proveedorId;

    @NotNull(message = "La fecha es obligatoria")
    private String fechaCarga; // formato ISO (String)

    @NotNull(message = "Los litros son obligatorios")
    @Positive(message = "Los litros deben ser mayores a 0")
    private Double litrosCargados;

    @NotNull(message = "El costo total es obligatorio")
    @Positive(message = "El costo total debe ser mayor a 0")
    private Double costoTotal;

    @NotNull(message = "El costo unitario es obligatorio")
    @Positive(message = "El costo unitario debe ser mayor a 0")
    private Double costoUnitario;

    @NotNull(message = "La lectura de horómetro es obligatoria")
    private Double lecturaHorometro;

    private String facturaNum;
    private String estadoPago;
}
