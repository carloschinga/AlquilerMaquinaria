package com.example.alquilermaquinaria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {

    private Integer clienteId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    private String nombre;

    @NotBlank(message = "El RUC/DNI es obligatorio")
    @Size(max = 20)
    private String rucDni;

    @Size(max = 20)
    private String telefono;

    @Size(max = 255)
    private String direccion;

    public ClienteDTO() {
    }

}
