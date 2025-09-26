package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO para la entidad Empresa
 * Proporciona información básica de la empresa para la API REST
 */
@Data
@Builder
public class EmpresaDto {
    private Long id;
    private String nombre;
    private String razonSocial;
    private Integer cuil;
    private boolean eliminado;
    private List<SucursalDto> sucursales;
    private int cantidadSucursales;

    /**
     * DTO simplificado para sucursal dentro de empresa
     */
    @Data
    @Builder
    public static class SucursalDto {
        private Long id;
        private String nombre;
        private String horarioApertura;
        private String horarioCierre;
        private String direccion;
        private int cantidadCategorias;
        private int cantidadPromociones;
    }
}