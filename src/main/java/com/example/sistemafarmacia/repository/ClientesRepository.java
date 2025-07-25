package com.example.sistemafarmacia.repository;

import com.example.sistemafarmacia.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientesRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDni(String dni);
    List<Cliente> findByNombresContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(String nombres, String apellidoPaterno);
}
