package com.example.alquilermaquinaria.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.Maquinaria;

public interface MaquinariaRepository extends JpaRepository<Maquinaria, Integer> {
}
