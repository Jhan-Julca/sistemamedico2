package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Cliente;
import java.util.Map;
import java.util.Optional;

public interface ClienteCacheService {
    Optional<Cliente> get(String dni);
    void put(String dni, Cliente cliente);
    void clearCache();
    Map<String, Object> getCacheStats();
}
