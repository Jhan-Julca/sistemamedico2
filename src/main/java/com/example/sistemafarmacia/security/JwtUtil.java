package com.example.sistemafarmacia.security;

import com.example.sistemafarmacia.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    private final long EXPIRATION = 86400000; // 24 horas en milisegundos
    private final long REFRESH_EXPIRATION = 604800000; // 7 días en milisegundos

    // Generar token con información básica (mantiene compatibilidad)
    public String generateToken(String username) {
        return generateToken(username, null);
    }

    // Generar token con información completa del usuario
    public String generateToken(String username, Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        
        if (usuario != null) {
            claims.put("userId", usuario.getId());
            claims.put("nombreCompleto", usuario.getNombreCompleto());
            claims.put("rol", usuario.getRol().name());
            claims.put("email", usuario.getEmail());
            
            if (usuario.getSede() != null) {
                claims.put("sedeId", usuario.getSede().getId());
                claims.put("sedeNombre", usuario.getSede().getNombre());
            }
            
            claims.put("activo", usuario.getActivo());
        }
        
        return createToken(claims, username, EXPIRATION);
    }

    // Generar refresh token
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, username, REFRESH_EXPIRATION);
    }

    // Crear token con claims personalizados
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener clave de firma
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extraer username del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraer fecha de expiración
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraer claim específico
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException("Token no soportado", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token malformado", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    // Verificar si el token ha expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validar token básico
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Validar token con username
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extraer información específica del usuario
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object userId = claims.get("userId");
        return userId != null ? Long.valueOf(userId.toString()) : null;
    }

    public String extractUserRole(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("rol");
    }

    public String extractNombreCompleto(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("nombreCompleto");
    }

    public Long extractSedeId(String token) {
        Claims claims = extractAllClaims(token);
        Object sedeId = claims.get("sedeId");
        return sedeId != null ? Long.valueOf(sedeId.toString()) : null;
    }

    public String extractSedeNombre(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("sedeNombre");
    }

    public Boolean extractActivo(String token) {
        Claims claims = extractAllClaims(token);
        Object activo = claims.get("activo");
        return activo != null ? Boolean.valueOf(activo.toString()) : true;
    }

    // Verificar si es refresh token
    public Boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "refresh".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    // Obtener tiempo restante del token
    public long getTimeUntilExpiration(String token) {
        Date expiration = extractExpiration(token);
        return expiration.getTime() - System.currentTimeMillis();
    }

    // Verificar si el token necesita renovación (menos de 1 hora restante)
    public Boolean shouldRefreshToken(String token) {
        try {
            long timeRemaining = getTimeUntilExpiration(token);
            return timeRemaining < 3600000; // 1 hora en milisegundos
        } catch (Exception e) {
            return true; // Si hay error, mejor renovar
        }
    }
}