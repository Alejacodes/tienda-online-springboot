package com.tienda.controller;

import com.tienda.model.Categoria;
import com.tienda.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository repo;

    public CategoriaController(CategoriaRepository repo) {
        this.repo = repo;
    }

    // ✅ GET: Listar todas las categorías
    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    // ✅ GET: Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Optional<Categoria> categoria = repo.findById(id);
        Map<String, Object> response = new HashMap<>();

        if (categoria.isPresent()) {
            response.put("categoria", categoria.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("mensaje", "Categoría no encontrada.");
            return ResponseEntity.status(404).body(response);
        }
    }

    // ✅ POST: Crear nueva categoría
    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(@Valid @RequestBody Categoria categoria) {
        Map<String, Object> response = new HashMap<>();

        try {
            Categoria nueva = repo.save(categoria);
            response.put("mensaje", "Categoría creada exitosamente.");
            response.put("categoria", nueva);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al guardar la categoría.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ PUT: Actualizar categoría existente
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
            @Valid @RequestBody Categoria categoria) {
        Map<String, Object> response = new HashMap<>();

        if (!repo.existsById(id)) {
            response.put("mensaje", "Categoría no encontrada.");
            return ResponseEntity.status(404).body(response);
        }

        try {
            categoria.setId(id);
            Categoria actualizada = repo.save(categoria);
            response.put("mensaje", "Categoría actualizada.");
            response.put("categoria", actualizada);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al actualizar la categoría.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ DELETE: Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!repo.existsById(id)) {
            response.put("mensaje", "Categoría no encontrada.");
            return ResponseEntity.status(404).body(response);
        }

        try {
            repo.deleteById(id);
            response.put("mensaje", "Categoría eliminada exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar la categoría.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}