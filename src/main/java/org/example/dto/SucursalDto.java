package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 * DTO para la entidad Sucursal
 * Proporciona información de sucursales para la API REST
 */
@Data
@Builder
public class SucursalDto {
    private Long id;
    private String nombre;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private boolean eliminado;
    private String empresa;
    private DomicilioDto domicilio;
    private List<CategoriaDto> categorias;
    private List<PromocionDto> promociones;
    private Integer cantidadCategorias;
    private Integer cantidadPromociones;
    private boolean abierta; // Indica si está abierta en este momento

    /**
     * DTO para Domicilio
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