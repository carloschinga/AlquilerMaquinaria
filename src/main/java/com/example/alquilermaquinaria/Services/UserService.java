package com.example.alquilermaquinaria.Services;

import java.util.Optional;

import com.example.alquilermaquinaria.dto.LoginRequestDTO;
import com.example.alquilermaquinaria.dto.PasswordChangeDTO;
import com.example.alquilermaquinaria.dto.UserRegisterDTO;
import com.example.alquilermaquinaria.dto.UserUpdateDTO;
import com.example.alquilermaquinaria.entity.User;

public interface UserService {

    // Funcionalidades CRUD y de gestión
    User registerNewUser(UserRegisterDTO registerDTO);

    User updateUser(Integer userId, UserUpdateDTO updateDTO);

    void changePassword(PasswordChangeDTO changeDTO);

    // Funcionalidad de Seguridad
    Optional<User> login(LoginRequestDTO loginDTO);

    // Obtener usuario por ID (utilidad)
    Optional<User> findById(Integer userId);

    User findByUsername(String username);
}
