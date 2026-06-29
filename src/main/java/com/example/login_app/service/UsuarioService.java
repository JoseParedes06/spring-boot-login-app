package com.example.login_app.service;

import com.example.login_app.entity.Usuario;
import com.example.login_app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// @Service marca esta clase como la capa de lógica de negocio
@Service
public class UsuarioService {

    // @Autowired inyecta el repositorio automáticamente (Spring lo crea por nosotros)
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ─── CREATE ──────────────────────────────────────────────────────────────

    public boolean registrar(String username, String password) {
        // Sugerencia:
        // 1. Verificar si el usuario ya existe con existsByUsername()
        //    Si existe, retornar false para indicar que falló
        // 2. Crear un nuevo objeto Usuario
        // 3. Asignar el username y password
        // 4. Guardar el usuario con usuarioRepository.save()
        // 5. Retornar true para indicar éxito
        if (usuarioRepository.existsByUsername(username)) {
            return false;
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuarioRepository.save(usuario);
        return true;
    }

    // ─── READ / LOGIN ─────────────────────────────────────────────────────────

    public Optional<Usuario> iniciarSesion(String username, String password) {
        // Sugerencia:
        // 1. Buscar el usuario por username con findByUsername()
        //    Esto retorna un Optional — puede existir o no
        // 2. Si existe, verificar que la password coincida directamente con equals()
        // 3. Si coincide, retornar el Optional con el usuario
        // 4. Si no coincide o no existe, retornar Optional.empty()
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if (usuario.isEmpty()) {
            return Optional.empty();
        }
        if (usuario.get().getPassword().equals(password)) {
            return usuario;
        } else {
            return Optional.empty();
        }
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    public boolean actualizarUsername(Long id, String nuevoUsername) {
        // Sugerencia:
        // 1. Verificar que el nuevo username no esté ya en uso
        // 2. Buscar el usuario por ID con findById()
        // 3. Cambiar el username y guardar
        // 4. Retornar true si se actualizó, false si no se encontró o el nombre ya existe
        if (usuarioRepository.existsByUsername(nuevoUsername)) {
            return false;
        }
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        usuario.get().setUsername(nuevoUsername);
        usuarioRepository.save(usuario.get());
        return true;
    }

    public boolean actualizarPassword(Long id, String passwordActual, String nuevaPassword) {
        // Sugerencia:
        // 1. Buscar el usuario por ID
        // 2. Verificar que la passwordActual coincida con equals()
        // 3. Asignar la nueva password y guardar
        // 4. Retornar true/false según resultado
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.get().getPassword().equals(passwordActual)) {
            usuario.get().setPassword(nuevaPassword);
            usuarioRepository.save(usuario.get());
            return true;
        }
        return false;
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    public boolean eliminar(Long id, String password) {
        // Sugerencia:
        // 1. Buscar el usuario por ID
        // 2. Verificar que la password coincida con equals()
        // 3. Eliminar con usuarioRepository.delete()
        // 4. Retornar true/false según resultado
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.get().getPassword().equals(password)) {
            usuarioRepository.delete(usuario.get());
            return true;
        }
        return false;
    }

    // ─── EXTRA ────────────────────────────────────────────────────────────────

    public List<Usuario> listarTodos() {
        // Sugerencia:
        // findAll() te da todos los usuarios — ya viene gratis en JpaRepository
        // ¿Para qué podría servir esto? ¿Un panel de admin quizás?
        return  usuarioRepository.findAll();
    }
}
