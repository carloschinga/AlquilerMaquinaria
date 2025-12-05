package com.example.alquilermaquinaria.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.alquilermaquinaria.entity.Proveedor;

public interface ReportesRepository extends JpaRepository<Proveedor, Integer> {

    @Query(
            value = """
              SELECT proveedor_id, nombre_proveedor, monto_pendiente_pago, numero_facturas_pendientes
              FROM view_cuenta_x_pagar_a_proveedores_de_combustible
              """,
            nativeQuery = true
    )
    List<Object[]> reporteCuentaProveedores();
}
