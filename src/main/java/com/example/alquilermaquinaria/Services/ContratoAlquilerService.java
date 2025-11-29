package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.ContratoAlquiler;

public interface ContratoAlquilerService {

    List<ContratoAlquiler> findAll();

    ContratoAlquiler findById(Integer id);

    ContratoAlquiler create(ContratoAlquiler contrato);

    ContratoAlquiler update(Integer id, ContratoAlquiler contrato);

    void delete(Integer id);
}
