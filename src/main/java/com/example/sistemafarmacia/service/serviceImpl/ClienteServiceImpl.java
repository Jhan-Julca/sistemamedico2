package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.Cliente;
import com.example.sistemafarmacia.model.dto.ClienteManualDTO;
import com.example.sistemafarmacia.model.dto.ClienteResponseDTO;
import com.example.sistemafarmacia.model.dto.ReniecResponseDTO;
import com.example.sistemafarmacia.repository.ClientesRepository;
import com.example.sistemafarmacia.service.ClienteService;
import com.example.sistemafarmacia.service.ReniecService;
import com.example.sistemafarmacia.service.ClienteCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClientesRepository clientesRepository;
    
    @Autowired
    private ReniecService reniecService;
    
    @Autowired
    private ClienteCacheService cacheService;

    @Override
    public ClienteResponseDTO obtenerOCrearCliente(String dni) {
        // 1. Buscar en BD local
        Optional<Cliente> clienteLocal = clientesRepository.findByDni(dni);
        if (clienteLocal.isPresent()) {
            return new ClienteResponseDTO(true, "Cliente encontrado en BD local", clienteLocal.get());
        }
        
        // 2. Buscar en cache
        Optional<Cliente> clienteCache = cacheService.get(dni);
        if (clienteCache.isPresent()) {
            Cliente cliente = clientesRepository.save(clienteCache.get());
            return new ClienteResponseDTO(true, "Cliente encontrado en cache y guardado", cliente);
        }
        
        // 3. Consultar RENIEC
        Optional<ReniecResponseDTO> reniecData = reniecService.consultarDni(dni);
        if (reniecData.isPresent()) {
            Cliente nuevoCliente = crearClienteDesdeReniec(dni, reniecData.get());
            Cliente clienteGuardado = clientesRepository.save(nuevoCliente);
            cacheService.put(dni, clienteGuardado);
            return new ClienteResponseDTO(true, "Cliente creado desde RENIEC", clienteGuardado);
        }
        
        // 4. Solicitar registro manual
        return new ClienteResponseDTO(false, "DNI no encontrado en RENIEC. Registro manual requerido", null);
    }

    @Override
    public ClienteResponseDTO registrarClienteManual(ClienteManualDTO clienteManual) {
        Cliente cliente = new Cliente();
        cliente.setDni(clienteManual.getDni());
        cliente.setNombres(clienteManual.getNombres());
        cliente.setApellidoPaterno(clienteManual.getApellidoPaterno());
        cliente.setApellidoMaterno(clienteManual.getApellidoMaterno());
        cliente.setTelefono(clienteManual.getTelefono());
        cliente.setFuenteDatos("MANUAL");
        cliente.setFechaRegistro(LocalDateTime.now());
        
        Cliente clienteGuardado = clientesRepository.save(cliente);
        return new ClienteResponseDTO(true, "Cliente registrado manualmente", clienteGuardado);
    }

    @Override
    public List<Cliente> buscarPorNombre(String termino) {
        return clientesRepository.findByNombresContainingIgnoreCaseOrApellidoPaternoContainingIgnoreCase(termino, termino);
    }

    private Cliente crearClienteDesdeReniec(String dni, ReniecResponseDTO reniecData) {
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        cliente.setNombres(reniecData.getNombres());
        cliente.setApellidoPaterno(reniecData.getApellidoPaterno());
        cliente.setApellidoMaterno(reniecData.getApellidoMaterno());
        cliente.setFuenteDatos("RENIEC");
        cliente.setFechaRegistro(LocalDateTime.now());
        return cliente;
    }

    // CRUD Tradicional
    @Override
    public List<Cliente> getAllClientes() {
        return clientesRepository.findAll();
    }

    @Override
    public Cliente getClienteById(int id) {
        return clientesRepository.findById(id).orElse(null);
    }

    @Override
    public Cliente addCliente(Cliente cliente) {
        return clientesRepository.save(cliente);
    }

    @Override
    public Cliente updateCliente(int id, Cliente cliente) {
        if (clientesRepository.existsById(id)) {
            cliente.setId(id);
            return clientesRepository.save(cliente);
        }
        return null;
    }

    @Override
    public void deleteCliente(int id) {
        clientesRepository.deleteById(id);
    }
}
