package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.dto.ClienteDTO;

public interface ClienteService {

    List<ClienteDTO> listar();

    ClienteDTO buscarPorId(Integer id);

    ClienteDTO guardarDesdeDTO(ClienteDTO dto);

    void eliminar(Integer id);
}
