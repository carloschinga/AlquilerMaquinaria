package com.example.alquilermaquinaria.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.alquilermaquinaria.Services.UserService;
import com.example.alquilermaquinaria.dto.LoginRequestDTO; // Si lo usaras explícitamente, aunque Login es manejado por Security
import com.example.alquilermaquinaria.dto.PasswordChangeDTO;
import com.example.alquilermaquinaria.dto.UserCreateAdminDTO;
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
     * POST /api/users/register : Para Agregar Nuevo Usuario (Registro Público)
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO registerDTO) {
        try {
            User newUser = userService.registerNewUser(registerDTO);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
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
     * GET /api/users/me : Obtener datos del usuario logueado actualmente
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

    // METODOS PARA EL PANEL DE GESTIÓN DE USUARIOS (Administracion)

    /**
     * GET /api/users : Listar todos los usuarios para la tabla de gestion
     */
    @GetMapping
    public ResponseEntity<List<User>> listarTodos() {
        return ResponseEntity.ok(userService.listarTodosLosUsuarios());
    }

    /**
     * POST /api/users/admin-create : Crear usuario desde el panel administrativo (con Rol específico)
     */
    @PostMapping("/admin-create")
    public ResponseEntity<?> crearUsuarioAdmin(@RequestBody UserCreateAdminDTO dto) {
        try {
            User nuevo = userService.crearUsuarioAdministrativo(dto);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * DELETE /api/users/{id} : Eliminar usuario del sistema
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PATCH /api/users/{id}/toggle-status : Bloquear o Desbloquear acceso al usuario
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id) {
        try {
            User user = userService.toggleUserStatus(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}