package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.SucursalDto;
import org.example.entidades.Sucursal;
import org.example.repository.SucursalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de sucursales
 *
 * Implementa las siguientes historias de usuario:
 * - HU-004: Consultar Sucursales por Empresa
 * - HU-005: Consultar Horarios de Sucursal
 */
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Sucursales", description = "API para gestión independiente de sucursales del sistema")
public class SucursalController {

    private final SucursalRepository sucursalRepository;

    /**
     * Obtener sucursal por ID
     */
    @Operation(
            summary = "Obtener sucursal por ID",
            description = "Consulta los datos completos de una sucursal específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDto> obtenerSucursal(
            @Parameter(description = "ID único de la sucursal") @PathVariable Long id) {
        log.info("GET /api/v1/sucursales/{} - Consultando sucursal", id);

        Sucursal sucursal = sucursalRepository.findById(id)
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada con ID: " + id));

        return ResponseEntity.ok(convertirADto(sucursal));
    }

    /**
     * Listar todas las sucursales activas
     */
    @Operation(
            summary = "Listar todas las sucursales",
            description = "Obtiene el listado completo de sucursales activas del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<SucursalDto>> listarSucursales() {
        log.info("GET /api/v1/sucursales - Listando todas las sucursales");

        List<Sucursal> sucursales = sucursalRepository.findByEliminadoFalse();
        List<SucursalDto> sucursalesDto = sucursales.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sucursalesDto);
    }

    /**
     * Buscar sucursales por nombre
     */
    @Operation(
            summary = "Buscar sucursales por nombre",
            description = "Busca sucursales que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<SucursalDto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String nombre) {
        log.info("GET /api/v1/sucursales/buscar-por-nombre?nombre={}", nombre);

        List<Sucursal> sucursales = sucursalRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
        List<SucursalDto> sucursalesDto = sucursales.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sucursalesDto);
    }

    /**
     * Buscar sucursales por empresa
     */
    @Operation(
            summary = "Buscar sucursales por empresa",
            description = "Obtiene todas las sucursales de una empresa específica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-empresa")
    public ResponseEntity<List<SucursalDto>> buscarPorEmpresa(
            @Parameter(description = "ID de la empresa") @RequestParam Long empresaId) {
        log.info("GET /api/v1/sucursales/buscar-por-empresa?empresaId={}", empresaId);

        List<Sucursal> sucursales = sucursalRepository.findByEmpresaIdAndEliminadoFalse(empresaId);
        List<SucursalDto> sucursalesDto = sucursales.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sucursalesDto);
    }

    /**
     * Obtener sucursales abiertas
     */
    @Operation(
            summary = "Obtener sucursales abiertas",
            description = "Obtiene las sucursales que están abiertas en este momento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursales abiertas obtenidas exitosamente")
    })
    @GetMapping("/abiertas")
    public ResponseEntity<List<SucursalDto>> obtenerSucursalesAbiertas() {
        log.info("GET /api/v1/sucursales/abiertas - Obteniendo sucursales abiertas");

        LocalTime ahora = LocalTime.now();
        List<Sucursal> sucursales = sucursalRepository.findByEliminadoFalse().stream()
                .filter(s -> estaAbierta(s, ahora))
                .collect(Collectors.toList());

        List<SucursalDto> sucursalesDto = sucursales.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sucursalesDto);
    }

    /**
     * Verificar si una sucursal está abierta
     */
    private boolean estaAbierta(Sucursal sucursal, LocalTime ahora) {
        if (sucursal.getHorarioApertura() == null || sucursal.getHorarioCierre() == null) {
            return false;
        }

        LocalTime apertura = sucursal.getHorarioApertura();
        LocalTime cierre = sucursal.getHorarioCierre();

        // Manejar el caso donde el horario cruza medianoche
        if (cierre.isBefore(apertura)) {
            return !ahora.isBefore(apertura) || !ahora.isAfter(cierre);
        } else {
            return !ahora.isBefore(apertura) && !ahora.isAfter(cierre);
        }
    }

    /**
     * Convertir entidad a DTO
     */
    private SucursalDto convertirADto(Sucursal sucursal) {
        LocalTime ahora = LocalTime.now();

        return SucursalDto.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .horarioApertura(sucursal.getHorarioApertura())
                .horarioCierre(sucursal.getHorarioCierre())
                .eliminado(sucursal.isEliminado())
                .empresa(sucursal.getEmpresa() != null ? sucursal.getEmpresa().getNombre() : null)
                .domicilio(sucursal.getDomicilio() != null ?
                    SucursalDto.DomicilioDto.builder()
                        .id(sucursal.getDomicilio().getId())
                        .nombre(sucursal.getDomicilio().getNombre())
                        .numero(sucursal.getDomicilio().getNumero())
                        .cp(sucursal.getDomicilio().getCp())
                        .localidad(sucursal.getDomicilio().getLocalidad() != null ?
                            sucursal.getDomicilio().getLocalidad().getNombre() : null)
                        .provincia(sucursal.getDomicilio().getLocalidad() != null &&
                            sucursal.getDomicilio().getLocalidad().getProvincia() != null ?
                            sucursal.getDomicilio().getLocalidad().getProvincia().getNombre() : null)
                        .pais(sucursal.getDomicilio().getLocalidad() != null &&
                            sucursal.getDomicilio().getLocalidad().getProvincia() != null &&
                            sucursal.getDomicilio().getLocalidad().getProvincia().getPais() != null ?
                            sucursal.getDomicilio().getLocalidad().getProvincia().getPais().getNombre() : null)
                        .build() : null)
                .cantidadCategorias(sucursal.getCategorias() != null ?
                    (int) sucursal.getCategorias().stream().filter(c -> !c.isEliminado()).count() : 0)
                .cantidadPromociones(sucursal.getPromociones() != null ?
                    (int) sucursal.getPromociones().stream().filter(p -> !p.isEliminado()).count() : 0)
                .abierta(estaAbierta(sucursal, ahora))
                .build();
    }
}