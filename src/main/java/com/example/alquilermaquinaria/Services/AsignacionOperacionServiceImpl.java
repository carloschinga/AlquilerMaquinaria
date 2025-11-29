package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.AsignacionOperacionRepository;
import com.example.alquilermaquinaria.entity.AsignacionOperacion;

@Service
public class AsignacionOperacionServiceImpl implements AsignacionOperacionService {

    private final AsignacionOperacionRepository repo;

    public AsignacionOperacionServiceImpl(AsignacionOperacionRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<AsignacionOperacion> findAll() {
        return repo.findAll();
    }

    @Override
    public AsignacionOperacion findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
    }

    @Override
    public AsignacionOperacion create(AsignacionOperacion asignacion) {
        return repo.save(asignacion);
    }

    @Override
    public AsignacionOperacion update(Integer id, AsignacionOperacion asignacion) {
        AsignacionOperacion original = findById(id);

        original.setContrato(asignacion.getContrato());
        original.setChofer(asignacion.getChofer());
        original.setFechaTurno(asignacion.getFechaTurno());
        original.setHorasTrabajadas(asignacion.getHorasTrabajadas());
        original.setPagoCalculado(asignacion.getPagoCalculado());
        original.setEstadoPago(asignacion.getEstadoPago());

        return repo.save(original);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
