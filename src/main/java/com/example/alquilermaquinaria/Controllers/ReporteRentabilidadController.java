package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Repository.ReporteRentabilidadRepository;

@RestController
@RequestMapping("/api/reportes/rentabilidad")
public class ReporteRentabilidadController {

    private final ReporteRentabilidadRepository repo;

    public ReporteRentabilidadController(ReporteRentabilidadRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Object[]> obtenerReporteRentabilidad() {
        return repo.reporteRentabilidadPorContrato();
    }
}
