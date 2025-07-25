package com.example.sistemafarmacia.service;

import com.example.sistemafarmacia.model.Cliente;
import com.example.sistemafarmacia.model.dto.ClienteManualDTO;
import com.example.sistemafarmacia.model.dto.ClienteResponseDTO;
import java.util.List;

public interface ClienteService {
    // Integración RENIEC
    ClienteResponseDTO obtenerOCrearCliente(String dni);
    ClienteResponseDTO registrarClienteManual(ClienteManualDTO clienteManual);
    List<Cliente> buscarPorNombre(String termino);
    
    // CRUD Tradicional
    List<Cliente> getAllClientes();
    Cliente getClienteById(int id);
    Cliente addCliente(Cliente cliente);
    Cliente updateCliente(int id, Cliente cliente);
    void deleteCliente(int id);
}
