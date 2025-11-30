package com.example.alquilermaquinaria.Services;

import java.util.List;

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

    @Override
    public List<Cliente> listar() {
        return repo.findAll();
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Cliente guardarDesdeDTO(ClienteDTO dto) {
        Cliente c = new Cliente();

        if (dto.getClienteId() != null) {
            c = buscarPorId(dto.getClienteId());
        }

        c.setNombre(dto.getNombre());
        c.setRucDni(dto.getRucDni());
        c.setTelefono(dto.getTelefono());
        c.setDireccion(dto.getDireccion());

        return repo.save(c);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}
