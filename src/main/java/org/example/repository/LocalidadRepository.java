package org.example.repository;

import org.example.entidades.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Localidad
 */
@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    List<Localidad> findByEliminadoFalse();

    List<Localidad> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    List<Localidad> findByProvinciaIdAndEliminadoFalse(Long provinciaId);
}