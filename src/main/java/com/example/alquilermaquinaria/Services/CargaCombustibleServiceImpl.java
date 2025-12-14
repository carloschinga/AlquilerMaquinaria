package com.example.alquilermaquinaria.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.alquilermaquinaria.Repository.CargaCombustibleRepository;
import com.example.alquilermaquinaria.dto.CargaCombustibleDTO;
import com.example.alquilermaquinaria.entity.CargaCombustible;
import com.example.alquilermaquinaria.entity.Maquinaria;
import com.example.alquilermaquinaria.entity.Proveedor;

@Service
public class CargaCombustibleServiceImpl implements CargaCombustibleService {

    private final CargaCombustibleRepository repo;

    public CargaCombustibleServiceImpl(CargaCombustibleRepository repo) {
        this.repo = repo;
    }

    private CargaCombustibleDTO toDTO(CargaCombustible carga) {
        CargaCombustibleDTO dto = new CargaCombustibleDTO();

        dto.setCargaId(carga.getCargaId());
        dto.setMaquinariaId(carga.getMaquinaria().getMaquinaId());
        dto.setMaquinaModelo(carga.getMaquinaria().getModelo());
        dto.setMaquinaTipo(carga.getMaquinaria().getTipo());
        dto.setProveedorId(carga.getProveedor().getProveedorId());
        dto.setProveedorNombre(carga.getProveedor().getNombre());
        dto.setFechaCarga(carga.getFechaCarga().toString());
        dto.setLitrosCargados(carga.getLitrosCargados());
        dto.setCostoTotal(carga.getCostoTotal());
        dto.setCostoUnitario(carga.getCostoUnitario());
        dto.setLecturaHorometro(carga.getLecturaHorometro());
        dto.setFacturaNum(carga.getFacturaNum());
        dto.setEstadoPago(carga.getEstadoPago());

        return dto;
    }

    private CargaCombustible toEntity(CargaCombustibleDTO dto) {
        CargaCombustible carga = new CargaCombustible();

        Maquinaria maq = new Maquinaria();
        maq.setMaquinaId(dto.getMaquinariaId());

        Proveedor prov = new Proveedor();
        prov.setProveedorId(dto.getProveedorId());

        carga.setMaquinaria(maq);
        carga.setProveedor(prov);
        carga.setFechaCarga(LocalDateTime.parse(dto.getFechaCarga()));
        carga.setLitrosCargados(dto.getLitrosCargados());
        carga.setCostoTotal(dto.getCostoTotal());
        carga.setCostoUnitario(dto.getCostoUnitario());
        carga.setLecturaHorometro(dto.getLecturaHorometro());
        carga.setFacturaNum(dto.getFacturaNum());
        carga.setEstadoPago(dto.getEstadoPago());

        return carga;
    }

    @Override
    public List<CargaCombustibleDTO> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public CargaCombustibleDTO findById(Integer id) {
        return repo.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));
    }

    @Override
    public CargaCombustibleDTO create(CargaCombustibleDTO dto) {
        CargaCombustible saved = repo.save(toEntity(dto));
        return toDTO(saved);
    }

    @Override
    public CargaCombustibleDTO update(Integer id, CargaCombustibleDTO dto) {
        CargaCombustible original = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Carga de combustible no encontrada"));

        // actualizamos campos
        original.setMaquinaria(toEntity(dto).getMaquinaria());
        original.setProveedor(toEntity(dto).getProveedor());
        original.setFechaCarga(LocalDateTime.parse(dto.getFechaCarga()));
        original.setLitrosCargados(dto.getLitrosCargados());
        original.setCostoTotal(dto.getCostoTotal());
        original.setCostoUnitario(dto.getCostoUnitario());
        original.setLecturaHorometro(dto.getLecturaHorometro());
        original.setFacturaNum(dto.getFacturaNum());
        original.setEstadoPago(dto.getEstadoPago());

        return toDTO(repo.save(original));
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
