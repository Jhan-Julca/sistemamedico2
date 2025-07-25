package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.VentaDetalle;
import com.example.sistemafarmacia.repository.VentaDetalleRepository;
import com.example.sistemafarmacia.service.VentaDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaDetalleServiceImpl implements VentaDetalleService {

    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<VentaDetalle> findByIdventa(int idventa) {
        return ventaDetalleRepository.findByIdventa(idventa);
    }

    @Override
    public VentaDetalle findById(int id) {
        Optional<VentaDetalle> ventaDetalleOpt = ventaDetalleRepository.findById(id);
        return ventaDetalleOpt.orElse(null);
    }

    @Override
    public List<VentaDetalle> getAllVentas() {
        return ventaDetalleRepository.findAll();
    }

    @Override
    public VentaDetalle getVentaById(int id) {
        Optional<VentaDetalle> ventaDetalleOpt = ventaDetalleRepository.findById(id);
        return ventaDetalleOpt.orElse(null);
    }

    @Override
    public VentaDetalle addVenta(VentaDetalle venta) {
        return ventaDetalleRepository.save(venta);
    }

    @Override
    public void deleteVenta(int id) {
        ventaDetalleRepository.deleteById(id);
    }
}
