package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.MaquinariaRepository;
import com.example.alquilermaquinaria.entity.Maquinaria;

@Service
public class MaquinariaServiceImpl implements MaquinariaService {

    private final MaquinariaRepository repo;

    public MaquinariaServiceImpl(MaquinariaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Maquinaria> findAll() {
        return repo.findAll();
    }

    @Override
    public Maquinaria findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));
    }

    @Override
    public Maquinaria create(Maquinaria maquinaria) {
        return repo.save(maquinaria);
    }

    @Override
    public Maquinaria update(Integer id, Maquinaria maquinaria) {
        Maquinaria original = findById(id);
        original.setTipo(maquinaria.getTipo());
        original.setModelo(maquinaria.getModelo());
        original.setSerialInterno(maquinaria.getSerialInterno());
        original.setHorasAcumuladas(maquinaria.getHorasAcumuladas());
        original.setEstado(maquinaria.getEstado());
        return repo.save(original);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
