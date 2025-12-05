package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Repository.ReportesChoferesRepository;

@RestController
@RequestMapping("/api/reportes/choferes")
public class ReportesChoferesController {

    private final ReportesChoferesRepository repo;

    public ReportesChoferesController(ReportesChoferesRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/pagos-pendientes")
    public List<Object[]> obtenerReporte() {
        return repo.reportePagosPendientesChoferes();
    }
}
