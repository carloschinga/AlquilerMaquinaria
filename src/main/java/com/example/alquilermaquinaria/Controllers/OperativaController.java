package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/operativa")
public class OperativaController {

    @GetMapping("/carga-combustible")
    public String cargaCombustible(Model model) {
        model.addAttribute("content", "operativa/carga-combustible :: content");
        return "principal";
    }

    @GetMapping("/pago-proveedores")
    public String pagoProveedores(Model model) {
        model.addAttribute("content", "operativa/pago-proveedores :: content");
        return "principal";
    }

    @GetMapping("/pago-choferes")
    public String pagoChoferes(Model model) {
        model.addAttribute("content", "operativa/pago-choferes :: content");
        return "principal";
    }
}
