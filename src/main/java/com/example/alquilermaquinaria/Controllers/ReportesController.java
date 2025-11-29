package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReportesController {

    @GetMapping("/rentabilidad")
    public String rentabilidad(Model model) {
        model.addAttribute("content", "reportes/rentabilidad :: content");
        return "principal";
    }

    @GetMapping("/utilizacion-maquinaria")
    public String usoMaquinaria(Model model) {
        model.addAttribute("content", "reportes/utilizacion-maquinaria :: content");
        return "principal";
    }

    @GetMapping("/deuda-pendiente")
    public String deudaPendiente(Model model) {
        model.addAttribute("content", "reportes/deuda-pendiente :: content");
        return "principal";
    }
}
