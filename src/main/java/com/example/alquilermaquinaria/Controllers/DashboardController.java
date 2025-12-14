package com.example.alquilermaquinaria.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Repository.ChoferRepository;
import com.example.alquilermaquinaria.Repository.MaquinariaRepository;
import com.example.alquilermaquinaria.Repository.ReportesChoferesRepository;
import com.example.alquilermaquinaria.Services.ContratoAlquilerService;
import com.example.alquilermaquinaria.Services.ReportesService;
import com.example.alquilermaquinaria.dto.DashboardContratosDTO;
import com.example.alquilermaquinaria.dto.DashboardMaquinariaDTO;
import com.example.alquilermaquinaria.dto.DashboardProveedorPagoDTO;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final ContratoAlquilerService contratoService;
    private final MaquinariaRepository maquinariaRepo;
    private final ChoferRepository choferRepository;
    private final ReportesChoferesRepository reportesChoferesRepository;
    private final ReportesService reportesService;

    public DashboardController(ChoferRepository choferRepository, ContratoAlquilerService contratoService, MaquinariaRepository maquinariaRepo, ReportesChoferesRepository reportesChoferesRepository, ReportesService reportesService) {
        this.choferRepository = choferRepository;
        this.contratoService = contratoService;
        this.maquinariaRepo = maquinariaRepo;
        this.reportesChoferesRepository = reportesChoferesRepository;
        this.reportesService = reportesService;
    }

    @GetMapping("/contratos")
    public ResponseEntity<DashboardContratosDTO> resumenContratos() {
        return ResponseEntity.ok(contratoService.obtenerResumenContratos());
    }

    @GetMapping("/maquinaria")
    public DashboardMaquinariaDTO getMaquinariaDashboard() {

        Long total = maquinariaRepo.totalMaquinas();
        Long operativas = maquinariaRepo.maquinasOperativas();
        Long mantenimiento = maquinariaRepo.maquinasEnMantenimiento();
        Long inactivas = maquinariaRepo.maquinasInactivas();
        Double horas = maquinariaRepo.horasTotales();

        List<Object[]> top = maquinariaRepo.top5Usadas();

        List<Map<String, Object>> topList = top.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("maquinaId", row[0]);
            map.put("modelo", row[1]);
            map.put("horas", row[2]);
            return map;
        }).toList();

        return new DashboardMaquinariaDTO(
                total,
                operativas,
                mantenimiento,
                inactivas,
                horas,
                topList
        );
    }

    @GetMapping("/choferes/total")
    public Long getTotalChoferes() {
        return choferRepository.count();
    }

    @GetMapping("/choferes/activos")
    public Long getChoferesActivos() {
        return choferRepository.countByEstado("Activo");
    }

    @GetMapping("/choferes/inactivos")
    public Long getChoferesInactivos() {
        return choferRepository.countByEstado("Inactivo");
    }

    @GetMapping("/choferes/pagos-pendientes")
    public List<Object[]> getPagosPendientesChoferes() {
        return reportesChoferesRepository.reportePagosPendientesChoferes();
    }

    @GetMapping("/cuentas-proveedores")
    public List<DashboardProveedorPagoDTO> cuentasPendientesProveedores() {
        return reportesService.obtenerCuentasProveedores();
    }

}
