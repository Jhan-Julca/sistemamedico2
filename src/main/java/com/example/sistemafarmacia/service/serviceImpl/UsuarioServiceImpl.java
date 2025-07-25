package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Usuario;
import com.example.sistemafarmacia.repository.UsuarioRepository;
import com.example.sistemafarmacia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        // Validar que el email sea único (excepto para el mismo usuario)
        if (usuario.getId() == null && existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con este email: " + usuario.getEmail());
        }
        
        // Si hay username y es diferente al email, validar que sea único
        if (usuario.getUsername() != null && 
            !usuario.getUsername().equals(usuario.getEmail()) && 
            usuario.getId() == null && 
            existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("Ya existe un usuario con este username: " + usuario.getUsername());
        }
        
        // Si no hay username, usar el email
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            usuario.setUsername(usuario.getEmail());
        }
        
        // Encriptar solo si la contraseña es nueva o no está encriptada
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        
        // Asegurar que activo no sea null
        if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Usuario findByEmailOrUsername(String identifier) {
        return usuarioRepository.findByEmailOrUsername(identifier).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    public Usuario findActiveByEmail(String email) {
        return usuarioRepository.findActiveByEmail(email).orElse(null);
    }

    @Override
    public List<Usuario> getAllActiveUsuarios() {
        return usuarioRepository.findAll().stream()
                .filter(Usuario::isActivo)
                .collect(Collectors.toList());
    }

    @Override
    public Usuario activateUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario deactivateUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updatePassword(Long id, String newPassword) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(newPassword));
        return usuarioRepository.save(usuario);
    }
}