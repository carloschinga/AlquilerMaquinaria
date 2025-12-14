package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.ReportesChoferesService;
import com.example.alquilermaquinaria.dto.PagosPendientesChoferDTO;

@RestController
@RequestMapping("/api/reportes/choferes")
public class ReportesChoferesController {

    private final ReportesChoferesService service;

    public ReportesChoferesController(ReportesChoferesService service) {
        this.service = service;
    }

    @GetMapping("/pagos-pendientes")
    public List<PagosPendientesChoferDTO> obtenerReporte() {
        return service.obtenerPagosPendientes();
    }
}
