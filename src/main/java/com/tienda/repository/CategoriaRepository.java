package com.tienda.repository;

import com.tienda.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Categoria.
 * Proporciona operaciones CRUD y búsquedas personalizadas.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca una categoría por su nombre exacto.
     *
     * @param nombre Nombre de la categoría a buscar.
     * @return Un Optional que contiene la categoría si existe, o vacío si no.
     */
    Optional<Categoria> findByNombre(String nombre);
}