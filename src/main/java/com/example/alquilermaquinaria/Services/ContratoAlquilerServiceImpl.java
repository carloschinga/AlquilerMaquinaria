package com.example.alquilermaquinaria.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.AsignacionOperacionRepository;
import com.example.alquilermaquinaria.Repository.ChoferRepository;
import com.example.alquilermaquinaria.Repository.ClienteRepository;
import com.example.alquilermaquinaria.Repository.ContratoAlquilerRepository;
import com.example.alquilermaquinaria.Repository.MaquinariaRepository;
import com.example.alquilermaquinaria.dto.ContratoAlquilerDTO;
import com.example.alquilermaquinaria.dto.ContratoCreateRequestDTO;
import com.example.alquilermaquinaria.dto.DashboardContratosDTO;
import com.example.alquilermaquinaria.dto.UtilizacionDTO;
import com.example.alquilermaquinaria.entity.AsignacionOperacion;
import com.example.alquilermaquinaria.entity.Chofer;
import com.example.alquilermaquinaria.entity.Cliente;
import com.example.alquilermaquinaria.entity.ContratoAlquiler;
import com.example.alquilermaquinaria.entity.Maquinaria;

@Service
public class ContratoAlquilerServiceImpl implements ContratoAlquilerService {

    private final ContratoAlquilerRepository repo;
    private final ClienteRepository clienteRepo;
    private final MaquinariaRepository maquinariaRepo;
    private final ChoferRepository choferRepo;
    private final AsignacionOperacionRepository asignacionRepo;

    public ContratoAlquilerServiceImpl(ContratoAlquilerRepository repo, ClienteRepository clienteRepo,
            MaquinariaRepository maquinariaRepo, ChoferRepository choferRepo,
            AsignacionOperacionRepository asignacionRepo) {
        this.repo = repo;
        this.clienteRepo = clienteRepo;
        this.maquinariaRepo = maquinariaRepo;
        this.choferRepo = choferRepo;
        this.asignacionRepo = asignacionRepo;
    }

    // ------------------ MAPPER ------------------
    private ContratoAlquilerDTO toDTO(ContratoAlquiler c) {
        ContratoAlquilerDTO dto = new ContratoAlquilerDTO();
        dto.setContratoId(c.getContratoId());

        // Cliente
        if (c.getCliente() != null) {
            dto.setClienteId(c.getCliente().getClienteId());
            dto.setClienteNombre(c.getCliente().getNombre());
        }

        // Maquinaria
        if (c.getMaquinaria() != null) {
            dto.setMaquinaId(c.getMaquinaria().getMaquinaId());
            dto.setMaquinaModelo(c.getMaquinaria().getModelo());
            dto.setMaquinaTipo(c.getMaquinaria().getTipo());
            dto.setMaquinaEstado(c.getMaquinaria().getEstado());
        }

        // Campos simples
        dto.setFechaInicio(c.getFechaInicio());
        dto.setFechaFinEstimada(c.getFechaFinEstimada());
        dto.setFechaFinReal(c.getFechaFinReal());
        dto.setHorometroInicial(c.getHorometroInicial());
        dto.setHorometroFinal(c.getHorometroFinal());
        dto.setTiempoUsoHrs(c.getTiempoUsoHrs());
        dto.setTarifaAplicada(c.getTarifaAplicada());
        dto.setEstadoPago(c.getEstadoPago());
        dto.setCondicionEntrega(c.getCondicionEntrega());
        dto.setCondicionDevolucion(c.getCondicionDevolucion());
        dto.setMontoTotalCobrar(c.getMontoTotalCobrar());

        // -------------------------------
        // CHOFER Y ASIGNACIÓN (SEGURO ANTE NULL)
        // -------------------------------
        if (c.getAsignaciones() != null && !c.getAsignaciones().isEmpty()) {
            AsignacionOperacion a = c.getAsignaciones().get(0);

            if (a != null && a.getChofer() != null) {
                dto.setChoferId(a.getChofer().getChoferId());
                dto.setChoferNombre(a.getChofer().getNombre());
                dto.setChoferTarifaHora(a.getChofer().getTarifaHora());
                dto.setChoferEstado(a.getChofer().getEstado());
            } else {
                dto.setChoferId(null);
            }

            if (a != null) {
                dto.setFechaTurnoInicial(a.getFechaTurno() != null ? a.getFechaTurno().toString() : null);
                dto.setHorasIniciales(a.getHorasTrabajadas());
            }
        } else {
            // No hay asignación → limpiar datos de chofer
            dto.setChoferId(null);
        }

        return dto;
    }

