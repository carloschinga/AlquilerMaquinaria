package com.example.alquilermaquinaria.Controllers;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.ContratoAlquilerService;
import com.example.alquilermaquinaria.dto.ContratoAlquilerDTO;
import com.example.alquilermaquinaria.dto.ContratoCreateRequestDTO;
import com.example.alquilermaquinaria.dto.UtilizacionDTO;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContratoAlquilerController {

    private final ContratoAlquilerService contratoService;

    @GetMapping
    public ResponseEntity<List<ContratoAlquilerDTO>> listar() {
        return ResponseEntity.ok(contratoService.findAllDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoAlquilerDTO> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(contratoService.findDTOById(id));
    }

    @PostMapping
    public ResponseEntity<ContratoAlquilerDTO> registrar(@RequestBody ContratoAlquiler contrato) {
        return ResponseEntity.ok(contratoService.createDTO(contrato));
    }

    @PostMapping("/crear")
    public ResponseEntity<ContratoAlquilerDTO> crearContratoConChofer(
            @RequestBody ContratoCreateRequestDTO dto) {

        return ResponseEntity.ok(contratoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoAlquilerDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody ContratoAlquilerDTO contrato
    ) {
        return ResponseEntity.ok(contratoService.updateDTO(id, contrato));
    }

    @PutMapping("/cerrar/{id}")
    public ResponseEntity<ContratoAlquilerDTO> cerrarContrato(
            @PathVariable Integer id,
            @RequestBody ContratoAlquiler contratoCierre) {
        return ResponseEntity.ok(contratoService.cerrarContrato(id, contratoCierre));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        contratoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utilizacion")
    public List<UtilizacionDTO> obtenerUtilizacion(
            @RequestParam String inicio,
            @RequestParam String fin) {

        // Validación simple por si viene vacío
        if (inicio == null || fin == null || inicio.isBlank() || fin.isBlank()) {
            throw new IllegalArgumentException("Las fechas 'inicio' y 'fin' son obligatorias.");
        }

        LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
        LocalDateTime fechaFin = LocalDateTime.parse(fin);

        // Usar dia completo
        LocalDateTime inicioDia = fechaInicio.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fechaFin.toLocalDate().atTime(23, 59, 59);

        return contratoService.obtenerUtilizacion(inicioDia, finDia);
    }

}
