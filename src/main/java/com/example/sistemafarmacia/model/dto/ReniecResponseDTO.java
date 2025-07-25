package com.example.sistemafarmacia.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReniecResponseDTO {
    
    @JsonProperty("nombres")
    private String nombres;
    
    @JsonProperty("apellidoPapackage com.example.sistemafarmacia.model.dto;\r\n" + //
                "\r\n" + //
                "import jakarta.validation.constraints.Email;\r\n" + //
                "import jakarta.validation.constraints.NotBlank;\r\n" + //
                "\r\n" + //
                "public class LoginRequest {\r\n" + //
                "    @NotBlank(message = \"Email es requerido\")\r\n" + //
                "    @Email(message = \"Email debe tener un formato válido\")\r\n" + //
                "    private String email;\r\n" + //
                "    \r\n" + //
                "    @NotBlank(message = \"Password es requerido\")\r\n" + //
                "    private String password;\r\n" + //
                "\r\n" + //
                "    public String getEmail() {\r\n" + //
                "        return email;\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    public void setEmail(String email) {\r\n" + //
                "        this.email = email;\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    public String getPassword() {\r\n" + //
                "        return password;\r\n" + //
                "    }\r\n" + //
                "\r\n" + //
                "    public void setPassword(String password) {\r\n" + //
                "        this.password = password;\r\n" + //
                "    }\r\n" + //
                "    \r\n" + //
                "    // Método de compatibilidad para username (se mapeará al email)\r\n" + //
                "    public String getUsername() {\r\n" + //
                "        return this.email;\r\n" + //
                "    }\r\n" + //
                "    \r\n" + //
                "    public void setUsername(String username) {\r\n" + //
                "        this.email = username;\r\n" + //
                "    }\r\n" + //
                "}terno")
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
