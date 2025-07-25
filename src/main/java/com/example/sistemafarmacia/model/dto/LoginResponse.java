package com.example.sistemafarmacia.model.dto;

public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
    private String nombreCompleto;
    private String rol;
    private Long sedeId;
    private String sedeNombre;
    private long expiresIn;

    // Constructor original para compatibilidad
    public LoginResponse(String token) {
        this.token = token;
        this.expiresIn = 86400000; // 24 horas en milisegundos
    }

    // Constructor completo
    public LoginResponse(String token, Long userId, String username, String nombreCompleto, 
                        String rol, Long sedeId, String sedeNombre) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
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

    public String getUsername() {
        return username;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
}