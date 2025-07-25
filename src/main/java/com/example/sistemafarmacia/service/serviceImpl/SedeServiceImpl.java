package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Sede;
import com.example.sistemafarmacia.repository.SedeRepository;
import com.example.sistemafarmacia.service.SedeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SedeServiceImpl implements SedeService {

    @Autowired
    private SedeRepository sedeRepository;

    @Override
    public List<Sede> getAllSedes() {
        return sedeRepository.findAll();
    }

    @Override
    public Sede saveSede(Sede sede) {
        return sedeRepository.save(sede);
    }

    @Override
    public Sede getSedeById(Long id) {
        Optional<Sede> sedeOpt = sedeRepository.findById(id);
        return sedeOpt.orElse(null);
    }

    @Override
    public void deleteSede(Long id) {
        sedeRepository.deleteById(id);
    }
}
