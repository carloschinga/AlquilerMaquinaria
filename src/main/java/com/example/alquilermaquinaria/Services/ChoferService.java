package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.Chofer;

public interface ChoferService {

    List<Chofer> findAll();

    Chofer findById(Integer id);

    Chofer create(Chofer chofer);

    Chofer update(Integer id, Chofer chofer);

    void delete(Integer id);
}
