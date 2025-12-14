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

import com.example.alquilermaquinaria.Services.ChoferService;
import com.example.alquilermaquinaria.entity.Chofer;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/choferes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChoferController {

    private final ChoferService choferService;

    @GetMapping
    public ResponseEntity<List<Chofer>> listar() {
        return ResponseEntity.ok(choferService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chofer> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(choferService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Chofer chofer) {
        try {
            Chofer nuevo = choferService.create(chofer);
            return ResponseEntity.ok(nuevo);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un chofer con esa identificación");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody Chofer chofer
    ) {
        try {
            Chofer actualizado = choferService.update(id, chofer);
            return ResponseEntity.ok(actualizado);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un chofer con esa identificación");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        choferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
