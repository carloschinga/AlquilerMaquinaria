package com.example.alquilermaquinaria.Services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.alquilermaquinaria.Repository.ReportesChoferesRepository;
import com.example.alquilermaquinaria.dto.PagosPendientesChoferDTO;

@Service
public class ReportesChoferesService {

    private final ReportesChoferesRepository repo;

    public ReportesChoferesService(ReportesChoferesRepository repo) {
        this.repo = repo;
    }

    public List<PagosPendientesChoferDTO> obtenerPagosPendientes() {
        return repo.reportePagosPendientesChoferes()
                .stream()
                .map(obj -> {
                    PagosPendientesChoferDTO dto = new PagosPendientesChoferDTO();

                    // 1. BLINDAJE ID: Aceptamos Integer, Long, BigInteger, etc.
                    if (obj[0] != null && obj[0] instanceof Number) {
                        dto.setChoferId(((Number) obj[0]).intValue());
                    } else {
                        dto.setChoferId(0); // Valor por defecto si viene nulo
                    }

                    // 2. BLINDAJE NOMBRE: Convertimos lo que llegue a String (incluso si llega un número 1)
                    dto.setNombreChofer(obj[1] != null ? String.valueOf(obj[1]) : "Sin Nombre");

                    // 3. BLINDAJE MONTO: Aceptamos BigDecimal, Double, Float
                    if (obj[2] != null && obj[2] instanceof Number) {
                        dto.setMontoPendientePago(((Number) obj[2]).doubleValue());
                    } else {
                        dto.setMontoPendientePago(0.0);
                    }

                    // 4. BLINDAJE TURNOS: Aceptamos Integer, Long
                    if (obj[3] != null && obj[3] instanceof Number) {
                        dto.setNumeroTurnosPendientes(((Number) obj[3]).intValue());
                    } else {
                        dto.setNumeroTurnosPendientes(0);
                    }

                    return dto;
                })
                .toList();
    }
}