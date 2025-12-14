package com.example.alquilermaquinaria.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.alquilermaquinaria.entity.ContratoAlquiler;

public interface ContratoAlquilerRepository extends JpaRepository<ContratoAlquiler, Integer> {

    @Query("SELECT c FROM ContratoAlquiler c WHERE c.fechaInicio >= :inicio AND c.fechaFinReal <= :fin")
    List<ContratoAlquiler> findByRangoFechas(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    @Query(value = """
    SELECT c.maquina_id,
       SUM(TIMESTAMPDIFF(HOUR, c.fecha_inicio, COALESCE(c.fecha_fin_real, :fin))) AS horas_uso,
       COUNT(*) AS cantidad_contratos,
       m.modelo
FROM contratos_alquiler c
JOIN maquinaria m ON m.maquina_id = c.maquina_id
WHERE c.fecha_inicio <= :fin
  AND COALESCE(c.fecha_fin_real, :fin) >= :inicio
GROUP BY c.maquina_id, m.modelo;

""", nativeQuery = true)
    List<Object[]> calcularUtilizacionPorRango(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM ContratoAlquiler c")
    Long contarTotalContratos();

    @Query("SELECT COUNT(c) FROM ContratoAlquiler c WHERE c.fechaFinReal IS NULL")
    Long contarContratosActivos();

    @Query("SELECT COUNT(c) FROM ContratoAlquiler c WHERE c.estadoPago = 'Pendiente'")
    Long contarContratosPorCobrar();

    @Query("SELECT COALESCE(SUM(c.montoTotalCobrar), 0) FROM ContratoAlquiler c WHERE c.estadoPago = 'Pendiente'")
    Double sumarMontoPorCobrar();

    @Query("""
    SELECT c FROM ContratoAlquiler c
    LEFT JOIN FETCH c.asignaciones a
    LEFT JOIN FETCH c.cliente
    LEFT JOIN FETCH c.maquinaria
    WHERE c.contratoId = :id
""")
    Optional<ContratoAlquiler> findByIdWithRelations(Integer id);

}
