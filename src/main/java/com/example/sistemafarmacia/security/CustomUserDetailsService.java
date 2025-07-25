package com.example.sistemafarmacia.security;

import com.example.sistemafarmacia.model.Usuario;
import com.example.sistemafarmacia.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si el usuario está activo
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new DisabledException("Usuario deshabilitado: " + username);
        }

        return new CustomUserPrincipal(usuario);
    }

    // Clase interna para representar el principal del usuario
    public static class CustomUserPrincipal implements UserDetails {
        private final Usuario usuario;

        public CustomUserPrincipal(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Agregar rol con prefijo ROLE_
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
            
            // Agregar permisos específicos basados en el rol
            switch (usuario.getRol()) {
                case ADMIN:
                    authorities.add(new SimpleGrantedAuthority("PERM_USER_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_SEDE_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_PRODUCT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_SALE_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_CLIENT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_REPORT_VIEW"));
                    break;
                case ENCARGADO:
                    authorities.add(new SimpleGrantedAuthority("PERM_PRODUCT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_SALE_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_CLIENT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_REPORT_VIEW"));
                    break;
                case VENDEDOR:
                    authorities.add(new SimpleGrantedAuthority("PERM_SALE_CREATE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_CLIENT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_PRODUCT_VIEW"));
                    break;
                case ALMACENERO:
                    authorities.add(new SimpleGrantedAuthority("PERM_PRODUCT_MANAGE"));
                    authorities.add(new SimpleGrantedAuthority("PERM_INVENTORY_MANAGE"));
                    break;
            }
            
            return authorities;
        }

        @Override
        public String getPassword() {
            return usuario.getPassword();
        }

        @Override
        public String getUsername() {
            return usuario.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true; // Implementar lógica de expiración si es necesaria
        }

        @Override
        public boolean isAccountNonLocked() {
            return true; // Implementar lógica de bloqueo si es necesaria
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true; // Implementar lógica de expiración de credenciales si es necesaria
        }

        @Override
        public boolean isEnabled() {
            return usuario.getActivo() != null ? usuario.getActivo() : true;
        }

        // Getter para acceder al usuario completo
        public Usuario getUsuario() {
            return usuario;
        }

        public Long getUserId() {
            return usuario.getId();
        }

        public String getNombreCompleto() {
            return usuario.getNombreCompleto();
        }

        public String getRol() {
            return usuario.getRol().name();
        }

        public Long getSedeId() {
            return usuario.getSede() != null ? usuario.getSede().getId() : null;
        }
    }
}