package com.example.alquilermaquinaria.Controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.CargaCombustibleService;
import com.example.alquilermaquinaria.dto.CargaCombustibleDTO;
import com.example.alquilermaquinaria.entity.CargaCombustible;
import com.example.alquilermaquinaria.entity.Maquinaria;
import com.example.alquilermaquinaria.entity.Proveedor;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cargas-combustible")
public class CargaCombustibleController {

    private final CargaCombustibleService service;

    public CargaCombustibleController(CargaCombustibleService service) {
        this.service = service;
    }

    @GetMapping
    public List<CargaCombustible> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CargaCombustible obtener(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public CargaCombustible crear(@Valid @RequestBody CargaCombustibleDTO dto) {

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

        return service.create(carga);
    }

    @PutMapping("/{id}")
    public CargaCombustible actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CargaCombustibleDTO dto) {

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

        return service.update(id, carga);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        service.delete(id);
    }
}
