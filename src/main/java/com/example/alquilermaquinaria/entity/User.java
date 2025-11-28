package com.example.alquilermaquinaria.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    // Relación ManyToOne con la entidad Role
    @ManyToOne(fetch = FetchType.EAGER) // Carga Eager para el rol
    @JoinColumn(name = "rol_id", nullable = false)
    private Role role;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(name = "nombre_usuario", unique = true, nullable = false, length = 50)
    private String nombreUsuario;

    // Contraseña hasheada (importante usar un algoritmo de hash seguro)
    @Column(name = "contrasena_hash", nullable = false, length = 255)
    private String contrasenaHash;

    @Column(name = "correo_electronico", unique = true, nullable = false, length = 100)
    private String correoElectronico;

    // Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoUsuario estado = EstadoUsuario.Activo;
}