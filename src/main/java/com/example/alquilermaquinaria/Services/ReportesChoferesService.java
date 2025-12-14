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
                    dto.setChoferId((Integer) obj[0]);
                    dto.setNombreChofer((String) obj[1]);
                    dto.setMontoPendientePago(((Number) obj[2]).doubleValue());
                    dto.setNumeroTurnosPendientes(((Number) obj[3]).intValue());
                    return dto;
                })
                .toList();
    }
}
