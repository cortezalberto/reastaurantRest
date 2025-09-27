package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CategoriaDto;
import org.example.dto.CreateCategoriaRequest;
import org.example.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías
 *
 * Implementa las siguientes historias de usuario:
 * - HU-008: Crear Categorías Jerárquicas
 * - HU-009: Consultar Productos por Categoría
 */
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Categorías", description = "API para gestión de categorías de productos del sistema")
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Crear nueva categoría
     */
    @Operation(
            summary = "Crear nueva categoría",
            description = "Registra una nueva categoría en el sistema con posibilidad de subcategorías"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal o categoría padre no encontrada")
    })
    @PostMapping
    public ResponseEntity<CategoriaDto> crearCategoria(
            @Valid @RequestBody CreateCategoriaRequest request) {
        log.info("POST /api/v1/categorias - Creando categoría: {}", request.getNombre());

        CategoriaDto categoriaCreada = categoriaService.crearCategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
    }

    /**
     * Obtener categoría por ID
     */
    @Operation(
            summary = "Obtener categoría por ID",
            description = "Consulta los datos completos de una categoría específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDto> obtenerCategoria(
            @Parameter(description = "ID único de la categoría") @PathVariable Long id) {
        log.info("GET /api/v1/categorias/{} - Consultando categoría", id);

        CategoriaDto categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    /**
     * Listar todas las categorías activas
     */
    @Operation(
            summary = "Listar todas las categorías",
            description = "Obtiene el listado completo de categorías activas del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listarCategorias() {
        log.info("GET /api/v1/categorias - Listando todas las categorías");

        List<CategoriaDto> categorias = categoriaService.listarCategoriasActivas();
        return ResponseEntity.ok(categorias);
    }

    /**
     * Actualizar categoría
     */
    @Operation(
            summary = "Actualizar categoría",
            description = "Modifica los datos de una categoría existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o referencia circular"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizarCategoria(
            @Parameter(description = "ID único de la categoría") @PathVariable Long id,
            @Valid @RequestBody CreateCategoriaRequest request) {
        log.info("PUT /api/v1/categorias/{} - Actualizando categoría", id);

        CategoriaDto categoriaActualizada = categoriaService.actualizarCategoria(id, request);
        return ResponseEntity.ok(categoriaActualizada);
    }

    /**
     * Eliminar categoría
     */
    @Operation(
            summary = "Eliminar categoría",
            description = "Realiza eliminación lógica de una categoría (marca como eliminada)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID único de la categoría") @PathVariable Long id) {
        log.info("DELETE /api/v1/categorias/{} - Eliminando categoría", id);

        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar categorías por nombre
     */
    @Operation(
            summary = "Buscar categorías por nombre",
            description = "Busca categorías que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<CategoriaDto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String nombre) {
        log.info("GET /api/v1/categorias/buscar-por-nombre?nombre={}", nombre);

        List<CategoriaDto> categorias = categoriaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Buscar categorías por sucursal
     */
    @Operation(
            summary = "Buscar categorías por sucursal",
            description = "Obtiene todas las categorías de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-sucursal")
    public ResponseEntity<List<CategoriaDto>> buscarPorSucursal(
            @Parameter(description = "ID de la sucursal") @RequestParam Long sucursalId) {
        log.info("GET /api/v1/categorias/buscar-por-sucursal?sucursalId={}", sucursalId);

        List<CategoriaDto> categorias = categoriaService.buscarPorSucursal(sucursalId);
        return ResponseEntity.ok(categorias);
    }

    /**
     * Obtener categorías principales
     */
    @Operation(
            summary = "Obtener categorías principales",
            description = "Obtiene las categorías que no tienen categoría padre (categorías raíz)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías principales obtenidas exitosamente")
    })
    @GetMapping("/principales")
    public ResponseEntity<List<CategoriaDto>> obtenerCategoriasPrincipales() {
        log.info("GET /api/v1/categorias/principales - Obteniendo categorías principales");

        List<CategoriaDto> categorias = categoriaService.obtenerCategoriasPrincipales();
        return ResponseEntity.ok(categorias);
    }
}