package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.CargaCombustibleService;
import com.example.alquilermaquinaria.dto.CargaCombustibleDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cargas-combustible")
public class CargaCombustibleController {

    private final CargaCombustibleService service;

    public CargaCombustibleController(CargaCombustibleService service) {
        this.service = service;
    }

    @GetMapping
    public List<CargaCombustibleDTO> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CargaCombustibleDTO obtener(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public CargaCombustibleDTO crear(@Valid @RequestBody CargaCombustibleDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public CargaCombustibleDTO actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CargaCombustibleDTO dto) {

        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.delete(id);
    }
}
