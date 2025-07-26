package com.example.sistemafarmacia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "idcliente")
    private Cliente cliente;

    // Relación opcional con Empresa (puede ser null si es venta a cliente)
    @ManyToOne
    @JoinColumn(name = "idempresa")
    private Empresa empresa;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @Column(name = "precio_total")
    private double precioTotal;

    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede sede;

    // Constructor vacío
    public Venta() {}

    // Constructor para cliente
    public Venta(Cliente cliente, LocalDate fechaRegistro, double precioTotal) {
        this.cliente = cliente;
        this.fechaRegistro = fechaRegistro;
        this.precioTotal = precioTotal;
    }

    // Constructor para empresa
    public Venta(Empresa empresa, LocalDate fechaRegistro, double precioTotal) {
        this.empresa = empresa;
        this.fechaRegistro = fechaRegistro;
        this.precioTotal = precioTotal;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }
}
