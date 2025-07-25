package com.example.sistemafarmacia.model.dto;

import com.example.sistemafarmacia.model.Cliente;

public class ClienteResponseDTO {
    private boolean success;
    private String message;
    private Cliente cliente;
    
    public ClienteResponseDTO() {}
    
    public ClienteResponseDTO(boolean success, String message, Cliente cliente) {
        this.success = success;
        this.message = message;
        this.cliente = cliente;
    }
    
    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
