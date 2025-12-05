package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alquilermaquinaria.entity.ContratoAlquiler;

public interface ReporteRentabilidadRepository extends JpaRepository<ContratoAlquiler, Integer> {

    @Query(
            value = """
            SELECT contrato_id,
                   cliente,
                   maquinaria,
                   fecha_inicio,
                   ingresos_alquiler,
                   costo_choferes,
                   costo_combustible_estimado,
                   utilidad_bruta
            FROM view_reporte_rentabilidad_x_contrato
        """,
            nativeQuery = true
    )
    List<Object[]> reporteRentabilidadPorContrato();
}
