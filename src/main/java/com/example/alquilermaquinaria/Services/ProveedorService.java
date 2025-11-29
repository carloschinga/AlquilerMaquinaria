package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.Proveedor;

public interface ProveedorService {

    List<Proveedor> findAll();

    Proveedor findById(Integer id);

    Proveedor create(Proveedor proveedor);

    Proveedor update(Integer id, Proveedor proveedor);

    void delete(Integer id);
}
