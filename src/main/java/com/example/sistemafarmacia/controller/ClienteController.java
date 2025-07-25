package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Cliente;
import com.example.sistemafarmacia.model.dto.ClienteManualDTO;
import com.example.sistemafarmacia.model.dto.ClienteResponseDTO;
import com.example.sistemafarmacia.service.ClienteService;
import com.example.sistemafarmacia.service.ClienteCacheService;
import com.example.sistemafarmacia.service.ReniecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private ClienteCacheService cacheService;
    
    @Autowired
    private ReniecService reniecService;

    // ===== INTEGRACIÓN RENIEC =====
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/test-reniec/{dni}")
    public ResponseEntity<String> testReniec(@PathVariable String dni) {
        System.out.println("🧪 ENDPOINT TEST: Recibido DNI: '" + dni + "' (longitud: " + dni.length() + ")");
        boolean isValid = reniecService.isValidDni(dni);
        var result = reniecService.consultarDni(dni);
        
        String response = String.format(
            "DNI: %s (longitud: %d)\nVálido: %s\nEncontrado en RENIEC: %s", 
            dni, dni.length(), isValid, result.isPresent()
        );
        
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/dni/{dni}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorDni(@PathVariable String dni) {
        System.out.println("🎯 CONTROLADOR: Recibido DNI: '" + dni + "' (longitud: " + dni.length() + ")");
        
        // Validación previa en el controlador
        if (dni == null || dni.length() != 8) {
            ClienteResponseDTO errorResponse = new ClienteResponseDTO(
                false, 
                "DNI debe tener exactamente 8 dígitos. Recibido: " + dni + " (longitud: " + (dni != null ? dni.length() : 0) + ")", 
                null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        ClienteResponseDTO response = clienteService.obtenerOCrearCliente(dni);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/dni-query")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorDniQuery(@RequestParam String dni) {
        System.out.println("🎯 CONTROLADOR QUERY: Recibido DNI: '" + dni + "' (longitud: " + dni.length() + ")");
        
        // Validación previa en el controlador
        if (dni == null || dni.length() != 8) {
            ClienteResponseDTO errorResponse = new ClienteResponseDTO(
                false, 
                "DNI debe tener exactamente 8 dígitos. Recibido: " + dni + " (longitud: " + (dni != null ? dni.length() : 0) + ")", 
                null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        ClienteResponseDTO response = clienteService.obtenerOCrearCliente(dni);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @PostMapping("/manual")
    public ResponseEntity<ClienteResponseDTO> registrarClienteManual(@RequestBody ClienteManualDTO clienteManual) {
        ClienteResponseDTO response = clienteService.registrarClienteManual(clienteManual);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientesPorNombre(@RequestParam String termino) {
        List<Cliente> clientes = clienteService.buscarPorNombre(termino);
        return ResponseEntity.ok(clientes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasCache() {
        Map<String, Object> stats = cacheService.getCacheStats();
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/cache")
    public ResponseEntity<String> limpiarCache() {
        cacheService.clearCache();
        return ResponseEntity.ok("Cache limpiado exitosamente");
    }

    // ===== CRUD TRADICIONAL =====
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable int id) {
        Cliente cliente = clienteService.getClienteById(id);
        return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Cliente addCliente(@RequestBody Cliente cliente) {
        return clienteService.addCliente(cliente);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.updateCliente(id, cliente);
        return updatedCliente != null ? ResponseEntity.ok(updatedCliente) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}
