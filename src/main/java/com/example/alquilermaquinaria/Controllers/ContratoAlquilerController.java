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

import com.example.alquilermaquinaria.Services.ContratoAlquilerService;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContratoAlquilerController {

    private final ContratoAlquilerService contratoService;

    @GetMapping
    public ResponseEntity<List<ContratoAlquiler>> listar() {
        return ResponseEntity.ok(contratoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoAlquiler> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(contratoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ContratoAlquiler> registrar(@RequestBody ContratoAlquiler contrato) {
        return ResponseEntity.ok(contratoService.create(contrato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoAlquiler> actualizar(
            @PathVariable Integer id,
            @RequestBody ContratoAlquiler contrato
    ) {
        return ResponseEntity.ok(contratoService.update(id, contrato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contratoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
