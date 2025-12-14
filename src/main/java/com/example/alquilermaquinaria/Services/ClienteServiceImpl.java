package com.example.alquilermaquinaria.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ClienteRepository;
import com.example.alquilermaquinaria.dto.ClienteDTO;
import com.example.alquilermaquinaria.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo) {
        this.repo = repo;
    }

    // Mapea Cliente -> ClienteDTO
    private ClienteDTO mapToDTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setClienteId(c.getClienteId());
        dto.setNombre(c.getNombre());
        dto.setRucDni(c.getRucDni());
        dto.setTelefono(c.getTelefono());
        dto.setDireccion(c.getDireccion());
        return dto;
    }

    // Mapea ClienteDTO -> Cliente (para guardar)
    private Cliente mapToEntity(ClienteDTO dto) {
        Cliente c = dto.getClienteId() != null ? repo.findById(dto.getClienteId()).orElse(new Cliente()) : new Cliente();
        c.setNombre(dto.getNombre());
        c.setRucDni(dto.getRucDni());
        c.setTelefono(dto.getTelefono());
        c.setDireccion(dto.getDireccion());
        return c;
    }

    @Override
    public List<ClienteDTO> listar() {
        return repo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO buscarPorId(Integer id) {
        return repo.findById(id).map(this::mapToDTO).orElse(null);
    }

    @Override
    public ClienteDTO guardarDesdeDTO(ClienteDTO dto) {
        Cliente c = mapToEntity(dto);
        return mapToDTO(repo.save(c));
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
