package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/contratos")
    public String dashboardContratos(Model model) {
        model.addAttribute("content", "dashboard/contratos :: content");
        return "principal";
    }

    @GetMapping("/maquinarias-disponibles")
    public String maquinariasDisponibles(Model model) {
        model.addAttribute("content", "dashboard/maquinarias-disponibles :: content");
        return "principal";
    }

    @GetMapping("/cuentas-por-cobrar")
    public String cuentasPorCobrar(Model model) {
        model.addAttribute("content", "dashboard/cuentas-por-cobrar :: content");
        return "principal";
    }

    @GetMapping("/pagos-pendientes")
    public String pagosPendientes(Model model) {
        model.addAttribute("content", "dashboard/pagos-pendientes :: content");
        return "principal";
    }
}
