package com.example.alquilermaquinaria.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.AsignacionOperacionService;
import com.example.alquilermaquinaria.dto.AsignacionOperacionDTO;
import com.example.alquilermaquinaria.entity.AsignacionOperacion;
import com.example.alquilermaquinaria.entity.Chofer;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/asignaciones-operacion")
public class AsignacionOperacionController {

    private final AsignacionOperacionService service;

    public AsignacionOperacionController(AsignacionOperacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<AsignacionOperacion> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public AsignacionOperacion obtener(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public AsignacionOperacion crear(@Valid @RequestBody AsignacionOperacionDTO dto) {

        AsignacionOperacion asignacion = new AsignacionOperacion();

        ContratoAlquiler contrato = new ContratoAlquiler();
        contrato.setContratoId(dto.getContratoId());

        Chofer chofer = new Chofer();
        chofer.setChoferId(dto.getChoferId());

        asignacion.setContrato(contrato);
        asignacion.setChofer(chofer);
        asignacion.setFechaTurno(LocalDate.parse(dto.getFechaTurno()));
        asignacion.setHorasTrabajadas(dto.getHorasTrabajadas());
        asignacion.setPagoCalculado(dto.getPagoCalculado());
        asignacion.setEstadoPago(dto.getEstadoPago());

        return service.create(asignacion);
    }

    @PutMapping("/{id}")
    public AsignacionOperacion actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AsignacionOperacionDTO dto) {

        AsignacionOperacion asignacion = new AsignacionOperacion();

        ContratoAlquiler contrato = new ContratoAlquiler();
        contrato.setContratoId(dto.getContratoId());

        Chofer chofer = new Chofer();
        chofer.setChoferId(dto.getChoferId());

        asignacion.setContrato(contrato);
        asignacion.setChofer(chofer);
        asignacion.setFechaTurno(LocalDate.parse(dto.getFechaTurno()));
        asignacion.setHorasTrabajadas(dto.getHorasTrabajadas());
        asignacion.setPagoCalculado(dto.getPagoCalculado());
        asignacion.setEstadoPago(dto.getEstadoPago());

        return service.update(id, asignacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.delete(id);
    }
}
