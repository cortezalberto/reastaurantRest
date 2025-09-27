package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PromocionDto;
import org.example.dto.CreatePromocionRequest;
import org.example.service.PromocionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de promociones
 *
 * Implementa las siguientes historias de usuario:
 * - HU-010: Crear Promociones Temporales
 * - HU-011: Consultar Promociones Activas
 * - HU-018: Analizar Promociones por Tipo
 */
@RestController
@RequestMapping("/api/v1/promociones")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Promociones", description = "API para gestión de promociones y ofertas del sistema")
public class PromocionController {

    private final PromocionService promocionService;

    /**
     * Crear nueva promoción
     */
    @Operation(
            summary = "Crear nueva promoción",
            description = "Registra una nueva promoción en el sistema con fechas, horarios y artículos asociados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Promoción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o fechas/horarios incorrectos"),
            @ApiResponse(responseCode = "404", description = "Sucursal o artículos no encontrados")
    })
    @PostMapping
    public ResponseEntity<PromocionDto> crearPromocion(
            @Valid @RequestBody CreatePromocionRequest request) {
        log.info("POST /api/v1/promociones - Creando promoción: {}", request.getNombre());

        PromocionDto promocionCreada = promocionService.crearPromocion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(promocionCreada);
    }

    /**
     * Obtener promoción por ID
     */
    @Operation(
            summary = "Obtener promoción por ID",
            description = "Consulta los datos completos de una promoción específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción encontrada"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PromocionDto> obtenerPromocion(
            @Parameter(description = "ID único de la promoción") @PathVariable Long id) {
        log.info("GET /api/v1/promociones/{} - Consultando promoción", id);

        PromocionDto promocion = promocionService.obtenerPromocionPorId(id);
        return ResponseEntity.ok(promocion);
    }

    /**
     * Listar todas las promociones activas
     */
    @Operation(
            summary = "Listar todas las promociones",
            description = "Obtiene el listado completo de promociones activas del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de promociones obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PromocionDto>> listarPromociones() {
        log.info("GET /api/v1/promociones - Listando todas las promociones");

        List<PromocionDto> promociones = promocionService.listarPromocionesActivas();
        return ResponseEntity.ok(promociones);
    }

    /**
     * Obtener promociones vigentes
     */
    @Operation(
            summary = "Obtener promociones vigentes",
            description = "Obtiene las promociones que están activas en este momento (dentro de fechas y horarios)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promociones vigentes obtenidas exitosamente")
    })
    @GetMapping("/vigentes")
    public ResponseEntity<List<PromocionDto>> obtenerPromocionesVigentes() {
        log.info("GET /api/v1/promociones/vigentes - Obteniendo promociones vigentes");

        List<PromocionDto> promociones = promocionService.obtenerPromocionesVigentes();
        return ResponseEntity.ok(promociones);
    }

    /**
     * Actualizar promoción
     */
    @Operation(
            summary = "Actualizar promoción",
            description = "Modifica los datos de una promoción existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promoción actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o fechas/horarios incorrectos"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PromocionDto> actualizarPromocion(
            @Parameter(description = "ID único de la promoción") @PathVariable Long id,
            @Valid @RequestBody CreatePromocionRequest request) {
        log.info("PUT /api/v1/promociones/{} - Actualizando promoción", id);

        PromocionDto promocionActualizada = promocionService.actualizarPromocion(id, request);
        return ResponseEntity.ok(promocionActualizada);
    }

    /**
     * Eliminar promoción
     */
    @Operation(
            summary = "Eliminar promoción",
            description = "Realiza eliminación lógica de una promoción (marca como eliminada)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Promoción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Promoción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(
            @Parameter(description = "ID único de la promoción") @PathVariable Long id) {
        log.info("DELETE /api/v1/promociones/{} - Eliminando promoción", id);

        promocionService.eliminarPromocion(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar promociones por nombre
     */
    @Operation(
            summary = "Buscar promociones por nombre",
            description = "Busca promociones que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<PromocionDto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String nombre) {
        log.info("GET /api/v1/promociones/buscar-por-nombre?nombre={}", nombre);

        List<PromocionDto> promociones = promocionService.buscarPorNombre(nombre);
        return ResponseEntity.ok(promociones);
    }

    /**
     * Buscar promociones por tipo
     */
    @Operation(
            summary = "Buscar promociones por tipo",
            description = "Busca promociones según su tipo (HAPPYHOUR o PROMOCION1)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de promoción inválido")
    })
    @GetMapping("/buscar-por-tipo")
    public ResponseEntity<List<PromocionDto>> buscarPorTipo(
            @Parameter(description = "Tipo de promoción (HAPPYHOUR o PROMOCION1)") @RequestParam String tipo) {
        log.info("GET /api/v1/promociones/buscar-por-tipo?tipo={}", tipo);

        List<PromocionDto> promociones = promocionService.buscarPorTipo(tipo);
        return ResponseEntity.ok(promociones);
    }

    /**
     * Buscar promociones por sucursal
     */
    @Operation(
            summary = "Buscar promociones por sucursal",
            description = "Obtiene todas las promociones de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-sucursal")
    public ResponseEntity<List<PromocionDto>> buscarPorSucursal(
            @Parameter(description = "ID de la sucursal") @RequestParam Long sucursalId) {
        log.info("GET /api/v1/promociones/buscar-por-sucursal?sucursalId={}", sucursalId);

        List<PromocionDto> promociones = promocionService.buscarPorSucursal(sucursalId);
        return ResponseEntity.ok(promociones);
    }
}