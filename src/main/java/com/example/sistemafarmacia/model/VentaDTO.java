package com.example.sistemafarmacia.model;

import java.time.LocalDate;
import java.util.List;

public class VentaDTO {
    private int idcliente;
    private LocalDate fechaRegistro;
    private double precioTotal;
    private List<VentaDetalle> detalles;
    private Sede sede;
    
    // Constructor vacío
    public VentaDTO() {}
    
    // Constructor completo
    public VentaDTO(int idcliente, LocalDate fechaRegistro, double precioTotal, List<VentaDetalle> detalles, Sede sede) {
        this.idcliente = idcliente;
        this.fechaRegistro = fechaRegistro;
        this.precioTotal = precioTotal;
        this.detalles = detalles;
        this.sede = sede;
    }
    
    // Getters y Setters
    public int getIdcliente() {
        return idcliente;
    }
    
    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public double getPrecioTotal() {
        return precioTotal;
    }
    
    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    
    public List<VentaDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }
    
    public Sede getSede() {
        return sede;
    }
    
    public void setSede(Sede sede) {
        this.sede = sede;
    }
}
