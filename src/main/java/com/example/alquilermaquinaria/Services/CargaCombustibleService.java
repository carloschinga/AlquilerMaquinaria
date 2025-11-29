package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.CargaCombustible;

public interface CargaCombustibleService {

    List<CargaCombustible> findAll();

    CargaCombustible findById(Integer id);

    CargaCombustible create(CargaCombustible carga);

    CargaCombustible update(Integer id, CargaCombustible carga);

    void delete(Integer id);
}
