package com.tienda.security;

import com.tienda.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetails para integrar el modelo Usuario con Spring
 * Security.
 */
public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Por ahora no se manejan roles o autoridades.
     * Si se agregan roles en el futuro, deben devolverse aquí.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Agregar roles aquí si los usas
    }

    /**
     * Devuelve la contraseña del usuario (encriptada).
     */
    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    /**
     * Devuelve el nombre de usuario utilizado para iniciar sesión (correo).
     */
    @Override
    public String getUsername() {
        return usuario.getCorreo(); // Se usa el correo como identificador único
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cambia si manejas expiración de cuenta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cambia si manejas bloqueo de cuenta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambia si manejas expiración de contraseña
    }

    @Override
    public boolean isEnabled() {
        return true; // Cambia si manejas activación/desactivación de usuarios
    }

    /**
     * Permite acceder al objeto Usuario original.
     */
    public Usuario getUsuario() {
        return usuario;
    }
}