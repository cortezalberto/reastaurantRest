package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO para la entidad Promocion
 * Proporciona información de promociones para la API REST
 */
@Data
@Builder
public class PromocionDto {
    private Long id;
    private String nombre;
    private String denominacion;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private LocalTime horaDesde;
    private LocalTime horaHasta;
    private Double precioDescuento;
    private Double precioPromocional;
    private String tipoPromocion;
    private boolean eliminado;
    private String sucursal;
    private List<ArticuloDto> articulos;
    private Integer cantidadArticulos;
    private boolean activa; // Indica si está activa en este momento
}