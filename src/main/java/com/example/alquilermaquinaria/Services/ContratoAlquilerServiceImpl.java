package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ContratoAlquilerRepository;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;

@Service
public class ContratoAlquilerServiceImpl implements ContratoAlquilerService {

    private final ContratoAlquilerRepository repo;

    public ContratoAlquilerServiceImpl(ContratoAlquilerRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ContratoAlquiler> findAll() {
        return repo.findAll();
    }

    @Override
    public ContratoAlquiler findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }

    @Override
    public ContratoAlquiler create(ContratoAlquiler contrato) {
        return repo.save(contrato);
    }

    @Override
    public ContratoAlquiler update(Integer id, ContratoAlquiler contrato) {
        ContratoAlquiler original = findById(id);

        original.setCliente(contrato.getCliente());
        original.setMaquinaria(contrato.getMaquinaria());

        original.setFechaInicio(contrato.getFechaInicio());
        original.setFechaFinEstimada(contrato.getFechaFinEstimada());
        original.setFechaFinReal(contrato.getFechaFinReal());

        original.setHorometroInicial(contrato.getHorometroInicial());
        original.setHorometroFinal(contrato.getHorometroFinal());
        original.setTiempoUsoHrs(contrato.getTiempoUsoHrs());

        original.setTarifaAplicada(contrato.getTarifaAplicada());
        original.setMontoTotalCobrar(contrato.getMontoTotalCobrar());

        original.setEstadoPago(contrato.getEstadoPago());
        original.setCondicionEntrega(contrato.getCondicionEntrega());
        original.setCondicionDevolucion(contrato.getCondicionDevolucion());

        return repo.save(original);
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
