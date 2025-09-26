package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO para la entidad Articulo
 * Proporciona información de productos para la API REST
 */
@Data
@Builder
public class ArticuloDto {
    private Long id;
    private String nombre;
    private String denominacion;
    private String descripcion;
    private Double precioVenta;
    private boolean eliminado;
    private String tipo; // "INSUMO" o "MANUFACTURADO"
    private UnidadMedidaDto unidadMedida;

    // Campos específicos para ArticuloInsumo
    private Double precioCompra;
    private Integer stockActual;
    private Integer stockMaximo;
    private Boolean esParaElaborar;

    // Campos específicos para ArticuloManufacturado
    private Integer tiempoEstimadoMinutos;
    private String preparacion;
    private List<ArticuloManufacturadoDetalleDto> detalles;

    /**
     * DTO para UnidadMedida
     */
    @Data
    @Builder
    public static class UnidadMedidaDto {
        private Long id;
        private String nombre;
        private String denominacion;
    }

    /**
     * DTO para detalles de artículo manufacturado (receta)
     */
    @Data
    @Builder
    public static class ArticuloManufacturadoDetalleDto {
        private Long id;
        private String nombre;
        private Integer cantidad;
        private String articuloInsumo;
    }
}