package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Cliente;
import com.example.sistemafarmacia.service.ClienteCacheService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClienteCacheServiceImpl implements ClienteCacheService {
    
    private final Map<String, Cliente> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 30 * 60 * 1000; // 30 minutos en milisegundos
    
    @Override
    public Optional<Cliente> get(String dni) {
        if (isExpired(dni)) {
            cache.remove(dni);
            cacheTimestamps.remove(dni);
            return Optional.empty();
        }
        return Optional.ofNullable(cache.get(dni));
    }
    
    @Override
    public void put(String dni, Cliente cliente) {
        cache.put(dni, cliente);
        cacheTimestamps.put(dni, System.currentTimeMillis());
    }
    
    @Override
    public void clearCache() {
        cache.clear();
        cacheTimestamps.clear();
    }
    
    @Override
    public Map<String, Object> getCacheStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("size", cache.size());
        stats.put("entries", cache.keySet());
        
        // Limpiar entradas expiradas
        cleanExpiredEntries();
        
        return stats;
    }
    
    private boolean isExpired(String dni) {
        Long timestamp = cacheTimestamps.get(dni);
        if (timestamp == null) {
            return true;
        }
        return (System.currentTimeMillis() - timestamp) > CACHE_DURATION;
    }
    
    private void cleanExpiredEntries() {
        long currentTime = System.currentTimeMillis();
        cacheTimestamps.entrySet().removeIf(entry -> {
            if ((currentTime - entry.getValue()) > CACHE_DURATION) {
                cache.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}
