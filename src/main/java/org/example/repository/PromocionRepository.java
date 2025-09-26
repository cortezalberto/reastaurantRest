package org.example.repository;

import org.example.entidades.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad Promocion
 */
@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {

    List<Promocion> findByEliminadoFalse();

    List<Promocion> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);

    @Query("SELECT p FROM Promocion p WHERE p.fechaDesde <= :fecha AND p.fechaHasta >= :fecha AND p.eliminado = false")
    List<Promocion> findPromocionesVigentes(@Param("fecha") LocalDate fecha);

    @Query("SELECT p FROM Promocion p WHERE p.horaDesde <= :hora AND p.horaHasta >= :hora AND p.eliminado = false")
    List<Promocion> findPromocionesPorHora(@Param("hora") LocalTime hora);
}