package com.example.alquilermaquinaria.Services;

import java.time.LocalDateTime;
import java.util.List;

import com.example.alquilermaquinaria.dto.ContratoAlquilerDTO;
import com.example.alquilermaquinaria.dto.ContratoCreateRequestDTO;
import com.example.alquilermaquinaria.dto.DashboardContratosDTO;
import com.example.alquilermaquinaria.dto.UtilizacionDTO;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;

public interface ContratoAlquilerService {

    List<ContratoAlquilerDTO> findAllDTO();

    ContratoAlquilerDTO findDTOById(Integer id);

    ContratoAlquilerDTO createDTO(ContratoAlquiler contrato);

    ContratoAlquilerDTO updateDTO(Integer id, ContratoAlquilerDTO contrato);

    void delete(Integer id);

    ContratoAlquilerDTO cerrarContrato(Integer id, ContratoAlquiler contratoCierre);

    List<UtilizacionDTO> obtenerUtilizacion(LocalDateTime inicio, LocalDateTime fin);

    DashboardContratosDTO obtenerResumenContratos();

    ContratoAlquilerDTO create(ContratoCreateRequestDTO dto);
}
