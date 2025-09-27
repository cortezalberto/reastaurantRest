package org.example.repository;

import org.example.entidades.Pedido;
import org.example.entidades.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de pedidos
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Buscar todos los pedidos activos (no eliminados) con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.eliminado = false")
    List<Pedido> findByEliminadoFalse();

    /**
     * Buscar pedido por ID con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.id = :id")
    Optional<Pedido> findById(@Param("id") Long id);

    /**
     * Buscar pedidos por cliente y que no estén eliminados con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.cliente.id = :clienteId AND p.eliminado = false")
    List<Pedido> findByClienteIdAndEliminadoFalse(@Param("clienteId") Long clienteId);

    /**
     * Buscar pedidos por estado y que no estén eliminados con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.estado = :estado AND p.eliminado = false")
    List<Pedido> findByEstadoAndEliminadoFalse(@Param("estado") Estado estado);

    /**
     * Buscar pedidos por fecha y que no estén eliminados con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.fechaPedido = :fecha AND p.eliminado = false")
    List<Pedido> findByFechaPedidoAndEliminadoFalse(@Param("fecha") LocalDate fecha);

    /**
     * Buscar pedidos por sucursal y que no estén eliminados con detalles
     */
    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.detallePedidos WHERE p.sucursal.id = :sucursalId AND p.eliminado = false")
    List<Pedido> findBySucursalIdAndEliminadoFalse(@Param("sucursalId") Long sucursalId);
}