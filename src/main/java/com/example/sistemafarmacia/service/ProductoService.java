package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Producto;
import java.util.List;

public interface ProductoService {
    // Búsquedas
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorCategoria(String categoria);
    
    // CRUD
    List<Producto> getAllProductos();
    Producto getProductoById(int id);
    Producto addProducto(Producto producto);
    Producto updateProducto(int id, Producto producto);
    void deleteProducto(int id);
}
