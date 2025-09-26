package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.repository.EmpresaRepository;
import org.example.repository.ClienteRepository;
import org.example.repository.ArticuloRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controlador de salud del sistema
 *
 * Proporciona endpoints para verificar el estado del sistema
 * y obtener estadísticas básicas de la base de datos
 */
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Tag(name = "Salud del Sistema", description = "Endpoints para verificar el estado del sistema")
public class HealthController {

    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final ArticuloRepository articuloRepository;

    /**
     * Endpoint básico de salud
     */
    @Operation(
            summary = "Verificar salud del sistema",
            description = "Endpoint básico para verificar que la API está funcionando"
    )
    @ApiResponse(responseCode = "200", description = "Sistema funcionando correctamente")
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now(),
                "message", "Sistema de Gestión de Restaurante funcionando correctamente"
        ));
    }

    /**
     * Estadísticas del sistema
     */
    @Operation(
            summary = "Obtener estadísticas del sistema",
            description = "Proporciona conteos básicos de las entidades principales del sistema"
    )
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        long totalEmpresas = empresaRepository.count();
        long totalClientes = clienteRepository.count();
        long totalArticulos = articuloRepository.count();

        return ResponseEntity.ok(Map.of(
                "timestamp", LocalDateTime.now(),
                "database", Map.of(
                        "empresas", totalEmpresas,
                        "clientes", totalClientes,
                        "articulos", totalArticulos
                ),
                "status", "Sistema operativo con " + totalEmpresas + " empresas registradas"
        ));
    }
}