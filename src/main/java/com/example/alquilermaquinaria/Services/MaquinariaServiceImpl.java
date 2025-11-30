package com.example.alquilermaquinaria.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.MaquinariaRepository;
import com.example.alquilermaquinaria.dto.MaquinariaDTO;
import com.example.alquilermaquinaria.entity.Maquinaria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaquinariaServiceImpl implements MaquinariaService {

    private final MaquinariaRepository maquinariaRepository;

    private MaquinariaDTO mapToDTO(Maquinaria entity) {
        MaquinariaDTO dto = new MaquinariaDTO();
        dto.setMaquinaId(entity.getMaquinaId());
        dto.setTipo(entity.getTipo());
        dto.setModelo(entity.getModelo());
        dto.setSerialInterno(entity.getSerialInterno());
        dto.setHorasAcumuladas(entity.getHorasAcumuladas());
        dto.setEstado(entity.getEstado());
        return dto;
    }

    private Maquinaria mapToEntity(MaquinariaDTO dto) {
        Maquinaria entity = new Maquinaria();
        entity.setMaquinaId(dto.getMaquinaId());
        entity.setTipo(dto.getTipo());
        entity.setModelo(dto.getModelo());
        entity.setSerialInterno(dto.getSerialInterno());
        entity.setHorasAcumuladas(dto.getHorasAcumuladas());
        entity.setEstado(dto.getEstado());
        return entity;
    }

    @Override
    public MaquinariaDTO registrar(MaquinariaDTO dto) {
        Maquinaria entity = mapToEntity(dto);
        return mapToDTO(maquinariaRepository.save(entity));
    }

    @Override
    public MaquinariaDTO actualizar(Integer id, MaquinariaDTO dto) {
        Maquinaria maquina = maquinariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));

        maquina.setTipo(dto.getTipo());
        maquina.setModelo(dto.getModelo());
        maquina.setSerialInterno(dto.getSerialInterno());
        maquina.setHorasAcumuladas(dto.getHorasAcumuladas());
        maquina.setEstado(dto.getEstado());

        return mapToDTO(maquinariaRepository.save(maquina));
    }

    @Override
    public MaquinariaDTO obtenerPorId(Integer id) {
        return maquinariaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));
    }

    @Override
    public List<MaquinariaDTO> listar() {
        return maquinariaRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Integer id) {
        maquinariaRepository.deleteById(id);
    }
}
