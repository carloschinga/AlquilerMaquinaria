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

import com.example.alquilermaquinaria.Services.ClienteService;
import com.example.alquilermaquinaria.dto.ClienteDTO;

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
    public List<ClienteDTO> listar() {
        return service.listar();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ClienteDTO buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody ClienteDTO dto) {
        try {
            ClienteDTO nuevo = service.guardarDesdeDTO(dto);
            return ResponseEntity.ok(nuevo);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un cliente con ese RUC/DNI");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ClienteDTO dto) {
        dto.setClienteId(id);
        try {
            ClienteDTO actualizado = service.guardarDesdeDTO(dto);
            return ResponseEntity.ok(actualizado);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Ya existe un cliente con ese RUC/DNI");
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.eliminar(id);
    }
}
