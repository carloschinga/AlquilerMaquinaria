package com.example.alquilermaquinaria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardProveedorPagoDTO {

    private Integer proveedorId;
    private String nombreProveedor;
    private Double montoPendiente;
    private Integer numeroFacturasPendientes;
}
