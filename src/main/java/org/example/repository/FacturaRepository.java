package org.example.repository;

import org.example.entidades.Factura;
import org.example.entidades.FormaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la gestión de facturas
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Buscar todas las facturas activas (no eliminadas)
     */
    List<Factura> findByEliminadoFalse();

    /**
     * Buscar facturas por fecha de facturación y que no estén eliminadas
     */
    List<Factura> findByFechaFacturacionAndEliminadoFalse(LocalDate fecha);

    /**
     * Buscar facturas por rango de fechas y que no estén eliminadas
     */
    List<Factura> findByFechaFacturacionBetweenAndEliminadoFalse(LocalDate fechaDesde, LocalDate fechaHasta);

    /**
     * Buscar facturas por forma de pago y que no estén eliminadas
     */
    List<Factura> findByFormaPagoAndEliminadoFalse(FormaPago formaPago);
}