    // ------------------ CRUD ------------------
    @Override
    public List<ContratoAlquilerDTO> findAllDTO() {
        return repo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ContratoAlquilerDTO findDTOById(Integer id) {
        ContratoAlquiler c = repo.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        return toDTO(c);
    }

    @Override
    public ContratoAlquilerDTO createDTO(ContratoAlquiler contrato) {
        Cliente cliente = clienteRepo.findById(contrato.getCliente().getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Maquinaria maquinaria = maquinariaRepo.findById(contrato.getMaquinaria().getMaquinaId())
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));
        maquinaria.setEstado("Ocupada");
        contrato.setCliente(cliente);
        contrato.setMaquinaria(maquinaria);

        ContratoAlquiler saved = repo.save(contrato);
        return toDTO(saved);
    }

    @Override
    public ContratoAlquilerDTO create(ContratoCreateRequestDTO dto) {

        System.out.println("======================================");
        System.out.println(dto);
        // crear contrato normal
        Cliente cliente = clienteRepo.findById(dto.getClienteId()).orElseThrow();
        Maquinaria maq = maquinariaRepo.findById(dto.getMaquinaId()).orElseThrow();
        maq.setEstado("Ocupada");
        ContratoAlquiler contrato = new ContratoAlquiler();
        contrato.setCliente(cliente);
        contrato.setMaquinaria(maq);
        contrato.setFechaInicio(dto.getFechaInicio());
        contrato.setEstadoPago(dto.getEstadoPago());
        contrato.setFechaFinEstimada(dto.getFechaFinEstimada());
        contrato.setHorometroInicial(dto.getHorometroInicial());
        contrato.setTarifaAplicada(dto.getTarifaAplicada());
        contrato.setCondicionEntrega(dto.getCondicionEntrega());

        ContratoAlquiler saved = repo.save(contrato);

        // ===========================
        // Si viene choferId, crear asignación
        // ===========================
        if (dto.getChoferId() != null && dto.getChoferId() != 0) {
            Chofer chofer = choferRepo.findById(dto.getChoferId()).orElseThrow();
            chofer.setEstado("Ocupado");

            AsignacionOperacion a = new AsignacionOperacion();
            a.setContrato(saved);
            a.setChofer(chofer);
            a.setHorasTrabajadas(dto.getHorasIniciales());
            a.setFechaTurno(LocalDate.parse(dto.getFechaTurnoInicial()));

            double pago = chofer.getTarifaHora() * dto.getHorasIniciales();
            a.setPagoCalculado(pago);
            a.setEstadoPago("Pendiente");

            asignacionRepo.save(a);
        }

        return toDTO(saved);
    }

