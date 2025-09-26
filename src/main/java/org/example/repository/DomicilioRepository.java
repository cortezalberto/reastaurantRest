package org.example.repository;

import org.example.entidades.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Domicilio
 */
@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, Long> {

    List<Domicilio> findByEliminadoFalse();

    List<Domicilio> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    List<Domicilio> findByLocalidadIdAndEliminadoFalse(Long localidadId);
}