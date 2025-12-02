package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.PagoProveedorRepository;
import com.example.alquilermaquinaria.entity.PagoProveedor;

@Service
public class PagoProveedorServiceImpl implements PagoProveedorService {

    private final PagoProveedorRepository repository;

    public PagoProveedorServiceImpl(PagoProveedorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PagoProveedor> findAll() {
        return repository.findAll();
    }

    @Override
    public PagoProveedor findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago de proveedor no encontrado"));
    }

    @Override
    public PagoProveedor create(PagoProveedor pago) {
        return repository.save(pago);
    }

    @Override
    public PagoProveedor update(Integer id, PagoProveedor pago) {
        PagoProveedor existente = findById(id);

        existente.setProveedor(pago.getProveedor());
        existente.setFechaPago(pago.getFechaPago());
        existente.setMontoPagado(pago.getMontoPagado());
        existente.setMetodoPago(pago.getMetodoPago());
        existente.setDescripcion(pago.getDescripcion());
        existente.setEstado(pago.getEstado());

        return repository.save(existente);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
