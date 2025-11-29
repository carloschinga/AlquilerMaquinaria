package com.example.alquilermaquinaria.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
