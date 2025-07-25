package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Sede;
import java.util.List;

public interface SedeService {
    List<Sede> getAllSedes();
    Sede saveSede(Sede sede);
    Sede getSedeById(Long id);
    void deleteSede(Long id);
}
