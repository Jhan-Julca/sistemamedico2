package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Empresa;
import com.example.sistemafarmacia.repository.EmpresaRepository;
import com.example.sistemafarmacia.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Value("${reniec.api.base-url}")
    private String baseUrl;

    @Value("${reniec.api.token}")
    private String token;

    @Override
    public Empresa obtenerOCrearEmpresa(String ruc) {
        Optional<Empresa> empresaOpt = empresaRepository.findByNumeroDocumento(ruc);
        if (empresaOpt.isPresent()) {
            return empresaOpt.get();
        }

        // Consultar SUNAT
        String url = baseUrl + "v2/sunat/ruc/full?numero=" + ruc;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Empresa> response = restTemplate.exchange(url, HttpMethod.GET, entity, Empresa.class);

        Empresa empresa = response.getBody();
        if (empresa != null && empresa.getRazonSocial() != null) {
            empresa.setNumeroDocumento(ruc); // Asegura que el RUC esté seteado
            empresaRepository.save(empresa);
            return empresa;
        }
        return null;
    }
}
