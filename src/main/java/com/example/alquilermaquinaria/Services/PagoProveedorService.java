package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.PagoProveedor;

public interface PagoProveedorService {

    List<PagoProveedor> findAll();

    PagoProveedor findById(Integer id);

    PagoProveedor create(PagoProveedor pago);

    PagoProveedor update(Integer id, PagoProveedor pago);

    void delete(Integer id);
}
