package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.dto.ReniecResponseDTO;
import java.util.Optional;

public interface ReniecService {
    Optional<ReniecResponseDTO> consultarDni(String dni);
    boolean isServiceAvailable();
    boolean isValidDni(String dni);
}
