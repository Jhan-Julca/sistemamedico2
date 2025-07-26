package com.example.sistemafarmacia.model;

import java.time.LocalDate;
import java.util.List;

public class VentaDTO {
    private Integer idCliente; // solo el id
    private String idEmpresa; // solo el RUC
    private LocalDate fechaRegistro;
    private double precioTotal;
    private List<VentaDetalle> detalles;
    private Sede sede;

    public VentaDTO() {}

    public VentaDTO(Integer idCliente, String idEmpresa, LocalDate fechaRegistro, double precioTotal, List<VentaDetalle> detalles, Sede sede) {
        this.idCliente = idCliente;
        this.idEmpresa = idEmpresa;
        this.fechaRegistro = fechaRegistro;
        this.precioTotal = precioTotal;
        this.detalles = detalles;
        this.sede = sede;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    // Validación: solo uno debe estar presente
    public boolean isValid() {
        return (idCliente != null && idEmpresa == null) || (idCliente == null && idEmpresa != null);
    }
}