    @Override
    public ContratoAlquilerDTO updateDTO(Integer id, ContratoAlquilerDTO dto) {
        // 1. Obtener contrato
        ContratoAlquiler contrato = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        // 2. Actualizar campos simples del contrato
        contrato.setFechaInicio(dto.getFechaInicio());
        contrato.setFechaFinEstimada(dto.getFechaFinEstimada());
        contrato.setFechaFinReal(dto.getFechaFinReal());
        contrato.setHorometroInicial(dto.getHorometroInicial());
        contrato.setHorometroFinal(dto.getHorometroFinal());
        contrato.setTiempoUsoHrs(dto.getTiempoUsoHrs());
        contrato.setTarifaAplicada(dto.getTarifaAplicada());
        contrato.setEstadoPago(dto.getEstadoPago());
        contrato.setCondicionEntrega(dto.getCondicionEntrega());
        contrato.setCondicionDevolucion(dto.getCondicionDevolucion());
        contrato.setMontoTotalCobrar(dto.getMontoTotalCobrar());

        // 3. Cliente
        Cliente cliente = clienteRepo.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        contrato.setCliente(cliente);

        // 4. Maquinaria
        Maquinaria maquinaria = maquinariaRepo.findById(dto.getMaquinaId())
                .orElseThrow(() -> new RuntimeException("Maquinaria no encontrada"));
        contrato.setMaquinaria(maquinaria);

        // 5. Obtener la(s) asignaciones relacionadas (ahora retorna LIST)
        List<AsignacionOperacion> asignaciones = asignacionRepo.findByContratoContratoId(id);
        AsignacionOperacion asignacionActual = asignaciones.isEmpty() ? null : asignaciones.get(asignaciones.size() - 1);

        boolean teniaChoferAntes = asignacionActual != null && asignacionActual.getChofer() != null;
        Integer choferAnteriorId = teniaChoferAntes ? asignacionActual.getChofer().getChoferId() : null;

        // 6. Si en el DTO no viene chofer (se quita)
        if (dto.getChoferId() == null || dto.getChoferId() == 0) {
            if (teniaChoferAntes) {
                // liberar chofer anterior
                Chofer choferAntiguo = choferRepo.findById(choferAnteriorId).orElse(null);
                if (choferAntiguo != null) {
                    choferAntiguo.setEstado("Activo");
                    choferRepo.save(choferAntiguo);
                }
                // eliminar la asignación actual
                asignacionRepo.delete(asignacionActual);
            }
            // guardar contrato sin asignación
            return toDTO(contrato);
        }

        // 7. Si viene chofer en DTO -> obtener nuevo chofer
        Chofer nuevoChofer = choferRepo.findById(dto.getChoferId())
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));

        // normalizar valores null
        Double horas = dto.getHorasIniciales() != null ? dto.getHorasIniciales() : 0.0;
        LocalDate fechaTurno = null;
        if (dto.getFechaTurnoInicial() != null && !dto.getFechaTurnoInicial().isBlank()) {
            fechaTurno = LocalDate.parse(dto.getFechaTurnoInicial());
        }

        // 8. Si es el mismo chofer que ya tenía la última asignación -> solo actualizar esa asignación
        if (teniaChoferAntes && choferAnteriorId != null && choferAnteriorId.equals(dto.getChoferId())) {
            // actualizar horas, fecha y pago
            asignacionActual.setHorasTrabajadas(horas);
            if (fechaTurno != null) {
                asignacionActual.setFechaTurno(fechaTurno);
            }
            Double tarifa = asignacionActual.getChofer() != null && asignacionActual.getChofer().getTarifaHora() != null
                    ? asignacionActual.getChofer().getTarifaHora()
                    : nuevoChofer.getTarifaHora();
            asignacionActual.setPagoCalculado(horas * tarifa);
            asignacionRepo.save(asignacionActual);

            // asegúrate de que el chofer esté en estado Ocupado (por si)
            nuevoChofer.setEstado("Ocupado");
            choferRepo.save(nuevoChofer);

            return toDTO(contrato);
        }

        // 9. Si cambió el chofer → liberar el anterior (si existía) y eliminar su asignación
        if (teniaChoferAntes) {
            Chofer choferAntiguo = asignacionActual.getChofer() != null
                    ? choferRepo.findById(asignacionActual.getChofer().getChoferId()).orElse(null)
                    : null;
            if (choferAntiguo != null) {
                choferAntiguo.setEstado("Activo");
                choferRepo.save(choferAntiguo);
            }
            asignacionRepo.delete(asignacionActual);
        }

        // 10. Crear nueva asignación para el nuevo chofer
        AsignacionOperacion nueva = new AsignacionOperacion();
        nueva.setContrato(contrato);
        nueva.setChofer(nuevoChofer);
        nueva.setHorasTrabajadas(horas);
        if (fechaTurno != null) {
            nueva.setFechaTurno(fechaTurno);
        }
        nueva.setPagoCalculado(horas * (nuevoChofer.getTarifaHora() != null ? nuevoChofer.getTarifaHora() : 0.0));
        nueva.setEstadoPago("Pendiente");
        asignacionRepo.save(nueva);

        // 11. Marcar nuevo chofer como ocupado
        nuevoChofer.setEstado("Ocupado");
        choferRepo.save(nuevoChofer);

        // 12. Guardar contrato y retornar
        return toDTO(contrato);
    }

    @Override
    public void delete(Integer id) {
        ContratoAlquiler original = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        original.getMaquinaria().setEstado("Disponible");
        List<AsignacionOperacion> asignaciones = asignacionRepo.findByContratoContratoId(id);
        AsignacionOperacion asignacionActual = asignaciones.isEmpty()
                ? null
                : asignaciones.get(asignaciones.size() - 1);

        if (asignacionActual != null && asignacionActual.getChofer() != null) {
            Chofer chofer = asignacionActual.getChofer();
            chofer.setEstado("Activo");
            choferRepo.save(chofer);
        }
        repo.deleteById(id);
    }

    @Override
    public ContratoAlquilerDTO cerrarContrato(Integer id, ContratoAlquiler contratoCierre) {

        // 1. Obtener contrato original
        ContratoAlquiler original = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        // 2. Obtener todas las asignaciones (LISTA)
        List<AsignacionOperacion> asignaciones = asignacionRepo.findByContratoContratoId(id);
        AsignacionOperacion asignacionActual = asignaciones.isEmpty()
                ? null
                : asignaciones.get(asignaciones.size() - 1); // Tomamos la última asignación

        // 3. Liberar chofer si tenía
        if (asignacionActual != null && asignacionActual.getChofer() != null) {
            Chofer chofer = asignacionActual.getChofer();
            chofer.setEstado("Activo");
            choferRepo.save(chofer);

            // IMPORTANTE: marcar la asignación como pagada/cerrada
            asignacionActual.setEstadoPago("Pendiente");
            asignacionRepo.save(asignacionActual);
        }

        // 4. Actualizar datos del cierre del contrato
        if (contratoCierre.getFechaFinReal() != null) {
            original.setFechaFinReal(contratoCierre.getFechaFinReal());
        }
        if (contratoCierre.getHorometroFinal() != null) {
            original.setHorometroFinal(contratoCierre.getHorometroFinal());
        }
        if (contratoCierre.getTiempoUsoHrs() != null) {
            original.setTiempoUsoHrs(contratoCierre.getTiempoUsoHrs());
        }
        if (contratoCierre.getMontoTotalCobrar() != null) {
            original.setMontoTotalCobrar(contratoCierre.getMontoTotalCobrar());
        }
        if (contratoCierre.getEstadoPago() != null) {
            original.setEstadoPago(contratoCierre.getEstadoPago());
        }
        if (contratoCierre.getCondicionDevolucion() != null) {
            original.setCondicionDevolucion(contratoCierre.getCondicionDevolucion());
        }

        // 5. Cambiar estado de maquinaria
        Maquinaria maq = original.getMaquinaria();
        maq.setEstado("Disponible");

        // 6. Acumular horas solo si está pagado
        if (contratoCierre.getTiempoUsoHrs() != null) {
            double horasFinales = maq.getHorasAcumuladas() + contratoCierre.getTiempoUsoHrs();
            maq.setHorasAcumuladas(horasFinales);
        }

        // 7. Guardar
        ContratoAlquiler updated = repo.save(original);

        return toDTO(updated);
    }

    public List<UtilizacionDTO> obtenerUtilizacion(LocalDateTime inicio, LocalDateTime fin) {
        return repo.calcularUtilizacionPorRango(inicio, fin)
                .stream()
                .map(obj -> {
                    UtilizacionDTO dto = new UtilizacionDTO();
                    dto.setMaquinaId((Integer) obj[0]);
                    dto.setHorasUsadas(((Number) obj[1]).longValue());
                    dto.setCantidadContratos(((Number) obj[2]).longValue());
                    dto.setModelo(((String) obj[3]));
                    return dto;
                })
                .toList();
    }

    @Override
    public DashboardContratosDTO obtenerResumenContratos() {
        DashboardContratosDTO dto = new DashboardContratosDTO();
        dto.setTotalContratos(repo.contarTotalContratos());
        dto.setContratosActivos(repo.contarContratosActivos());
        dto.setContratosPorCobrar(repo.contarContratosPorCobrar());
        dto.setMontoPorCobrar(repo.sumarMontoPorCobrar());
        return dto;
    }

}
