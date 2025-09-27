package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.FacturaDto;
import org.example.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para la gestión de facturas
 *
 * Implementa las siguientes historias de usuario:
 * - HU-016: Gestionar Facturación
 * - HU-017: Generar Estadísticas de Ventas
 */
@RestController
@RequestMapping("/api/v1/facturas")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Facturas", description = "API para gestión de facturas del sistema")
public class FacturaController {

    private final FacturaService facturaService;

    /**
     * Obtener factura por ID
     */
    @Operation(
            summary = "Obtener factura por ID",
            description = "Consulta los datos completos de una factura específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> obtenerFactura(
            @Parameter(description = "ID único de la factura") @PathVariable Long id) {
        log.info("GET /api/v1/facturas/{} - Consultando factura", id);

        FacturaDto factura = facturaService.obtenerFacturaPorId(id);
        return ResponseEntity.ok(factura);
    }

    /**
     * Listar todas las facturas activas
     */
    @Operation(
            summary = "Listar todas las facturas",
            description = "Obtiene el listado completo de facturas activas del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<FacturaDto>> listarFacturas() {
        log.info("GET /api/v1/facturas - Listando todas las facturas");

        List<FacturaDto> facturas = facturaService.listarFacturasActivas();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por fecha
     */
    @Operation(
            summary = "Buscar facturas por fecha",
            description = "Obtiene todas las facturas de una fecha específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-fecha")
    public ResponseEntity<List<FacturaDto>> buscarPorFecha(
            @Parameter(description = "Fecha de facturación (YYYY-MM-DD)") @RequestParam LocalDate fecha) {
        log.info("GET /api/v1/facturas/buscar-por-fecha?fecha={}", fecha);

        List<FacturaDto> facturas = facturaService.buscarPorFecha(fecha);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por rango de fechas
     */
    @Operation(
            summary = "Buscar facturas por rango de fechas",
            description = "Obtiene todas las facturas entre dos fechas específicas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-rango-fechas")
    public ResponseEntity<List<FacturaDto>> buscarPorRangoFechas(
            @Parameter(description = "Fecha desde (YYYY-MM-DD)") @RequestParam LocalDate fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)") @RequestParam LocalDate fechaHasta) {
        log.info("GET /api/v1/facturas/buscar-por-rango-fechas?fechaDesde={}&fechaHasta={}", fechaDesde, fechaHasta);

        List<FacturaDto> facturas = facturaService.buscarPorRangoFechas(fechaDesde, fechaHasta);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por forma de pago
     */
    @Operation(
            summary = "Buscar facturas por forma de pago",
            description = "Obtiene todas las facturas con una forma de pago específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-forma-pago")
    public ResponseEntity<List<FacturaDto>> buscarPorFormaPago(
            @Parameter(description = "Forma de pago (EFECTIVO o MERCADOPAGO)") @RequestParam String formaPago) {
        log.info("GET /api/v1/facturas/buscar-por-forma-pago?formaPago={}", formaPago);

        List<FacturaDto> facturas = facturaService.buscarPorFormaPago(formaPago);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Obtener estadísticas de facturación
     */
    @Operation(
            summary = "Obtener estadísticas de facturación",
            description = "Obtiene estadísticas básicas de facturación por fecha"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    @GetMapping("/estadisticas")
    public ResponseEntity<Object> obtenerEstadisticas(
            @Parameter(description = "Fecha desde (YYYY-MM-DD)") @RequestParam LocalDate fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)") @RequestParam LocalDate fechaHasta) {
        log.info("GET /api/v1/facturas/estadisticas?fechaDesde={}&fechaHasta={}", fechaDesde, fechaHasta);

        Object estadisticas = facturaService.obtenerEstadisticas(fechaDesde, fechaHasta);
        return ResponseEntity.ok(estadisticas);
    }

}