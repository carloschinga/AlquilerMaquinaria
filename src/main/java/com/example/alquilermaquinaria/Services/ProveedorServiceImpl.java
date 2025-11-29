package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ProveedorRepository;
import com.example.alquilermaquinaria.entity.Proveedor;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public List<Proveedor> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor findById(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
    }

    @Override
    public Proveedor create(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor update(Integer id, Proveedor proveedor) {
        Proveedor original = findById(id);
        original.setNombre(proveedor.getNombre());
        original.setRuc(proveedor.getRuc());
        original.setContacto(proveedor.getContacto());
        return proveedorRepository.save(original);
    }

    @Override
    public void delete(Integer id) {
        proveedorRepository.deleteById(id);
    }
}
