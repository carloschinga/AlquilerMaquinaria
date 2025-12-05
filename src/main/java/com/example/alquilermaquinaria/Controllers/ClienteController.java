package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.ClienteService;
import com.example.alquilermaquinaria.dto.ClienteDTO;
import com.example.alquilermaquinaria.entity.Cliente;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    // LISTAR
    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Cliente guardar(@RequestBody ClienteDTO dto) {
        return service.guardarDesdeDTO(dto);
    }

    // EDITAR
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        dto.setClienteId(id);
        return service.guardarDesdeDTO(dto);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
