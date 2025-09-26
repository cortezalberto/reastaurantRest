package org.example.repository;

import org.example.entidades.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad UnidadMedida
 */
@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Long> {

    List<UnidadMedida> findByEliminadoFalse();

    List<UnidadMedida> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}