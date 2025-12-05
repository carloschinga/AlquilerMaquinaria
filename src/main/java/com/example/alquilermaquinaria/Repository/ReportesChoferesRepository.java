package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alquilermaquinaria.entity.Chofer;

public interface ReportesChoferesRepository extends JpaRepository<Chofer, Integer> {

    @Query(
            value = """
            SELECT chofer_id, nombre_chofer, monto_pendiente_pago, numero_turnos_pendientes
            FROM view_pagos_pendientes_a_choferes
        """,
            nativeQuery = true
    )
    List<Object[]> reportePagosPendientesChoferes();
}
