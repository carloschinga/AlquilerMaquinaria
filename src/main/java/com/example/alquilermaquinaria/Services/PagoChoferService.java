package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.PagoChofer;

public interface PagoChoferService {

    List<PagoChofer> findAll();

    PagoChofer findById(Integer id);

    PagoChofer create(PagoChofer pago);

    PagoChofer update(Integer id, PagoChofer pago);

    void delete(Integer id);
}
