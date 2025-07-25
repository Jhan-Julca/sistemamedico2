package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.VentaDetalle;
import java.util.List;

public interface VentaDetalleService {
    List<VentaDetalle> findByIdventa(int idventa);
    VentaDetalle findById(int id);
    List<VentaDetalle> getAllVentas();
    VentaDetalle getVentaById(int id);
    VentaDetalle addVenta(VentaDetalle venta);
    void deleteVenta(int id);
}
