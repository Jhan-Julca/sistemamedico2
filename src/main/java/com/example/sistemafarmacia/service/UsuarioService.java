package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario saveUsuario(Usuario usuario);
    void deleteUsuario(Long id);
    List<Usuario> getAllUsuarios();
    
    // Métodos para buscar por diferentes criterios
    Usuario findByUsername(String username);
    Usuario findByEmail(String email);
    Usuario findByEmailOrUsername(String identifier);
    
    // Métodos para verificar existencia
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
    // Métodos para usuarios activos
    Usuario findActiveByEmail(String email);
    List<Usuario> getAllActiveUsuarios();
    
    // Métodos para gestión de usuarios
    Usuario activateUsuario(Long id);
    Usuario deactivateUsuario(Long id);
    Usuario updatePassword(Long id, String newPassword);
}