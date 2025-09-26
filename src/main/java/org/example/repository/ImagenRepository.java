package org.example.repository;

import org.example.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Imagen
 */
@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByEliminadoFalse();

    List<Imagen> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}