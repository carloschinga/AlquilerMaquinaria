package com.example.alquilermaquinaria.Services;

import java.util.List;

import com.example.alquilermaquinaria.entity.PagoChofer;

public interface PagoChoferService {

    List<PagoChofer> findAll();

    PagoChofer findById(Integer id);

    PagoChofer create(PagoChofer pago, Integer choferId);

    PagoChofer update(Integer id, PagoChofer pago, Integer choferId);

    void delete(Integer id);
}
