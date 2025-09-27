package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO para la entidad Categoria
 * Proporciona información de categorías para la API REST
 */
@Data
@Builder
public class CategoriaDto {
    private Long id;
    private String nombre;
    private String denominacion;
    private boolean eliminado;
    private CategoriaDto categoriaPadre;
    private List<CategoriaDto> subcategorias;
    private List<ArticuloDto> articulos;
    private String sucursal;
    private Integer cantidadArticulos;
    private Integer cantidadSubcategorias;
}