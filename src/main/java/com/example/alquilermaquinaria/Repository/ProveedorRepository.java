package com.example.alquilermaquinaria.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}
