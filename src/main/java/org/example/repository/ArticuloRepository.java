package org.example.repository;

import org.example.entidades.Articulo;
import org.example.entidades.ArticuloInsumo;
import org.example.entidades.ArticuloManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Articulo
 *
 * Implementa operaciones CRUD y consultas personalizadas para artículos.
 * Cumple con las historias de usuario HU-007, HU-008, HU-009, HU-010, HU-011.
 */
@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Long> {

    /**
     * HU-009: Consultar artículos activos (no eliminados)
     */
    List<Articulo> findByEliminadoFalse();

    /**
     * HU-011: Buscar artículos por precio de venta exacto
     */
    List<Articulo> findByPrecioVentaAndEliminadoFalse(Double precioVenta);

    /**
     * HU-011: Buscar artículos por rango de precios
     */
    List<Articulo> findByPrecioVentaBetweenAndEliminadoFalse(Double precioMin, Double precioMax);

    /**
     * HU-009: Buscar artículos por nombre (contiene texto)
     */
    List<Articulo> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    /**
     * Consulta específica para ArticuloInsumo con stock bajo
     */
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.stockActual < (ai.stockMaximo * 0.2) AND ai.eliminado = false")
    List<ArticuloInsumo> findInsumosConStockBajo();

    /**
     * HU-009: Obtener solo insumos para elaboración
     */
    @Query("SELECT ai FROM ArticuloInsumo ai WHERE ai.esParaElaborar = true AND ai.eliminado = false")
    List<ArticuloInsumo> findInsumosParaElaborar();

    /**
     * HU-008: Obtener artículos manufacturados con tiempo de preparación
     */
    @Query("SELECT am FROM ArticuloManufacturado am WHERE am.tiempoEstimadoMinutos <= :maxTiempo AND am.eliminado = false")
    List<ArticuloManufacturado> findManufacturadosPorTiempo(@Param("maxTiempo") Integer maxTiempo);

    /**
     * Consulta personalizada: Obtener artículos con información completa
     */
    @Query("SELECT a FROM Articulo a " +
           "LEFT JOIN FETCH a.unidadMedida " +
           "WHERE a.eliminado = false " +
           "ORDER BY a.nombre")
    List<Articulo> findAllActiveWithUnidadMedida();

    /**
     * HU-009: Análisis de productos por precio
     */
    @Query("SELECT a FROM Articulo a WHERE a.eliminado = false ORDER BY a.precioVenta DESC")
    List<Articulo> findAllActiveOrderByPrecioDesc();

}