package com.example.login_app.repository;

import com.example.login_app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository ya nos da los métodos básicos gratis:
// save(), findById(), findAll(), deleteById(), existsById(), etc.
// El primer tipo <Usuario> es la entidad, el segundo <Long> es el tipo del ID
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring genera la consulta SQL automáticamente según el nombre del metodo.
    // Sugerencia: buscar un usuario por su nombre de usuario
     Optional<Usuario> findByUsername(String username);

    // Sugerencia: verificar si ya existe un usuario con ese nombre (útil al registrar)
     boolean existsByUsername(String username);

    // Sugerencia: ¿qué otras consultas podrías necesitar?
    // Por ejemplo: buscar por email, buscar usuarios activos, etc.
}
