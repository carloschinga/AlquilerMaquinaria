package com.example.alquilermaquinaria.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.alquilermaquinaria.entity.Chofer;

public interface ChoferRepository extends JpaRepository<Chofer, Integer> {

    Long countByEstado(String estado);

}
