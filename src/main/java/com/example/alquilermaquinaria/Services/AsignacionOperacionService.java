package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.AsignacionOperacion;

public interface AsignacionOperacionService {

    List<AsignacionOperacion> findAll();

    AsignacionOperacion findById(Integer id);

    AsignacionOperacion create(AsignacionOperacion asignacion);

    AsignacionOperacion update(Integer id, AsignacionOperacion asignacion);

    void delete(Integer id);
}
