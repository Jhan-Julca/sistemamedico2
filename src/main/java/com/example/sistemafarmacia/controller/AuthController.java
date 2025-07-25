package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Usuario;
import com.example.sistemafarmacia.model.dto.LoginRequest;
import com.example.sistemafarmacia.model.dto.LoginResponse;
import com.example.sistemafarmacia.security.JwtUtil;
import com.example.sistemafarmacia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Verificar si el usuario existe y está activo
            Usuario usuario = usuarioService.findByEmail(loginRequest.getEmail());
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuario no encontrado"));
            }

            if (usuario.getActivo() != null && !usuario.getActivo()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Usuario inactivo"));
            }

            // Autenticar credenciales
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            // Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            
            // Generar token con información adicional
            String token = jwtUtil.generateToken(userDetails.getUsername(), usuario);
            
            // Crear respuesta con información del usuario
            LoginResponse response = new LoginResponse(
                token,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombreCompleto(),
                usuario.getRol().name(),
                usuario.getSede() != null ? usuario.getSede().getId() : null,
                usuario.getSede() != null ? usuario.getSede().getNombre() : null
            );

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Usuario deshabilitado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Credenciales inválidas"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Error de autenticación: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno del servidor"));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token no proporcionado"));
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null && jwtUtil.validateToken(token)) {
                Usuario usuario = usuarioService.findByEmail(username);
                if (usuario != null && (usuario.getActivo() == null || usuario.getActivo())) {
                    return ResponseEntity.ok(new TokenValidationResponse(
                        true,
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getRol().name(),
                        usuario.getSede() != null ? usuario.getSede().getId() : null
                    ));
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Token inválido"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Error validando token"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Token no proporcionado"));
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null) {
                Usuario usuario = usuarioService.findByEmail(username);
                if (usuario != null && (usuario.getActivo() == null || usuario.getActivo())) {
                    String newToken = jwtUtil.generateToken(username, usuario);
                    return ResponseEntity.ok(new LoginResponse(
                        newToken,
                        usuario.getId(),
                        usuario.getUsername(),
                        usuario.getNombreCompleto(),
                        usuario.getRol().name(),
                        usuario.getSede() != null ? usuario.getSede().getId() : null,
                        usuario.getSede() != null ? usuario.getSede().getNombre() : null
                    ));
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("No se puede renovar el token"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Error renovando token"));
        }
    }

    // Clases internas para respuestas
    public static class ErrorResponse {
        private String message;
        private long timestamp;

        public ErrorResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() { return message; }
        public long getTimestamp() { return timestamp; }
    }

    public static class TokenValidationResponse {
        private boolean valid;
        private Long userId;
        private String username;
        private String role;
        private Long sedeId;

        public TokenValidationResponse(boolean valid, Long userId, String username, String role, Long sedeId) {
            this.valid = valid;
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.sedeId = sedeId;
        }

        // Getters
        public boolean isValid() { return valid; }
        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public Long getSedeId() { return sedeId; }
    }
}