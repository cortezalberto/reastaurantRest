package org.example.repository;

import org.example.entidades.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Provincia
 */
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

    List<Provincia> findByEliminadoFalse();

    List<Provincia> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    List<Provincia> findByPaisIdAndEliminadoFalse(Long paisId);
}