package com.example.sistemafarmacia.repository;

import com.example.sistemafarmacia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por email (método principal)
    Optional<Usuario> findByEmail(String email);
    
    // Buscar por username (para compatibilidad)
    Optional<Usuario> findByUsername(String username);
    
    // Buscar por email o username (para flexibilidad)
    @Query("SELECT u FROM Usuario u WHERE u.email = :identifier OR u.username = :identifier")
    Optional<Usuario> findByEmailOrUsername(@Param("identifier") String identifier);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Verificar si existe un username
    boolean existsByUsername(String username);
    
    // Buscar usuarios activos por email
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND (u.activo IS NULL OR u.activo = true)")
    Optional<Usuario> findActiveByEmail(@Param("email") String email);
}