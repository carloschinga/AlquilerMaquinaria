package com.example.alquilermaquinaria.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alquilermaquinaria.Repository.RoleRepository;
import com.example.alquilermaquinaria.Repository.UserRepository;
import com.example.alquilermaquinaria.dto.*;
import com.example.alquilermaquinaria.entity.EstadoUsuario;
import com.example.alquilermaquinaria.entity.Role;
import com.example.alquilermaquinaria.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- MÉTODOS BÁSICOS ---

    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public Optional<User> login(LoginRequestDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByNombreUsuario(loginDTO.getNombreUsuario());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginDTO.getContrasena(), user.getContrasenaHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> listarTodosLosUsuarios() {
        return userRepository.findAll();
    }

    // --- MÉTODOS DE CREACIÓN Y EDICIÓN ---

    @Override
    @Transactional
    public User registerNewUser(UserRegisterDTO registerDTO) {
        // (Tu lógica actual de registro público)
        if (userRepository.findByNombreUsuario(registerDTO.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        Role role = roleRepository.findById(registerDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = new User();
        user.setNombreCompleto(registerDTO.getNombreCompleto());
        user.setNombreUsuario(registerDTO.getNombreUsuario());
        user.setCorreoElectronico(registerDTO.getCorreoElectronico());
        user.setRole(role);
        user.setEstado(EstadoUsuario.Activo);
        user.setContrasenaHash(passwordEncoder.encode(registerDTO.getContrasena()));

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User crearUsuarioAdministrativo(UserCreateAdminDTO dto) {
        if (userRepository.findByNombreUsuario(dto.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El usuario " + dto.getNombreUsuario() + " ya existe.");
        }
        Role role = roleRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol inválido"));

        User user = new User();
        user.setNombreUsuario(dto.getNombreUsuario());
        user.setNombreCompleto(dto.getNombreCompleto() != null ? dto.getNombreCompleto().toUpperCase() : "NUEVO USUARIO");
        user.setCorreoElectronico(dto.getCorreoElectronico());
        user.setRole(role);
        user.setEstado(EstadoUsuario.Activo);
        user.setContrasenaHash(passwordEncoder.encode("1234")); // Default

        return userRepository.save(user);
    }

    // --- AQUÍ ESTÁ LA MAGIA DE LA EDICIÓN ---
    @Override
    @Transactional
    public User updateUser(Integer userId, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1. Actualizar datos básicos
        user.setNombreCompleto(updateDTO.getNombreCompleto());
        user.setCorreoElectronico(updateDTO.getCorreoElectronico());

        // 2. Actualizar Rol si viene el ID
        if (updateDTO.getRolId() != null) {
            Role role = roleRepository.findById(updateDTO.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRole(role);
        }

        // 3. CAMBIO DE CONTRASEÑA (Solo si no está vacío)
        if (updateDTO.getNuevaContrasena() != null && !updateDTO.getNuevaContrasena().trim().isEmpty()) {
            user.setContrasenaHash(passwordEncoder.encode(updateDTO.getNuevaContrasena()));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("No existe el usuario para eliminar.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public User toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getEstado() == EstadoUsuario.Activo) {
            user.setEstado(EstadoUsuario.Inactivo);
        } else {
            user.setEstado(EstadoUsuario.Activo);
        }
        return userRepository.save(user);
    }

    @Override
    public void changePassword(PasswordChangeDTO changeDTO) {
        // (Manten tu lógica de cambio de clave por el propio usuario si la usas)
    }
}