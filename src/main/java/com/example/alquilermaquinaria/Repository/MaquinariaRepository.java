package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alquilermaquinaria.entity.Maquinaria;

public interface MaquinariaRepository extends JpaRepository<Maquinaria, Integer> {

    @Query("SELECT COUNT(m) FROM Maquinaria m")
    Long totalMaquinas();

    @Query("SELECT COUNT(m) FROM Maquinaria m WHERE m.estado = 'Disponible'")
    Long maquinasOperativas();

    @Query("SELECT COUNT(m) FROM Maquinaria m WHERE m.estado = 'Mantenimiento'")
    Long maquinasEnMantenimiento();

    @Query("SELECT COUNT(m) FROM Maquinaria m WHERE m.estado = 'Ocupada'")
    Long maquinasInactivas();

    @Query("SELECT SUM(m.horasAcumuladas) FROM Maquinaria m")
    Double horasTotales();

    @Query("""
        SELECT m.maquinaId, m.modelo, m.horasAcumuladas
        FROM Maquinaria m
        ORDER BY m.horasAcumuladas DESC
        LIMIT 5
    """)
    List<Object[]> top5Usadas();
}
