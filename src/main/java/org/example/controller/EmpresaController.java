package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CreateEmpresaRequest;
import org.example.dto.EmpresaDto;
import org.example.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de empresas
 *
 * Expone endpoints REST para las historias de usuario:
 * - HU-001: Crear Nueva Empresa
 * - HU-002: Consultar Información de Empresa
 * - HU-003: Actualizar Datos de Empresa
 */
@RestController
@RequestMapping("/api/v1/empresas")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Empresas", description = "API para gestión de empresas del sistema")
public class EmpresaController {

    private final EmpresaService empresaService;

    /**
     * HU-001: Crear Nueva Empresa
     *
     * POST /api/v1/empresas
     */
    @Operation(
            summary = "Crear nueva empresa",
            description = "Registra una nueva empresa en el sistema con nombre, razón social y CUIL"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe empresa con el mismo CUIL")
    })
    @PostMapping
    public ResponseEntity<EmpresaDto> crearEmpresa(
            @Valid @RequestBody CreateEmpresaRequest request) {
        log.info("POST /api/v1/empresas - Creando empresa: {}", request.getNombre());

        EmpresaDto empresaCreada = empresaService.crearEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaCreada);
    }

    /**
     * HU-002: Consultar Información de Empresa por ID
     *
     * GET /api/v1/empresas/{id}
     */
    @Operation(
            summary = "Obtener empresa por ID",
            description = "Consulta los datos completos de una empresa específica incluyendo sus sucursales"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDto> obtenerEmpresa(
            @Parameter(description = "ID único de la empresa") @PathVariable Long id) {
        log.info("GET /api/v1/empresas/{} - Consultando empresa", id);

        EmpresaDto empresa = empresaService.obtenerEmpresaPorId(id);
        return ResponseEntity.ok(empresa);
    }

    /**
     * HU-002: Listar todas las empresas activas
     *
     * GET /api/v1/empresas
     */
    @Operation(
            summary = "Listar todas las empresas",
            description = "Obtiene el listado completo de empresas activas del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empresas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<EmpresaDto>> listarEmpresas() {
        log.info("GET /api/v1/empresas - Listando todas las empresas");

        List<EmpresaDto> empresas = empresaService.listarEmpresasActivas();
        return ResponseEntity.ok(empresas);
    }

    /**
     * HU-003: Actualizar Datos de Empresa
     *
     * PUT /api/v1/empresas/{id}
     */
    @Operation(
            summary = "Actualizar empresa",
            description = "Modifica los datos de una empresa existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada"),
            @ApiResponse(responseCode = "409", description = "CUIL ya existe en otra empresa")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDto> actualizarEmpresa(
            @Parameter(description = "ID único de la empresa") @PathVariable Long id,
            @Valid @RequestBody CreateEmpresaRequest request) {
        log.info("PUT /api/v1/empresas/{} - Actualizando empresa", id);

        EmpresaDto empresaActualizada = empresaService.actualizarEmpresa(id, request);
        return ResponseEntity.ok(empresaActualizada);
    }

    /**
     * HU-002: Buscar empresa por nombre
     *
     * GET /api/v1/empresas/buscar-por-nombre
     */
    @Operation(
            summary = "Buscar empresa por nombre",
            description = "Busca una empresa específica por su nombre exacto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<EmpresaDto> buscarPorNombre(
            @Parameter(description = "Nombre exacto de la empresa") @RequestParam String nombre) {
        log.info("GET /api/v1/empresas/buscar-por-nombre?nombre={}", nombre);

        EmpresaDto empresa = empresaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(empresa);
    }

    /**
     * HU-002: Buscar empresas por razón social
     *
     * GET /api/v1/empresas/buscar-por-razon-social
     */
    @Operation(
            summary = "Buscar empresas por razón social",
            description = "Busca empresas que contengan el texto especificado en su razón social"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-razon-social")
    public ResponseEntity<List<EmpresaDto>> buscarPorRazonSocial(
            @Parameter(description = "Texto a buscar en la razón social") @RequestParam String razonSocial) {
        log.info("GET /api/v1/empresas/buscar-por-razon-social?razonSocial={}", razonSocial);

        List<EmpresaDto> empresas = empresaService.buscarPorRazonSocial(razonSocial);
        return ResponseEntity.ok(empresas);
    }

    /**
     * Eliminar empresa (eliminación lógica)
     *
     * DELETE /api/v1/empresas/{id}
     */
    @Operation(
            summary = "Eliminar empresa",
            description = "Realiza eliminación lógica de una empresa (marca como eliminada)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(
            @Parameter(description = "ID único de la empresa") @PathVariable Long id) {
        log.info("DELETE /api/v1/empresas/{} - Eliminando empresa", id);

        empresaService.eliminarEmpresa(id);
        return ResponseEntity.noContent().build();
    }
}