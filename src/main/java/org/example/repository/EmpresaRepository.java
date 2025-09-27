package org.example.repository;

import org.example.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad Empresa
 *
 * Implementa operaciones CRUD y consultas personalizadas para empresas.
 * Cumple con las historias de usuario HU-001, HU-002, HU-003.
 */
@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    /**
     * HU-002: Consultar empresas activas (no eliminadas)
     */
    List<Empresa> findByEliminadoFalse();

    /**
     * HU-002: Buscar empresa por nombre exacto
     */
    Optional<Empresa> findByNombreAndEliminadoFalse(String nombre);

    /**
     * HU-002: Buscar empresa por CUIL
     */
    Optional<Empresa> findByCuilAndEliminadoFalse(Long cuil);

    /**
     * HU-002: Buscar empresas por razón social (contiene texto)
     */
    List<Empresa> findByRazonSocialContainingIgnoreCaseAndEliminadoFalse(String razonSocial);

    /**
     * Consulta personalizada: Obtener empresas con cantidad de sucursales
     */
    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.sucursales WHERE e.eliminado = false")
    List<Empresa> findAllActiveWithSucursales();

    /**
     * Consulta personalizada: Verificar si existe empresa con CUIL (para validaciones)
     */
    @Query("SELECT COUNT(e) > 0 FROM Empresa e WHERE e.cuil = :cuil AND e.eliminado = false AND (:id IS NULL OR e.id != :id)")
    boolean existsByCuilAndNotId(@Param("cuil") Long cuil, @Param("id") Long id);
}