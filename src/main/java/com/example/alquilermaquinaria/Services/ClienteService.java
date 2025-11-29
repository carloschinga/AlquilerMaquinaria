package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.Cliente;

public interface ClienteService {

    List<Cliente> findAll();

    Cliente findById(Integer id);

    Cliente create(Cliente cliente);

    Cliente update(Integer id, Cliente cliente);

    void delete(Integer id);
}
