package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PedidoDto;
import org.example.entidades.Pedido;
import org.example.entidades.Estado;
import org.example.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de pedidos
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    /**
     * Obtener pedido por ID
     */
    @Transactional(readOnly = true)
    public PedidoDto obtenerPedidoPorId(Long id) {
        log.info("Consultando pedido con ID: {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .filter(p -> !p.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));

        return convertirADto(pedido);
    }

    /**
     * Listar todos los pedidos activos
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> listarPedidosActivos() {
        log.info("Consultando todos los pedidos activos");

        List<Pedido> pedidos = pedidoRepository.findByEliminadoFalse();
        return pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar pedidos por cliente
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> buscarPorCliente(Long clienteId) {
        log.info("Buscando pedidos por cliente ID: {}", clienteId);

        List<Pedido> pedidos = pedidoRepository.findByClienteIdAndEliminadoFalse(clienteId);
        return pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar pedidos por estado
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> buscarPorEstado(String estado) {
        log.info("Buscando pedidos por estado: {}", estado);

        Estado estadoEnum = Estado.valueOf(estado.toUpperCase());
        List<Pedido> pedidos = pedidoRepository.findByEstadoAndEliminadoFalse(estadoEnum);
        return pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar pedidos por fecha
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> buscarPorFecha(LocalDate fecha) {
        log.info("Buscando pedidos por fecha: {}", fecha);

        List<Pedido> pedidos = pedidoRepository.findByFechaPedidoAndEliminadoFalse(fecha);
        return pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar pedidos por sucursal
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> buscarPorSucursal(Long sucursalId) {
        log.info("Buscando pedidos por sucursal ID: {}", sucursalId);

        List<Pedido> pedidos = pedidoRepository.findBySucursalIdAndEliminadoFalse(sucursalId);
        return pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private PedidoDto convertirADto(Pedido pedido) {
        return PedidoDto.builder()
                .id(pedido.getId())
                .nombre(pedido.getNombre())
                .fechaPedido(pedido.getFechaPedido())
                .horaEstimadaFinalizacion(pedido.getHoraEstimadaFinalizacion())
                .total(pedido.getTotal())
                .totalCosto(pedido.getTotalCosto())
                .estado(pedido.getEstado() != null ? pedido.getEstado().name() : null)
                .formaPago(pedido.getFormaPago() != null ? pedido.getFormaPago().name() : null)
                .tipoDeEnvio(pedido.getTipoDeEnvio() != null ? pedido.getTipoDeEnvio().name() : null)
                .eliminado(pedido.isEliminado())
                .sucursal(pedido.getSucursal() != null ? pedido.getSucursal().getNombre() : null)
                .detalles(pedido.getDetallePedidos() != null ?
                    pedido.getDetallePedidos().stream()
                        .filter(d -> !d.isEliminado())
                        .map(this::convertirDetallePedidoADto)
                        .collect(Collectors.toList()) : List.of())
                .cantidadItems(pedido.getDetallePedidos() != null ?
                    pedido.getDetallePedidos().stream()
                        .filter(d -> !d.isEliminado())
                        .mapToInt(d -> d.getCantidad())
                        .sum() : 0)
                .build();
    }

    /**
     * Convertir DetallePedido a DTO
     */
    private PedidoDto.DetallePedidoDto convertirDetallePedidoADto(org.example.entidades.DetallePedido detalle) {
        return PedidoDto.DetallePedidoDto.builder()
                .id(detalle.getId())
                .nombre(detalle.getNombre())
                .cantidad(detalle.getCantidad())
                .subTotal(detalle.getSubTotal())
                .articulo(detalle.getArticulo() != null ? detalle.getArticulo().getDenominacion() : null)
                .build();
    }
}