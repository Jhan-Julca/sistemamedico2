package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.VentaDetalle;
import com.example.sistemafarmacia.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventadetalle")
public class VentaDetalleController {

    @Autowired
    private VentaDetalleService ventaDetalleService;

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping
    public List<VentaDetalle> getAllVentaDetalles() {
        return ventaDetalleService.getAllVentas();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<VentaDetalle> getVentaDetalleById(@PathVariable int id) {
        VentaDetalle ventaDetalle = ventaDetalleService.getVentaById(id);
        return ventaDetalle != null ? ResponseEntity.ok(ventaDetalle) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/detalle/{idventa}")
    public List<VentaDetalle> getDetallesPorVenta(@PathVariable int idventa) {
        return ventaDetalleService.findByIdventa(idventa);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @PostMapping
    public VentaDetalle addVentaDetalle(@RequestBody VentaDetalle ventaDetalle) {
        return ventaDetalleService.addVenta(ventaDetalle);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVentaDetalle(@PathVariable int id) {
        ventaDetalleService.deleteVenta(id);
        return ResponseEntity.noContent().build();
    }
}
