package com.tienda.repository;

import com.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Producto.
 * Proporciona métodos CRUD y búsquedas personalizadas.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Busca productos cuyo nombre contenga el texto especificado,
     * sin distinguir entre mayúsculas y minúsculas.
     *
     * @param nombre Fragmento del nombre del producto a buscar.
     * @return Lista de productos que coincidan con el criterio.
     */
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca productos que pertenezcan a una categoría específica.
     *
     * @param categoriaId ID de la categoría.
     * @return Lista de productos de esa categoría.
     */
    List<Producto> findByCategoriaId(Long categoriaId);
}