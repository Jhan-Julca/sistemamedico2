package com.example.sistemafarmacia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReniecResponseDTO {
    
    @JsonProperty("nombres")
    private String nombres;
    
    @JsonProperty("apellidoPaterno")
    private String apellidoPaterno;
    
    @JsonProperty("apellidoMaterno")
    private String apellidoMaterno;
    
    @JsonProperty("nombreCompleto")
    private String nombreCompleto;
    
    @JsonProperty("tipoDocumento")
    private String tipoDocumento;
    
    @JsonProperty("numeroDocumento")
    private String numeroDocumento;
    
    @JsonProperty("digitoVerificador")
    private String digitoVerificador;
    
    // Constructor vacío
    public ReniecResponseDTO() {}
    
    // Constructor completo
    public ReniecResponseDTO(String nombres, String apellidoPaterno, String apellidoMaterno, 
                           String nombreCompleto, String tipoDocumento, String numeroDocumento, String digitoVerificador) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.nombreCompleto = nombreCompleto;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.digitoVerificador = digitoVerificador;
    }
    
    // Getters y Setters
    public String getNombres() {
        return nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    
    public String getTipoDocumento() {
        return tipoDocumento;
    }
    
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getDigitoVerificador() {
        return digitoVerificador;
    }
    
    public void setDigitoVerificador(String digitoVerificador) {
        this.digitoVerificador = digitoVerificador;
    }
}
