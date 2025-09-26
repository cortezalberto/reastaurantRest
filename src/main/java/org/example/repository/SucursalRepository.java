package org.example.repository;

import org.example.entidades.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Sucursal
 *
 * Implementa operaciones CRUD y consultas personalizadas para sucursales.
 * Cumple con las historias de usuario HU-004, HU-005, HU-006.
 */
@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    /**
     * HU-005: Obtener sucursales por empresa
     */
    List<Sucursal> findByEmpresaIdAndEliminadoFalse(Long empresaId);

    /**
     * HU-005: Consultar sucursales activas (no eliminadas)
     */
    List<Sucursal> findByEliminadoFalse();

    /**
     * Buscar sucursales por nombre
     */
    List<Sucursal> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    /**
     * HU-035: Consulta geográfica - sucursales por localidad
     */
    @Query("SELECT s FROM Sucursal s " +
           "JOIN s.domicilio d " +
           "JOIN d.localidad l " +
           "WHERE l.nombre = :localidad AND s.eliminado = false")
    List<Sucursal> findByLocalidad(@Param("localidad") String localidad);

    /**
     * Consulta personalizada: Obtener sucursales con información completa
     */
    @Query("SELECT s FROM Sucursal s " +
           "LEFT JOIN FETCH s.empresa " +
           "LEFT JOIN FETCH s.domicilio d " +
           "LEFT JOIN FETCH d.localidad l " +
           "LEFT JOIN FETCH l.provincia p " +
           "LEFT JOIN FETCH p.pais " +
           "LEFT JOIN FETCH s.categorias " +
           "LEFT JOIN FETCH s.promociones " +
           "WHERE s.eliminado = false")
    List<Sucursal> findAllActiveWithCompleteInfo();
}