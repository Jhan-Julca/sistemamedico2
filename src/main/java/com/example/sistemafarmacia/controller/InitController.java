package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Usuario;
import com.example.sistemafarmacia.model.Sede;
import com.example.sistemafarmacia.model.Rol;
import com.example.sistemafarmacia.service.UsuarioService;
import com.example.sistemafarmacia.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/init")
public class InitController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SedeService sedeService;

    @PostMapping("/setup")
    public ResponseEntity<Map<String, String>> initializeSystem() {
        Map<String, String> response = new HashMap<>();
        
        try {
            // Verificar si ya existe un usuario admin
            Usuario existingAdmin = usuarioService.findByUsername("admin");
            if (existingAdmin != null) {
                response.put("message", "Sistema ya inicializado");
                response.put("status", "already_initialized");
                return ResponseEntity.ok(response);
            }

            // Crear sede principal si no existe
            Sede sedePrincipal = new Sede();
            sedePrincipal.setNombre("Sede Principal");
            sedePrincipal.setDireccion("Dirección Principal");
            sedePrincipal.setCiudad("Lima");
            sedePrincipal.setTelefono("999999999");
            sedePrincipal.setActivo(true);
            
            Sede sedeGuardada = sedeService.saveSede(sedePrincipal);

            // Crear usuario administrador
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword("admin123"); // Se encriptará automáticamente en el service
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setEmail("admin@farmacia.com");
            admin.setRol(Rol.ADMIN);
            admin.setSede(sedeGuardada);
            admin.setActivo(true);

            usuarioService.saveUsuario(admin);

            // Crear usuario vendedor de ejemplo
            Usuario vendedor = new Usuario();
            vendedor.setUsername("vendedor");
            vendedor.setPassword("vendedor123");
            vendedor.setNombreCompleto("Vendedor de Ejemplo");
            vendedor.setEmail("vendedor@farmacia.com");
            vendedor.setRol(Rol.VENDEDOR);
            vendedor.setSede(sedeGuardada);
            vendedor.setActivo(true);

            usuarioService.saveUsuario(vendedor);

            response.put("message", "Sistema inicializado correctamente");
            response.put("status", "success");
            response.put("admin_user", "admin");
            response.put("admin_password", "admin123");
            response.put("vendedor_user", "vendedor");
            response.put("vendedor_password", "vendedor123");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Error inicializando sistema: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            Usuario admin = usuarioService.findByUsername("admin");
            boolean isInitialized = admin != null;
            
            status.put("initialized", isInitialized);
            status.put("timestamp", System.currentTimeMillis());
            
            if (isInitialized) {
                status.put("message", "Sistema inicializado");
            } else {
                status.put("message", "Sistema requiere inicialización");
            }

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            status.put("initialized", false);
            status.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(status);
        }
    }
}