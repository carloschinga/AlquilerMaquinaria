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

import com.example.alquilermaquinaria.Services.MaquinariaService;
import com.example.alquilermaquinaria.dto.MaquinariaDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maquinarias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // opcional si usas fetch desde HTML
public class MaquinariaController {

    private final MaquinariaService maquinariaService;

    @GetMapping
    public ResponseEntity<List<MaquinariaDTO>> listar() {
        return ResponseEntity.ok(maquinariaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinariaDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(maquinariaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<MaquinariaDTO> registrar(@RequestBody MaquinariaDTO dto) {
        return ResponseEntity.ok(maquinariaService.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaquinariaDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody MaquinariaDTO dto
    ) {
        return ResponseEntity.ok(maquinariaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        maquinariaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
