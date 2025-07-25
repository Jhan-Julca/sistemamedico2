package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Venta;
import com.example.sistemafarmacia.model.VentaDetalle;
import java.util.List;

public interface VentaService {
    List<Venta> getAllVentas();
    Venta getVentaById(int id);
    Venta addVenta(Venta venta, List<VentaDetalle> detalles);
    Venta updateVenta(int id, Venta venta);
    void deleteVenta(int id);
}
