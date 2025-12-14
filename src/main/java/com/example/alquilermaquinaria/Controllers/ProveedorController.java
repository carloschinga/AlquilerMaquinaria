package com.example.alquilermaquinaria.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.ProveedorService;
import com.example.alquilermaquinaria.dto.ProveedorDTO;
import com.example.alquilermaquinaria.entity.Proveedor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listar() {
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(proveedorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Proveedor proveedor) {
        try {
            Proveedor nuevo = proveedorService.create(proveedor);
            return ResponseEntity.ok(nuevo);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un proveedor con ese RUC");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody Proveedor proveedor
    ) {
        try {
            Proveedor actualizado = proveedorService.update(id, proveedor);
            return ResponseEntity.ok(actualizado);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un proveedor con ese RUC");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        proveedorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
