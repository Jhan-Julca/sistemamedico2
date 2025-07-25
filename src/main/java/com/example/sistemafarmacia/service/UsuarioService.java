package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario saveUsuario(Usuario usuario);
    void deleteUsuario(Long id);
    List<Usuario> getAllUsuarios();
    Usuario findByUsername(String username);
}
