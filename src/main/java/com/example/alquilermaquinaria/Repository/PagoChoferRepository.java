package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.Chofer;
import com.example.alquilermaquinaria.entity.PagoChofer;

public interface PagoChoferRepository extends JpaRepository<PagoChofer, Integer> {

    List<PagoChofer> findByChofer(Chofer chofer);
}
