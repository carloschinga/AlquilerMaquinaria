package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.dto.CargaCombustibleDTO;

public interface CargaCombustibleService {

    List<CargaCombustibleDTO> findAll();

    CargaCombustibleDTO findById(Integer id);

    CargaCombustibleDTO create(CargaCombustibleDTO carga);

    CargaCombustibleDTO update(Integer id, CargaCombustibleDTO carga);

    void delete(Integer id);
}
