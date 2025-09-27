package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO de request para crear/actualizar artículos
 */
@Data
public class CreateArticuloRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La denominación es obligatoria")
    @Size(min = 2, max = 150, message = "La denominación debe tener entre 2 y 150 caracteres")
    private String denominacion;

    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    private Double precioVenta;

    @NotNull(message = "La unidad de medida es obligatoria")
    private Long unidadMedidaId;

    private Long categoriaId;

    @NotBlank(message = "El tipo de artículo es obligatorio")
    @Pattern(regexp = "INSUMO|MANUFACTURADO", message = "El tipo debe ser INSUMO o MANUFACTURADO")
    private String tipo;

    // Campos específicos para ArticuloInsumo
    @DecimalMin(value = "0.0", message = "El precio de compra no puede ser negativo")
    private Double precioCompra;

    @Min(value = 0, message = "El stock actual no puede ser negativo")
    private Integer stockActual;

    @Min(value = 0, message = "El stock máximo no puede ser negativo")
    private Integer stockMaximo;

    private Boolean esParaElaborar;

    // Campos específicos para ArticuloManufacturado
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Min(value = 1, message = "El tiempo estimado debe ser mayor a 0")
    private Integer tiempoEstimadoMinutos;

    @Size(max = 1000, message = "La preparación no puede exceder 1000 caracteres")
    private String preparacion;
}