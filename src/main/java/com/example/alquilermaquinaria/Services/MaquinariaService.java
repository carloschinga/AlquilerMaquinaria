package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.Maquinaria;

public interface MaquinariaService {

    List<Maquinaria> findAll();

    Maquinaria findById(Integer id);

    Maquinaria create(Maquinaria maquinaria);

    Maquinaria update(Integer id, Maquinaria maquinaria);

    void delete(Integer id);
}
