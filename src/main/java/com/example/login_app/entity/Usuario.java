package com.example.login_app.entity;

import jakarta.persistence.*;
import lombok.Data;

// @Entity le dice a JPA que esta clase representa una tabla en la base de datos
@Entity
// @Table permite especificar el nombre exacto de la tabla en MySQL
@Table(name = "usuarios")
// @Data de Lombok genera automáticamente getters, setters, toString, etc.
@Data
public class Usuario {

    // @Id marca este campo como la llave primaria de la tabla
    // @GeneratedValue hace que el ID se genere automáticamente (auto_increment en MySQL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column permite configurar propiedades de la columna:
    // unique = no puede haber dos usuarios con el mismo nombre
    // nullable = no puede estar vacío
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    // Sugerencia: ¿qué otros campos podría tener un usuario?
    // Por ejemplo: email, fecha de creación, rol (admin/user), etc.
}
