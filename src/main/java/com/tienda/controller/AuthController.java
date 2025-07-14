package com.tienda.controller;

import com.tienda.model.Usuario;
import com.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registrar(@Valid @RequestBody Usuario usuario) {
        Map<String, Object> response = new HashMap<>();

        if (usuarioRepository.findByCorreo(usuario.getCorreo()) != null) {
            response.put("mensaje", "Ya existe un usuario con ese correo.");
            return ResponseEntity.status(409).body(response);
        }

        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            Usuario guardado = usuarioRepository.save(usuario);

            response.put("mensaje", "Usuario registrado exitosamente.");
            response.put("usuario", guardado);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al registrar el usuario.");
            return ResponseEntity.status(500).body(response);
        }
    }
}