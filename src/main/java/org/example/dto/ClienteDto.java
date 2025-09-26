package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para la entidad Cliente
 * Proporciona información del cliente para la API REST
 */
@Data
@Builder
public class ClienteDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private boolean eliminado;
    private UsuarioDto usuario;
    private List<DomicilioDto> domicilios;
    private int cantidadPedidos;

    /**
     * DTO simplificado para usuario dentro de cliente
     */
    @Data
    @Builder
    public static class UsuarioDto {
        private Long id;
        private String nombre;
        private String username;
        private String auth0Id;
    }

    /**
     * DTO simplificado para domicilio dentro de cliente
     */
    @Data
    @Builder
    public static class DomicilioDto {
        private Long id;
        private String nombre;
        private Integer numero;
        private Integer cp;
        private String localidad;
        private String provincia;
        private String pais;
    }
}