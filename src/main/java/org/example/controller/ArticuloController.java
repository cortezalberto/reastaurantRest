package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ArticuloDto;
import org.example.dto.CreateArticuloRequest;
import org.example.service.ArticuloService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de artículos
 *
 * Implementa las siguientes historias de usuario:
 * - HU-006: Gestionar Artículos del Catálogo
 * - HU-007: Consultar Stock de Productos
 */
@RestController
@RequestMapping("/api/v1/articulos")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Artículos", description = "API para gestión de productos e ingredientes del sistema")
public class ArticuloController {

    private final ArticuloService articuloService;

    /**
     * Crear nuevo artículo
     */
    @Operation(
            summary = "Crear nuevo artículo",
            description = "Registra un nuevo artículo en el sistema (insumo o manufacturado)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artículo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Unidad de medida o categoría no encontrada")
    })
    @PostMapping
    public ResponseEntity<ArticuloDto> crearArticulo(
            @Valid @RequestBody CreateArticuloRequest request) {
        log.info("POST /api/v1/articulos - Creando artículo: {}", request.getNombre());

        ArticuloDto articuloCreado = articuloService.crearArticulo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(articuloCreado);
    }

    /**
     * Obtener artículo por ID
     */
    @Operation(
            summary = "Obtener artículo por ID",
            description = "Consulta los datos completos de un artículo específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDto> obtenerArticulo(
            @Parameter(description = "ID único del artículo") @PathVariable Long id) {
        log.info("GET /api/v1/articulos/{} - Consultando artículo", id);

        ArticuloDto articulo = articuloService.obtenerArticuloPorId(id);
        return ResponseEntity.ok(articulo);
    }

    /**
     * Listar todos los artículos activos
     */
    @Operation(
            summary = "Listar todos los artículos",
            description = "Obtiene el listado completo de artículos activos del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de artículos obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ArticuloDto>> listarArticulos() {
        log.info("GET /api/v1/articulos - Listando todos los artículos");

        List<ArticuloDto> articulos = articuloService.listarArticulosActivos();
        return ResponseEntity.ok(articulos);
    }

    /**
     * Actualizar artículo
     */
    @Operation(
            summary = "Actualizar artículo",
            description = "Modifica los datos de un artículo existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticuloDto> actualizarArticulo(
            @Parameter(description = "ID único del artículo") @PathVariable Long id,
            @Valid @RequestBody CreateArticuloRequest request) {
        log.info("PUT /api/v1/articulos/{} - Actualizando artículo", id);

        ArticuloDto articuloActualizado = articuloService.actualizarArticulo(id, request);
        return ResponseEntity.ok(articuloActualizado);
    }

    /**
     * Eliminar artículo
     */
    @Operation(
            summary = "Eliminar artículo",
            description = "Realiza eliminación lógica de un artículo (marca como eliminado)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artículo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarArticulo(
            @Parameter(description = "ID único del artículo") @PathVariable Long id) {
        log.info("DELETE /api/v1/articulos/{} - Eliminando artículo", id);

        articuloService.eliminarArticulo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Buscar artículos por nombre
     */
    @Operation(
            summary = "Buscar artículos por nombre",
            description = "Busca artículos que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<ArticuloDto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String nombre) {
        log.info("GET /api/v1/articulos/buscar-por-nombre?nombre={}", nombre);

        List<ArticuloDto> articulos = articuloService.buscarPorNombre(nombre);
        return ResponseEntity.ok(articulos);
    }

    /**
     * Buscar artículos por tipo
     */
    @Operation(
            summary = "Buscar artículos por tipo",
            description = "Busca artículos según su tipo (INSUMO o MANUFACTURADO)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de artículo inválido")
    })
    @GetMapping("/buscar-por-tipo")
    public ResponseEntity<List<ArticuloDto>> buscarPorTipo(
            @Parameter(description = "Tipo de artículo (INSUMO o MANUFACTURADO)") @RequestParam String tipo) {
        log.info("GET /api/v1/articulos/buscar-por-tipo?tipo={}", tipo);

        List<ArticuloDto> articulos = articuloService.buscarPorTipo(tipo);
        return ResponseEntity.ok(articulos);
    }
}