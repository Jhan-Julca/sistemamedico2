package com.example.sistemafarmacia.repository;

import com.example.sistemafarmacia.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
    Optional<Empresa> findByNumeroDocumento(String numeroDocumento);
}
