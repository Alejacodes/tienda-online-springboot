package com.tienda.repository;

import com.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona métodos CRUD y consultas personalizadas usando Spring Data JPA.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario.
     * @return Usuario si existe, de lo contrario null.
     */
    Usuario findByCorreo(String correo);

    /**
     * Verifica si ya existe un usuario con el correo dado.
     *
     * @param correo Correo a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByCorreo(String correo);
}