package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PedidoDto;
import org.example.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de pedidos
 *
 * Implementa las siguientes historias de usuario:
 * - HU-015: Consultar Pedidos del Sistema
 */
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Pedidos", description = "API para gestión de pedidos del sistema")
public class PedidoController {

    private final PedidoService pedidoService;

    /**
     * Obtener pedido por ID
     */
    @Operation(
            summary = "Obtener pedido por ID",
            description = "Consulta los datos completos de un pedido específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> obtenerPedido(
            @Parameter(description = "ID único del pedido") @PathVariable Long id) {
        log.info("GET /api/v1/pedidos/{} - Consultando pedido", id);

        PedidoDto pedido = pedidoService.obtenerPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Listar todos los pedidos activos
     */
    @Operation(
            summary = "Listar todos los pedidos",
            description = "Obtiene el listado completo de pedidos activos del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarPedidos() {
        log.info("GET /api/v1/pedidos - Listando todos los pedidos");

        List<PedidoDto> pedidos = pedidoService.listarPedidosActivos();
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Buscar pedidos por cliente
     */
    @Operation(
            summary = "Buscar pedidos por cliente",
            description = "Obtiene todos los pedidos de un cliente específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-cliente")
    public ResponseEntity<List<PedidoDto>> buscarPorCliente(
            @Parameter(description = "ID del cliente") @RequestParam Long clienteId) {
        log.info("GET /api/v1/pedidos/buscar-por-cliente?clienteId={}", clienteId);

        List<PedidoDto> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Buscar pedidos por estado
     */
    @Operation(
            summary = "Buscar pedidos por estado",
            description = "Obtiene todos los pedidos con un estado específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-estado")
    public ResponseEntity<List<PedidoDto>> buscarPorEstado(
            @Parameter(description = "Estado del pedido") @RequestParam String estado) {
        log.info("GET /api/v1/pedidos/buscar-por-estado?estado={}", estado);

        List<PedidoDto> pedidos = pedidoService.buscarPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Buscar pedidos por fecha
     */
    @Operation(
            summary = "Buscar pedidos por fecha",
            description = "Obtiene todos los pedidos de una fecha específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-fecha")
    public ResponseEntity<List<PedidoDto>> buscarPorFecha(
            @Parameter(description = "Fecha del pedido (YYYY-MM-DD)") @RequestParam LocalDate fecha) {
        log.info("GET /api/v1/pedidos/buscar-por-fecha?fecha={}", fecha);

        List<PedidoDto> pedidos = pedidoService.buscarPorFecha(fecha);
        return ResponseEntity.ok(pedidos);
    }

    /**
     * Buscar pedidos por sucursal
     */
    @Operation(
            summary = "Buscar pedidos por sucursal",
            description = "Obtiene todos los pedidos de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-sucursal")
    public ResponseEntity<List<PedidoDto>> buscarPorSucursal(
            @Parameter(description = "ID de la sucursal") @RequestParam Long sucursalId) {
        log.info("GET /api/v1/pedidos/buscar-por-sucursal?sucursalId={}", sucursalId);

        List<PedidoDto> pedidos = pedidoService.buscarPorSucursal(sucursalId);
        return ResponseEntity.ok(pedidos);
    }

}