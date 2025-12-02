package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.PagoChoferRepository;
import com.example.alquilermaquinaria.entity.PagoChofer;

@Service
public class PagoChoferServiceImpl implements PagoChoferService {

    private final PagoChoferRepository repository;

    public PagoChoferServiceImpl(PagoChoferRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PagoChofer> findAll() {
        return repository.findAll();
    }

    @Override
    public PagoChofer findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago de chofer no encontrado"));
    }

    @Override
    public PagoChofer create(PagoChofer pago) {
        return repository.save(pago);
    }

    @Override
    public PagoChofer update(Integer id, PagoChofer pago) {
        PagoChofer existente = findById(id);

        existente.setChofer(pago.getChofer());
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
