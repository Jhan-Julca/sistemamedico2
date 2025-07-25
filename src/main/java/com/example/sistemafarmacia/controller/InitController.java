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
            Usuario existingAdmin = usuarioService.findByEmail("admin@farmacia.com");
            if (existingAdmin != null) {
                response.put("message", "Sistema ya inicializado");
                response.put("status", "already_initialized");
                response.put("admin_email", "admin@farmacia.com");
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
            admin.setEmail("admin@farmacia.com");
            admin.setUsername("admin");
            admin.setPassword("admin123"); // Se encriptará automáticamente en el service
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setRol(Rol.ADMIN);
            admin.setSede(sedeGuardada);
            admin.setActivo(true);

            usuarioService.saveUsuario(admin);

            // Crear usuario vendedor de ejemplo
            Usuario vendedor = new Usuario();
            vendedor.setEmail("vendedor@farmacia.com");
            vendedor.setUsername("vendedor");
            vendedor.setPassword("vendedor123");
            vendedor.setNombreCompleto("Vendedor de Ejemplo");
            vendedor.setRol(Rol.VENDEDOR);
            vendedor.setSede(sedeGuardada);
            vendedor.setActivo(true);

            usuarioService.saveUsuario(vendedor);

            // Crear usuario almacenero de ejemplo
            Usuario almacenero = new Usuario();
            almacenero.setEmail("almacenero@farmacia.com");
            almacenero.setUsername("almacenero");
            almacenero.setPassword("almacenero123");
            almacenero.setNombreCompleto("Almacenero de Ejemplo");
            almacenero.setRol(Rol.ALMACENERO);
            almacenero.setSede(sedeGuardada);
            almacenero.setActivo(true);

            usuarioService.saveUsuario(almacenero);

            // Crear usuario encargado de ejemplo
            Usuario encargado = new Usuario();
            encargado.setEmail("encargado@farmacia.com");
            encargado.setUsername("encargado");
            encargado.setPassword("encargado123");
            encargado.setNombreCompleto("Encargado de Ejemplo");
            encargado.setRol(Rol.ENCARGADO);
            encargado.setSede(sedeGuardada);
            encargado.setActivo(true);

            usuarioService.saveUsuario(encargado);

            response.put("message", "Sistema inicializado correctamente");
            response.put("status", "success");
            response.put("admin_email", "admin@farmacia.com");
            response.put("admin_password", "admin123");
            response.put("vendedor_email", "vendedor@farmacia.com");
            response.put("vendedor_password", "vendedor123");
            response.put("almacenero_email", "almacenero@farmacia.com");
            response.put("almacenero_password", "almacenero123");
            response.put("encargado_email", "encargado@farmacia.com");
            response.put("encargado_password", "encargado123");

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
            Usuario admin = usuarioService.findByEmail("admin@farmacia.com");
            boolean isInitialized = admin != null;
            
            status.put("initialized", isInitialized);
            status.put("timestamp", System.currentTimeMillis());
            
            if (isInitialized) {
                status.put("message", "Sistema inicializado");
                status.put("admin_email", "admin@farmacia.com");
                
                // Contar usuarios por rol
                Map<String, Long> userStats = new HashMap<>();
                userStats.put("total", (long) usuarioService.getAllUsuarios().size());
                userStats.put("activos", (long) usuarioService.getAllActiveUsuarios().size());
                status.put("usuarios", userStats);
            } else {
                status.put("message", "Sistema requiere inicialización");
                status.put("setup_endpoint", "/api/init/setup");
            }

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            status.put("initialized", false);
            status.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(status);
        }
    }

    @GetMapping("/test-users")
    public ResponseEntity<Map<String, Object>> getTestUsers() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, String> users = new HashMap<>();
            users.put("admin", "admin@farmacia.com / admin123");
            users.put("vendedor", "vendedor@farmacia.com / vendedor123");
            users.put("almacenero", "almacenero@farmacia.com / almacenero123");
            users.put("encargado", "encargado@farmacia.com / encargado123");
            
            response.put("users", users);
            response.put("note", "Usar estos usuarios para probar el sistema");
            response.put("login_endpoint", "/api/auth/login");
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}