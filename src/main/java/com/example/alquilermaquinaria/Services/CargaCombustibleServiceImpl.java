package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.CargaCombustibleRepository;
import com.example.alquilermaquinaria.entity.CargaCombustible;

@Service
public class CargaCombustibleServiceImpl implements CargaCombustibleService {

    private final CargaCombustibleRepository repo;

    public CargaCombustibleServiceImpl(CargaCombustibleRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CargaCombustible> findAll() {
        return repo.findAll();
    }

    @Override
    public CargaCombustible findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));
    }

    @Override
    public CargaCombustible create(CargaCombustible carga) {
        return repo.save(carga);
    }

    @Override
    public CargaCombustible update(Integer id, CargaCombustible carga) {
        CargaCombustible original = findById(id);

        original.setMaquinaria(carga.getMaquinaria());
        original.setProveedor(carga.getProveedor());
        original.setFechaCarga(carga.getFechaCarga());
        original.setLitrosCargados(carga.getLitrosCargados());
        original.setCostoTotal(carga.getCostoTotal());
        original.setCostoUnitario(carga.getCostoUnitario());
        original.setLecturaHorometro(carga.getLecturaHorometro());
        original.setFacturaNum(carga.getFacturaNum());
        original.setEstadoPago(carga.getEstadoPago());

        return repo.save(original);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
