package com.example.alquilermaquinaria.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.alquilermaquinaria.Services.ClienteService;
import com.example.alquilermaquinaria.dto.ClienteDTO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", service.listar());
        model.addAttribute("content", "maestros/clientes :: content");
        return "principal";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clienteDTO", new ClienteDTO());
        model.addAttribute("content", "maestros/form_cliente :: content");
        return "principal";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("clienteDTO") ClienteDTO dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("content", "maestros/form_cliente :: content");
            return "principal";
        }

        service.guardarDesdeDTO(dto);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        var cliente = service.buscarPorId(id);
        if (cliente == null) {
            return "redirect:/clientes";
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setClienteId(cliente.getClienteId());
        dto.setNombre(cliente.getNombre());
        dto.setRucDni(cliente.getRucDni());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());

        model.addAttribute("clienteDTO", dto);
        model.addAttribute("content", "maestros/form_cliente :: content");
        return "principal";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/clientes";
    }
}
