package com.tienda.controller;

import com.tienda.model.Usuario;
import com.tienda.repository.UsuarioRepository;
import com.tienda.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    // ✅ Registrar nuevo usuario con validaciones
    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(@RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        if (usuario.getNombre() == null || usuario.getNombre().isBlank() ||
                usuario.getCorreo() == null || usuario.getCorreo().isBlank() ||
                usuario.getPassword() == null || usuario.getPassword().isBlank()) {

            response.put("mensaje", "Todos los campos son obligatorios.");
            return ResponseEntity.badRequest().body(response);
        }

        if (repo.findByCorreo(usuario.getCorreo()) != null) {
            response.put("mensaje", "El correo ya está registrado.");
            return ResponseEntity.status(409).body(response);
        }

        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario guardado = repo.save(usuario);

            response.put("mensaje", "Usuario registrado exitosamente.");
            response.put("usuario", guardado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al guardar el usuario.");
            return ResponseEntity.status(500).body(response);
        }
    }

    // ✅ Buscar usuario por correo
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Map<String, Object>> buscarPorCorreo(@PathVariable String correo) {
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = repo.findByCorreo(correo);

        if (usuario != null) {
            response.put("usuario", usuario);
            return ResponseEntity.ok(response);
        } else {
            response.put("mensaje", "Usuario no encontrado.");
            return ResponseEntity.status(404).body(response);
        }
    }

    // ✅ Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (repo.existsById(id)) {
            repo.deleteById(id);
            response.put("mensaje", "Usuario eliminado exitosamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("mensaje", "Usuario no encontrado.");
            return ResponseEntity.status(404).body(response);
        }
    }

    // ✅ Corregido: Obtener el usuario autenticado directamente
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioAutenticado(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Map<String, Object> response = new HashMap<>();

        if (usuarioDetails == null) {
            response.put("mensaje", "No se encontró usuario autenticado.");
            return ResponseEntity.status(401).body(response);
        }

        Usuario usuario = usuarioDetails.getUsuario(); // ✅ acceso directo
        response.put("usuario", usuario);
        return ResponseEntity.ok(response);
    }
}