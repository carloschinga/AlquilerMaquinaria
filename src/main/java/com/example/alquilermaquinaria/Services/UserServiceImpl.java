package com.example.alquilermaquinaria.Services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alquilermaquinaria.Repository.RoleRepository;
import com.example.alquilermaquinaria.Repository.UserRepository;
import com.example.alquilermaquinaria.dto.LoginRequestDTO;
import com.example.alquilermaquinaria.dto.PasswordChangeDTO;
import com.example.alquilermaquinaria.dto.UserRegisterDTO;
import com.example.alquilermaquinaria.dto.UserUpdateDTO;
import com.example.alquilermaquinaria.entity.EstadoUsuario; // Necesitas esta dependencia
import com.example.alquilermaquinaria.entity.Role;
import com.example.alquilermaquinaria.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Se inyectará el Bean de BCryptPasswordEncoder

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerNewUser(UserRegisterDTO registerDTO) {
        // 1. Verificar si el usuario ya existe (por nombre de usuario o email)
        if (userRepository.findByNombreUsuario(registerDTO.getNombreUsuario()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // 2. Buscar el Rol
        Role role = roleRepository.findById(registerDTO.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + registerDTO.getRolId()));

        // 3. Crear y configurar la entidad User
        User user = new User();
        user.setNombreCompleto(registerDTO.getNombreCompleto());
        user.setNombreUsuario(registerDTO.getNombreUsuario());
        user.setCorreoElectronico(registerDTO.getCorreoElectronico());
        user.setRole(role);
        user.setEstado(EstadoUsuario.Activo); // Por defecto

        // 4. Hashear y guardar la contraseña
        String hashedPassword = passwordEncoder.encode(registerDTO.getContrasena());
        user.setContrasenaHash(hashedPassword);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Integer userId, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // 1. Aplicar cambios a los campos permitidos
        user.setNombreCompleto(updateDTO.getNombreCompleto());
        user.setCorreoElectronico(updateDTO.getCorreoElectronico());

        // 2. Guardar y retornar
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeDTO changeDTO) {
        User user = userRepository.findByNombreUsuario(changeDTO.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        // 1. Verificar la contraseña actual
        if (!passwordEncoder.matches(changeDTO.getContrasenaActual(), user.getContrasenaHash())) {
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }

        // 2. Hashear y establecer la nueva contraseña
        String newHashedPassword = passwordEncoder.encode(changeDTO.getNuevaContrasena());
        user.setContrasenaHash(newHashedPassword);

        userRepository.save(user);
    }

    @Override
    public Optional<User> login(LoginRequestDTO loginDTO) {
        // 1. Buscar usuario por nombre de usuario
        Optional<User> userOptional = userRepository.findByNombreUsuario(loginDTO.getNombreUsuario());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 2. Verificar la contraseña
            if (passwordEncoder.matches(loginDTO.getContrasena(), user.getContrasenaHash())) {
                // Autenticación exitosa
                return Optional.of(user);
            }
        }

        // Autenticación fallida (usuario no encontrado o contraseña incorrecta)
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
