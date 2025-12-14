package com.example.alquilermaquinaria.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.UserService;
import com.example.alquilermaquinaria.dto.PasswordChangeDTO;
import com.example.alquilermaquinaria.dto.UserRegisterDTO;
import com.example.alquilermaquinaria.dto.UserUpdateDTO;
import com.example.alquilermaquinaria.entity.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users/register : Para Agregar Nuevo Usuario
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO registerDTO) {
        try {
            User newUser = userService.registerNewUser(registerDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Manejo de errores (ej. usuario ya existe, rol no encontrado)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /api/users/{userId} : Para Modificar Usuario
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @RequestBody UserUpdateDTO updateDTO) {
        try {
            User updatedUser = userService.updateUser(userId, updateDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PATCH /api/users/change-password : Para Cambiar Contraseña
     */
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO changeDTO) {
        try {
            userService.changePassword(changeDTO);
            return new ResponseEntity<>("Contraseña actualizada con éxito.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /api/users/login : Para Inicio de Sesión
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUsuarioLogueado(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        User user = userService.findByUsername(auth.getName());
        return ResponseEntity.ok(Map.of(
                "username", user.getNombreUsuario(),
                "rol", user.getRole().getNombreRol()
        ));
    }

}
