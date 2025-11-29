package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contratos")
public class ContratosController {

    @GetMapping("/nuevo")
    public String nuevoContrato(Model model) {
        model.addAttribute("content", "contratos/nuevo :: content");
        return "principal";
    }

    @GetMapping("/listado")
    public String listadoContratos(Model model) {
        model.addAttribute("content", "contratos/listado :: content");
        return "principal";
    }
}
