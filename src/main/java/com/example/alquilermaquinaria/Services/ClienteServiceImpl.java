package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ClienteRepository;
import com.example.alquilermaquinaria.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente findById(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public Cliente create(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente update(Integer id, Cliente cliente) {
        Cliente original = findById(id);
        original.setNombre(cliente.getNombre());
        original.setRucDni(cliente.getRucDni());
        original.setTelefono(cliente.getTelefono());
        original.setDireccion(cliente.getDireccion());
        return clienteRepository.save(original);
    }

    @Override
    public void delete(Integer id) {
        clienteRepository.deleteById(id);
    }
}
