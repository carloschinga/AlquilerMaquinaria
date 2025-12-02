package com.example.alquilermaquinaria.Controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.PagoChoferService;
import com.example.alquilermaquinaria.dto.PagoChoferDTO;
import com.example.alquilermaquinaria.entity.Chofer;
import com.example.alquilermaquinaria.entity.PagoChofer;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos-choferes")
public class PagoChoferController {

    private final PagoChoferService service;

    public PagoChoferController(PagoChoferService service) {
        this.service = service;
    }

    @GetMapping
    public List<PagoChofer> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PagoChofer obtener(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PagoChofer crear(@Valid @RequestBody PagoChoferDTO dto) {

        PagoChofer pago = new PagoChofer();

        Chofer c = new Chofer();
        c.setChoferId(dto.getChoferId());

        pago.setChofer(c);
        pago.setFechaPago(LocalDateTime.parse(dto.getFechaPago()));
        pago.setMontoPagado(dto.getMontoPagado());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setDescripcion(dto.getDescripcion());
        pago.setEstado(dto.getEstado());

        return service.create(pago);
    }

    @PutMapping("/{id}")
    public PagoChofer actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PagoChoferDTO dto) {

        PagoChofer pago = new PagoChofer();

        Chofer c = new Chofer();
        c.setChoferId(dto.getChoferId());

        pago.setChofer(c);
        pago.setFechaPago(LocalDateTime.parse(dto.getFechaPago()));
        pago.setMontoPagado(dto.getMontoPagado());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setDescripcion(dto.getDescripcion());
        pago.setEstado(dto.getEstado());

        return service.update(id, pago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.delete(id);
    }
}
