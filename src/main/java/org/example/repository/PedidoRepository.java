package org.example.repository;

import org.example.entidades.Pedido;
import org.example.entidades.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la gestión de pedidos
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Buscar todos los pedidos activos (no eliminados)
     */
    List<Pedido> findByEliminadoFalse();

    /**
     * Buscar pedidos por cliente y que no estén eliminados
     */
    List<Pedido> findByClienteIdAndEliminadoFalse(Long clienteId);

    /**
     * Buscar pedidos por estado y que no estén eliminados
     */
    List<Pedido> findByEstadoAndEliminadoFalse(Estado estado);

    /**
     * Buscar pedidos por fecha y que no estén eliminados
     */
    List<Pedido> findByFechaPedidoAndEliminadoFalse(LocalDate fecha);

    /**
     * Buscar pedidos por sucursal y que no estén eliminados
     */
    List<Pedido> findBySucursalIdAndEliminadoFalse(Long sucursalId);
}