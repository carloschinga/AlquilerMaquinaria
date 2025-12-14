package com.example.alquilermaquinaria.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardMaquinariaDTO {

    private Long totalMaquinas;
    private Long maquinasOperativas;
    private Long maquinasEnMantenimiento;
    private Long maquinasInactivas;
    private Double horasTotalesAcumuladas;
    private List<Map<String, Object>> top5MasUsadas;

    public DashboardMaquinariaDTO(Double horasTotalesAcumuladas, Long maquinasEnMantenimiento, Long maquinasInactivas, Long maquinasOperativas, List<Map<String, Object>> top5MasUsadas, Long totalMaquinas) {
        this.horasTotalesAcumuladas = horasTotalesAcumuladas;
        this.maquinasEnMantenimiento = maquinasEnMantenimiento;
        this.maquinasInactivas = maquinasInactivas;
        this.maquinasOperativas = maquinasOperativas;
        this.top5MasUsadas = top5MasUsadas;
        this.totalMaquinas = totalMaquinas;
    }

}
