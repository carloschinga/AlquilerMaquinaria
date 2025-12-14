package com.example.alquilermaquinaria.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.ReportesRepository;
import com.example.alquilermaquinaria.dto.DashboardProveedorPagoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportesService {

    private final ReportesRepository reportesRepository;

    public List<DashboardProveedorPagoDTO> obtenerCuentasProveedores() {

        List<Object[]> data = reportesRepository.reporteCuentaProveedores();

        return data.stream().map(row -> {
            DashboardProveedorPagoDTO dto = new DashboardProveedorPagoDTO();

            dto.setProveedorId(((Number) row[0]).intValue()); // <-- seguro
            dto.setNombreProveedor((String) row[1]);
            dto.setMontoPendiente((Double) row[2]);
            dto.setNumeroFacturasPendientes(((Number) row[3]).intValue()); // <-- seguro

            return dto;
        }).toList();

    }
}
