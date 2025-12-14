package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.AsignacionOperacion;
import com.example.alquilermaquinaria.entity.Chofer;

public interface AsignacionOperacionRepository extends JpaRepository<AsignacionOperacion, Integer> {

    AsignacionOperacion findByAsignacionId(int id);

    List<AsignacionOperacion> findByChoferAndEstadoPagoNot(Chofer chofer, String estado);

    List<AsignacionOperacion> findByContratoContratoId(Integer contratoId);

    List<AsignacionOperacion> findByChoferAndEstadoPago(Chofer chofer, String estadoPago);

}
