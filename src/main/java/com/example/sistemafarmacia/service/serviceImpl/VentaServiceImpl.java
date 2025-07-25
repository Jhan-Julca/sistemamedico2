package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Venta;
import com.example.sistemafarmacia.model.VentaDetalle;
import com.example.sistemafarmacia.repository.VentaRepository;
import com.example.sistemafarmacia.repository.VentaDetalleRepository;
import com.example.sistemafarmacia.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta getVentaById(int id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        return ventaOpt.orElse(null);
    }

    @Override
    @Transactional
    public Venta addVenta(Venta venta, List<VentaDetalle> detalles) {
        // Guardar la venta
        Venta nuevaVenta = ventaRepository.save(venta);

        // Guardar los detalles de la venta
        for (VentaDetalle detalle : detalles) {
            detalle.setIdventa(nuevaVenta.getId());
            ventaDetalleRepository.save(detalle);
        }

        return nuevaVenta;
    }

    @Override
    public Venta updateVenta(int id, Venta venta) {
        if (ventaRepository.existsById(id)) {
            venta.setId(id);
            return ventaRepository.save(venta);
        }
        return null;
    }

    @Override
    public void deleteVenta(int id) {
        ventaRepository.deleteById(id);
    }
}
