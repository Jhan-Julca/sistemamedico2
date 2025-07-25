package com.example.sistemafarmacia.controller;

import com.example.sistemafarmacia.model.Sede;
import com.example.sistemafarmacia.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sede")
public class SedeController {

    @Autowired
    private SedeService sedeService;

    @GetMapping
    public List<Sede> getAllSedes() {
        return sedeService.getAllSedes();
    }

    @GetMapping("/{id}")
    public Sede getSedeById(@PathVariable Long id) {
        return sedeService.getSedeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Sede saveSede(@RequestBody Sede sede) {
        return sedeService.saveSede(sede);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteSede(@PathVariable Long id) {
        sedeService.deleteSede(id);
    }
}
