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

import com.example.alquilermaquinaria.Services.PagoProveedorService;
import com.example.alquilermaquinaria.dto.PagoProveedorDTO;
import com.example.alquilermaquinaria.entity.PagoProveedor;
import com.example.alquilermaquinaria.entity.Proveedor;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos-proveedores")
public class PagoProveedorController {

    private final PagoProveedorService service;

    public PagoProveedorController(PagoProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public List<PagoProveedor> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PagoProveedor obtener(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public PagoProveedor crear(@Valid @RequestBody PagoProveedorDTO dto) {

        PagoProveedor pago = new PagoProveedor();

        Proveedor p = new Proveedor();
        p.setProveedorId(dto.getProveedorId());

        pago.setProveedor(p);
        pago.setFechaPago(LocalDateTime.parse(dto.getFechaPago()));
        pago.setMontoPagado(dto.getMontoPagado());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setDescripcion(dto.getDescripcion());
        pago.setEstado(dto.getEstado());

        return service.create(pago);
    }

    @PutMapping("/{id}")
    public PagoProveedor actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PagoProveedorDTO dto) {

        PagoProveedor pago = new PagoProveedor();

        Proveedor p = new Proveedor();
        p.setProveedorId(dto.getProveedorId());

        pago.setProveedor(p);
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
