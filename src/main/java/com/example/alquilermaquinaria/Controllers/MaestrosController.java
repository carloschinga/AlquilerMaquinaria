package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maestros")
public class MaestrosController {

    @GetMapping("/clientes")
    public String clientes(Model model) {
        model.addAttribute("content", "maestros/clientes :: content");
        return "principal";
    }

    @GetMapping("/maquinarias")
    public String maquinarias(Model model) {
        model.addAttribute("content", "maestros/maquinarias :: content");
        return "principal";
    }

    @GetMapping("/choferes")
    public String choferes(Model model) {
        model.addAttribute("content", "maestros/choferes :: content");
        return "principal";
    }

    @GetMapping("/proveedores")
    public String proveedores(Model model) {
        model.addAttribute("content", "maestros/proveedores :: content");
        return "principal";
    }
}
