package com.tienda.controller;

import com.tienda.model.Producto;
import com.tienda.model.Categoria;
import com.tienda.repository.ProductoRepository;
import com.tienda.repository.CategoriaRepository;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ✅ GET: Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    // ✅ GET: Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        Map<String, Object> response = new HashMap<>();

        if (producto.isPresent()) {
            response.put("producto", producto.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("mensaje", "Producto no encontrado.");
            return ResponseEntity.status(404).body(response);
        }
    }

    // ✅ POST: Crear nuevo producto
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long idCategoria = producto.getCategoria().getId();
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + idCategoria));

            producto.setCategoria(categoria);
            Producto guardado = productoRepository.save(producto);

            response.put("mensaje", "Producto creado exitosamente.");
            response.put("producto", guardado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al guardar el producto.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ PUT: Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        Map<String, Object> response = new HashMap<>();

        if (!productoRepository.existsById(id)) {
            response.put("mensaje", "Producto no encontrado.");
            return ResponseEntity.status(404).body(response);
        }

        try {
            Long idCategoria = producto.getCategoria().getId();
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + idCategoria));

            producto.setId(id);
            producto.setCategoria(categoria);
            Producto actualizado = productoRepository.save(producto);

            response.put("mensaje", "Producto actualizado correctamente.");
            response.put("producto", actualizado);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al actualizar el producto.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ DELETE: Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!productoRepository.existsById(id)) {
            response.put("mensaje", "Producto no encontrado.");
            return ResponseEntity.status(404).body(response);
        }

        try {
            productoRepository.deleteById(id);
            response.put("mensaje", "Producto eliminado correctamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar el producto.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ✅ NUEVO: Obtener productos por ID de categoría
    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable Long id) {
        List<Producto> productos = productoRepository.findByCategoriaId(id);
        return ResponseEntity.ok(productos);
    }
}