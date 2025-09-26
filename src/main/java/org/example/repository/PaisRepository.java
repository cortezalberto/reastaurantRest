package org.example.repository;

import org.example.entidades.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Pais
 */
@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    List<Pais> findByEliminadoFalse();

    List<Pais> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}