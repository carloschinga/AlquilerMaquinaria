package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ChoferRepository;
import com.example.alquilermaquinaria.entity.Chofer;

@Service
public class ChoferServiceImpl implements ChoferService {

    private final ChoferRepository repo;

    public ChoferServiceImpl(ChoferRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Chofer> findAll() {
        return repo.findAll();
    }

    @Override
    public Chofer findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
    }

    @Override
    public Chofer create(Chofer chofer) {
        return repo.save(chofer);
    }

    @Override
    public Chofer update(Integer id, Chofer chofer) {
        Chofer original = findById(id);

        original.setNombre(chofer.getNombre());
        original.setIdentificacion(chofer.getIdentificacion());
        original.setTarifaHora(chofer.getTarifaHora());
        original.setEstado(chofer.getEstado());

        return repo.save(original);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
