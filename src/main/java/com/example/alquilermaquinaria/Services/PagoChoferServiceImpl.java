package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.AsignacionOperacionRepository;
import com.example.alquilermaquinaria.Repository.ChoferRepository;
import com.example.alquilermaquinaria.Repository.PagoChoferRepository;
import com.example.alquilermaquinaria.entity.AsignacionOperacion;
import com.example.alquilermaquinaria.entity.Chofer;
import com.example.alquilermaquinaria.entity.PagoChofer;

@Service
public class PagoChoferServiceImpl implements PagoChoferService {

    private final PagoChoferRepository repository;
    private final AsignacionOperacionRepository asignacionOperacionRepository;
    private final ChoferRepository choferRepository;

    public PagoChoferServiceImpl(PagoChoferRepository repository,
            AsignacionOperacionRepository asignacionOperacionRepository,
            ChoferRepository choferRepository) {
        this.repository = repository;
        this.asignacionOperacionRepository = asignacionOperacionRepository;
        this.choferRepository = choferRepository;
    }

    @Override
    public List<PagoChofer> findAll() {
        return repository.findAll();
    }

    @Override
    public PagoChofer findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago de chofer no encontrado"));
    }

    @Override
    public PagoChofer create(PagoChofer pago, Integer choferId) {
        Chofer chofer = choferRepository.findById(choferId)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));

        pago.setChofer(chofer);

        PagoChofer nuevoPago = repository.save(pago);
        actualizarAsignacionesChofer(chofer, pago.getEstado());
        return nuevoPago;
    }

    @Override
    public PagoChofer update(Integer id, PagoChofer pago, Integer choferId) {
        PagoChofer existente = findById(id);

        Chofer chofer = choferRepository.findById(choferId)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));

        existente.setChofer(chofer);
        existente.setFechaPago(pago.getFechaPago());
        existente.setMontoPagado(pago.getMontoPagado());
        existente.setMetodoPago(pago.getMetodoPago());
        existente.setDescripcion(pago.getDescripcion());
        existente.setEstado(pago.getEstado());

        PagoChofer actualizado = repository.save(existente);
        actualizarAsignacionesChofer(chofer, pago.getEstado());
        return actualizado;
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private void actualizarAsignacionesChofer(Chofer chofer, String estadoPago) {

        // Si el pago no está marcado como "Pagado", no actualizamos las asignaciones.
        if (!"Pagado".equalsIgnoreCase(estadoPago)) {
            // Revertir cambios si el pago estaba marcado como "Pagado" y ahora no lo está.
            List<AsignacionOperacion> asignacionesPendientes = asignacionOperacionRepository
                    .findByChoferAndEstadoPago(chofer, "Pagado");

            for (AsignacionOperacion asignacion : asignacionesPendientes) {
                // Si la asignación ya estaba como "Pagado", la revertimos a "Pendiente".
                asignacion.setEstadoPago("Pendiente");
                double montoOriginal = asignacion.getHorasTrabajadas() * chofer.getTarifaHora();
                asignacion.setPagoCalculado(montoOriginal); // o el valor que desees cuando esté pendiente
            }

            asignacionOperacionRepository.saveAll(asignacionesPendientes);
            return; // No procesamos más si no es "Pagado"
        }

        // Traer solo asignaciones pendientes
        List<AsignacionOperacion> asignaciones = asignacionOperacionRepository
                .findByChoferAndEstadoPagoNot(chofer, "Pagado");

        // Total pagado hasta ahora
        Double totalPagado = repository.findByChofer(chofer)
                .stream()
                .mapToDouble(PagoChofer::getMontoPagado)
                .sum();

        for (AsignacionOperacion asignacion : asignaciones) {

            double pendiente = asignacion.getHorasTrabajadas() * chofer.getTarifaHora();

            if (totalPagado >= pendiente) {
                asignacion.setEstadoPago("Pagado");
                asignacion.setPagoCalculado(pendiente);
                totalPagado -= pendiente;
            } else {
                asignacion.setEstadoPago("Pendiente");
                asignacion.setPagoCalculado(totalPagado);
                totalPagado = 0.0;
            }
        }

        asignacionOperacionRepository.saveAll(asignaciones);
    }

}
