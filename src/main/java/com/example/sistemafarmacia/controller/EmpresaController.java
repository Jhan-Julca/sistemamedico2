package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Empresa;
import com.example.sistemafarmacia.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN') or hasRole('VENDEDOR')")
@RequestMapping("/api/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<Empresa> obtenerEmpresaPorRuc(@PathVariable String ruc) {
        Empresa empresa = empresaService.obtenerOCrearEmpresa(ruc);
        if (empresa != null) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
