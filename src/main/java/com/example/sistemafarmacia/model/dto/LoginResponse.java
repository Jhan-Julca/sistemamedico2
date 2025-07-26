package com.example.sistemafarmacia.model.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private String email;
    private String username; // Mantener para compatibilidad
    private String nombreCompleto;
    private java.util.Set<com.example.sistemafarmacia.model.Rol> roles;
    private Long sedeId;
    private String sedeNombre;
    private long expiresIn;
    private String tokenType = "Bearer";

    // Constructor original para compatibilidad
    public LoginResponse(String token) {
        this.token = token;
        this.expiresIn = 86400000; // 24 horas en milisegundos
    }

    // Constructor completo actualizado para múltiples roles
    public LoginResponse(String token, Long userId, String email, String nombreCompleto, 
                        java.util.Set<com.example.sistemafarmacia.model.Rol> roles, Long sedeId, String sedeNombre) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.username = email; // Username será igual al email para compatibilidad
        this.nombreCompleto = nombreCompleto;
        this.roles = roles;
        this.sedeId = sedeId;
        this.sedeNombre = sedeNombre;
        this.expiresIn = 86400000; // 24 horas en milisegundos
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username != null ? username : email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public java.util.Set<com.example.sistemafarmacia.model.Rol> getRoles() {
        return roles;
    }

    public void setRoles(java.util.Set<com.example.sistemafarmacia.model.Rol> roles) {
        this.roles = roles;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public String getSedeNombre() {
        return sedeNombre;
    }

    public void setSedeNombre(String sedeNombre) {
        this.sedeNombre = sedeNombre;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}