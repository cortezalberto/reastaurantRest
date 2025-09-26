package org.example.repository;

import org.example.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Categoria
 *
 * Implementa operaciones CRUD y consultas personalizadas para categorías.
 * Cumple con las historias de usuario HU-012, HU-013, HU-014, HU-015.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * HU-015: Consultar categorías activas (no eliminadas)
     */
    List<Categoria> findByEliminadoFalse();

    /**
     * HU-012: Obtener categorías principales (sin padre)
     */
    List<Categoria> findByCategoriaPadreIsNullAndEliminadoFalse();

    /**
     * HU-013: Obtener subcategorías de una categoría padre
     */
    List<Categoria> findByCategoriaPadreIdAndEliminadoFalse(Long categoriaPadreId);

    /**
     * HU-015: Buscar categorías por nombre
     */
    List<Categoria> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    /**
     * HU-014: Obtener categorías con sus artículos
     */
    @Query("SELECT c FROM Categoria c " +
           "LEFT JOIN FETCH c.articulos a " +
           "WHERE c.eliminado = false AND a.eliminado = false")
    List<Categoria> findAllActiveWithArticulos();

    /**
     * HU-015: Obtener categorías con subcategorías
     */
    @Query("SELECT c FROM Categoria c " +
           "LEFT JOIN FETCH c.subcategorias sub " +
           "WHERE c.categoriaPadre IS NULL AND c.eliminado = false " +
           "ORDER BY c.nombre")
    List<Categoria> findCategoriasRootWithSubcategorias();

}