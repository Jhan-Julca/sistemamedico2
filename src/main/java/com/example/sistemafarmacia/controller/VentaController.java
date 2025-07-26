package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Producto;
import com.example.sistemafarmacia.model.VentaDetalle;
import com.example.sistemafarmacia.service.ProductoService;
import com.example.sistemafarmacia.service.VentaService;
import com.example.sistemafarmacia.model.Venta;
import com.example.sistemafarmacia.model.VentaDTO;
import com.example.sistemafarmacia.model.Cliente;
import com.example.sistemafarmacia.model.Empresa;
import com.example.sistemafarmacia.service.ClienteService;
import com.example.sistemafarmacia.service.EmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venta")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpresaService empresaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaService.getAllVentas();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable int id) {
        Venta venta = ventaService.getVentaById(id);
        return venta != null ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @PostMapping
    public ResponseEntity<?> addVenta(@RequestBody VentaDTO ventaDTO) {
        // Validar que solo uno esté presente
        if (!ventaDTO.isValid()) {
            return ResponseEntity.badRequest().body("Debe enviar solo idCliente o idEmpresa, no ambos ni ninguno.");
        }

        Venta venta = new Venta();
        venta.setFechaRegistro(ventaDTO.getFechaRegistro());
        venta.setPrecioTotal(ventaDTO.getPrecioTotal());
        venta.setSede(ventaDTO.getSede());

        if (ventaDTO.getIdCliente() != null) {
            Cliente cliente = clienteService.getClienteById(ventaDTO.getIdCliente());
            if (cliente == null) {
                return ResponseEntity.badRequest().body("Cliente no encontrado");
            }
            venta.setCliente(cliente);
        } else if (ventaDTO.getIdEmpresa() != null) {
            Empresa empresa = empresaService.obtenerOCrearEmpresa(ventaDTO.getIdEmpresa());
            if (empresa == null) {
                return ResponseEntity.badRequest().body("Empresa no encontrada o inválida");
            }
            venta.setEmpresa(empresa);
        }

        ventaService.addVenta(venta, ventaDTO.getDetalles());

        // Actualizar inventario de productos
        for (VentaDetalle detalle : ventaDTO.getDetalles()) {
            Producto producto = productoService.getProductoById(detalle.getIdproducto());
            if (producto != null) {
                producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
                productoService.updateProducto(detalle.getIdproducto(), producto);
            }
        }

        return ResponseEntity.ok(venta);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable int id, @RequestBody Venta venta) {
        Venta ventaActualizada = ventaService.updateVenta(id, venta);
        return ventaActualizada != null ? ResponseEntity.ok(ventaActualizada) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable int id) {
        ventaService.deleteVenta(id);
        return ResponseEntity.ok().build();
    }
}
