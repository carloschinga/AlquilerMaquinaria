package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.dto.ClienteDTO;
import com.example.alquilermaquinaria.entity.Cliente;

public interface ClienteService {

    List<Cliente> listar();

    Cliente buscarPorId(Integer id);

    Cliente guardarDesdeDTO(ClienteDTO dto);

    void eliminar(Integer id);
}
