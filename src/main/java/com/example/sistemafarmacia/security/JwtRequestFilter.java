package com.example.sistemafarmacia.security;

import com.example.sistemafarmacia.service.UsuarioService;
import com.example.sistemafarmacia.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String requestURI = request.getRequestURI();
        
        String username = null;
        String jwt = null;

        // Extraer JWT del header Authorization
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.warn("Error extrayendo username del JWT: " + e.getMessage());
                // Continuar sin establecer autenticación
            }
        }

        // Validar y establecer autenticación
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Validar token
                if (jwtUtil.validateToken(jwt)) {
                    
                    // Verificar que el usuario siga activo en la base de datos
                    Usuario usuario = usuarioService.findByUsername(username);
                    if (usuario != null && (usuario.getActivo() == null || usuario.getActivo())) {
                        
                        // Cargar detalles del usuario
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        // Crear token de autenticación
                        UsernamePasswordAuthenticationToken authToken = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                            );
                        
                        // Establecer detalles adicionales
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        
                        // Agregar información adicional al contexto de seguridad
                        request.setAttribute("userId", jwtUtil.extractUserId(jwt));
                        request.setAttribute("userRole", jwtUtil.extractUserRole(jwt));
                        request.setAttribute("sedeId", jwtUtil.extractSedeId(jwt));
                        
                        // Establecer autenticación en el contexto de seguridad
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        
                        // Log para debug (opcional)
                        logger.debug("Usuario autenticado: " + username + " para URI: " + requestURI);
                        
                    } else {
                        logger.warn("Usuario inactivo o no encontrado: " + username);
                    }
                } else {
                    logger.debug("Token JWT inválido para usuario: " + username);
                }
                
            } catch (Exception e) {
                logger.error("Error procesando JWT: " + e.getMessage());
                // No establecer autenticación en caso de error
            }
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Rutas que no necesitan filtrado JWT
        return path.startsWith("/api/auth/") ||
               path.startsWith("/api/init/") ||
               path.equals("/") ||
               path.startsWith("/static/") ||
               path.startsWith("/public/") ||
               path.contains("swagger") ||
               path.contains("api-docs");
    }
}