package com.example.sistemafarmacia.service.serviceImpl;

import com.example.sistemafarmacia.model.dto.ReniecResponseDTO;
import com.example.sistemafarmacia.service.ReniecService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ReniecServiceImpl implements ReniecService {
    
    @Value("${reniec.api.base-url:https://api.apis.net.pe/}")
    private String baseUrl;
    
    @Value("${reniec.api.token:apis-token-17334.jfOUrNQgv6yVyt4aGW4yMYshRbLy4zhA}")
    private String apiToken;
    
    @Value("${reniec.api.timeout:5000}")
    private int timeout;
    
    private final RestTemplate restTemplate;
    private static final Pattern DNI_PATTERN = Pattern.compile("^[0-9]{8}$");
    
    public ReniecServiceImpl() {
        this.restTemplate = new RestTemplate();
    }
    
    @Override
    public Optional<ReniecResponseDTO> consultarDni(String dni) {
        System.out.println("🔍 Recibido DNI: '" + dni + "' (longitud: " + dni.length() + ")");
        
        if (!isValidDni(dni)) {
            System.out.println("❌ DNI inválido: '" + dni + "'");
            return Optional.empty();
        }

        try {
            String url = baseUrl + "v2/reniec/dni?numero=" + dni;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            headers.set("Authorization", "Bearer " + apiToken);
            headers.set("Referer", "https://apis.net.pe/consulta-dni-api");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            System.out.println("🌐 Consultando RENIEC para DNI: " + dni);
            System.out.println("🌐 URL completa: " + url);
            System.out.println("🔑 Token completo: " + apiToken);
            
            // Primero obtenemos la respuesta como String para analizarla
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            System.out.println("📡 RENIEC Response Status: " + rawResponse.getStatusCode());
            System.out.println("📡 RENIEC Response Content-Type: " + rawResponse.getHeaders().getContentType());
            
            String responseBody = rawResponse.getBody();
            if (responseBody != null) {
                System.out.println("📡 RENIEC Response Body (first 200 chars): " + 
                    responseBody.substring(0, Math.min(200, responseBody.length())));
                
                // Verificar si la respuesta es HTML (indica problema con la API)
                if (responseBody.trim().startsWith("<!DOCTYPE html>") || 
                    responseBody.trim().startsWith("<html")) {
                    
                    System.out.println("❌ RENIEC API devolvió HTML en lugar de JSON");
                    System.out.println("❌ Esto indica un problema con la URL o el token de acceso");
                    System.out.println("❌ Posibles causas:");
                    System.out.println("   - URL incorrecta");
                    System.out.println("   - Token expirado o inválido");
                    System.out.println("   - Servicio RENIEC no disponible");
                    return Optional.empty();
                }
                
                // Intentar parsear como JSON
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ReniecResponseDTO reniecResponse = mapper.readValue(responseBody, ReniecResponseDTO.class);
                    System.out.println("✅ RENIEC Response exitosa: " + reniecResponse.getNombres() + " " + reniecResponse.getApellidoPaterno());
                    return Optional.of(reniecResponse);
                } catch (Exception jsonException) {
                    System.out.println("❌ Error parsing JSON: " + jsonException.getMessage());
                    System.out.println("❌ Response content: " + responseBody);
                    return Optional.empty();
                }
            } else {
                System.out.println("❌ RENIEC Response body is null");
                return Optional.empty();
            }
            
        } catch (HttpClientErrorException e) {
            System.out.println("❌ RENIEC HttpClientErrorException:");
            System.out.println("   - Status Code: " + e.getStatusCode());
            System.out.println("   - Response Body: " + e.getResponseBodyAsString());
            
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("❌ RENIEC: DNI no encontrado - " + dni);
            } else if (e.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                System.out.println("❌ RENIEC: DNI con formato inválido - " + dni);
            } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                System.out.println("❌ RENIEC: Token inválido o expirado - " + dni);
            } else {
                System.out.println("❌ RENIEC: Error HTTP " + e.getStatusCode() + " para DNI " + dni);
            }
            return Optional.empty();
        } catch (ResourceAccessException e) {
            System.out.println("❌ RENIEC ResourceAccessException: " + e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            System.out.println("❌ RENIEC: Error inesperado - " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    @Override
    public boolean isServiceAvailable() {
        try {
            String url = baseUrl + "v2/reniec/dni?numero=12345678";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");
            headers.set("Authorization", "Bearer " + apiToken);
            headers.set("Referer", "https://apis.net.pe/consulta-dni-api");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                entity, 
                String.class
            );
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isValidDni(String dni) {
        return dni != null && DNI_PATTERN.matcher(dni.trim()).matches();
    }
}
