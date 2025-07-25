package com.example.sistemafarmacia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "venta_detalle")
public class VentaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int idventa;
    private int idproducto;
    private int cantidad;
    
    // Constructor vacío
    public VentaDetalle() {}
    
    // Constructor completo
    public VentaDetalle(int idventa, int idproducto, int cantidad) {
        this.idventa = idventa;
        this.idproducto = idproducto;
        this.cantidad = cantidad;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdventa() {
        return idventa;
    }
    
    public void setIdventa(int idventa) {
        this.idventa = idventa;
    }
    
    public int getIdproducto() {
        return idproducto;
    }
    
    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
