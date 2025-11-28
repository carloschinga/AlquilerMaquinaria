package com.example.alquilermaquinaria.Repository;

import java.util.Optional;
import com.example.alquilermaquinaria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Método necesario para el inicio de sesión
    Optional<User> findByNombreUsuario(String nombreUsuario);

    // Método para la validación de email (si fuera necesario)
    Optional<User> findByCorreoElectronico(String correoElectronico);
}