package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Repository.ReportesRepository;

@RestController
@RequestMapping("/api/reportes/proveedores")
public class ReportesProveedoresController {

    private final ReportesRepository repo;

    public ReportesProveedoresController(ReportesRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/cuentas-por-pagar")
    public List<Object[]> obtenerReporte() {
        return repo.reporteCuentaProveedores();
    }
}
