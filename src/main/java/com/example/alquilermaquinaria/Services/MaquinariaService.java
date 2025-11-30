package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.dto.MaquinariaDTO;

public interface MaquinariaService {

    MaquinariaDTO registrar(MaquinariaDTO dto);

    MaquinariaDTO actualizar(Integer id, MaquinariaDTO dto);

    MaquinariaDTO obtenerPorId(Integer id);

    List<MaquinariaDTO> listar();

    void eliminar(Integer id);
}
