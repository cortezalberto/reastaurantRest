package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ClienteDto;
import org.example.dto.CreateClienteRequest;
import org.example.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de clientes
 *
 * Expone endpoints REST para las historias de usuario:
 * - HU-021: Registrar Nuevo Cliente
 * - HU-023: Consultar Información de Cliente
 * - HU-024: Actualizar Datos de Cliente
 * - HU-025: Buscar Clientes por Criterios
 */
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Clientes", description = "API para gestión de clientes del restaurante")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * HU-021: Registrar Nuevo Cliente
     *
     * POST /api/v1/clientes
     */
    @Operation(
            summary = "Registrar nuevo cliente",
            description = "Crea un nuevo cliente con datos personales, teléfono, email y fecha de nacimiento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe cliente con el mismo email")
    })
    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(
            @Valid @RequestBody CreateClienteRequest request) {
        log.info("POST /api/v1/clientes - Registrando cliente: {} {}", request.getNombre(), request.getApellido());

        ClienteDto clienteCreado = clienteService.crearCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
    }

    /**
     * HU-023: Consultar Información de Cliente por ID
     *
     * GET /api/v1/clientes/{id}
     */
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Consulta los datos completos de un cliente específico incluyendo domicilios y usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerCliente(
            @Parameter(description = "ID único del cliente") @PathVariable Long id) {
        log.info("GET /api/v1/clientes/{} - Consultando cliente", id);

        ClienteDto cliente = clienteService.obtenerClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * HU-023: Listar todos los clientes activos
     *
     * GET /api/v1/clientes
     */
    @Operation(
            summary = "Listar todos los clientes",
            description = "Obtiene el listado completo de clientes activos del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listarClientes() {
        log.info("GET /api/v1/clientes - Listando todos los clientes");

        List<ClienteDto> clientes = clienteService.listarClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * HU-024: Actualizar Datos de Cliente
     *
     * PUT /api/v1/clientes/{id}
     */
    @Operation(
            summary = "Actualizar cliente",
            description = "Modifica los datos de un cliente existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "409", description = "Email ya existe en otro cliente")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(
            @Parameter(description = "ID único del cliente") @PathVariable Long id,
            @Valid @RequestBody CreateClienteRequest request) {
        log.info("PUT /api/v1/clientes/{} - Actualizando cliente", id);

        ClienteDto clienteActualizado = clienteService.actualizarCliente(id, request);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * HU-025: Buscar cliente por email
     *
     * GET /api/v1/clientes/buscar-por-email
     */
    @Operation(
            summary = "Buscar cliente por email",
            description = "Busca un cliente específico por su dirección de correo electrónico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/buscar-por-email")
    public ResponseEntity<ClienteDto> buscarPorEmail(
            @Parameter(description = "Email del cliente") @RequestParam String email) {
        log.info("GET /api/v1/clientes/buscar-por-email?email={}", email);

        ClienteDto cliente = clienteService.buscarPorEmail(email);
        return ResponseEntity.ok(cliente);
    }

    /**
     * HU-025: Buscar clientes por nombre
     *
     * GET /api/v1/clientes/buscar-por-nombre
     */
    @Operation(
            summary = "Buscar clientes por nombre",
            description = "Busca clientes que contengan el texto especificado en su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-nombre")
    public ResponseEntity<List<ClienteDto>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre") @RequestParam String nombre) {
        log.info("GET /api/v1/clientes/buscar-por-nombre?nombre={}", nombre);

        List<ClienteDto> clientes = clienteService.buscarPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }

    /**
     * HU-025: Buscar clientes por apellido
     *
     * GET /api/v1/clientes/buscar-por-apellido
     */
    @Operation(
            summary = "Buscar clientes por apellido",
            description = "Busca clientes que contengan el texto especificado en su apellido"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente")
    })
    @GetMapping("/buscar-por-apellido")
    public ResponseEntity<List<ClienteDto>> buscarPorApellido(
            @Parameter(description = "Texto a buscar en el apellido") @RequestParam String apellido) {
        log.info("GET /api/v1/clientes/buscar-por-apellido?apellido={}", apellido);

        List<ClienteDto> clientes = clienteService.buscarPorApellido(apellido);
        return ResponseEntity.ok(clientes);
    }

    /**
     * HU-025: Buscar cliente por teléfono
     *
     * GET /api/v1/clientes/buscar-por-telefono
     */
    @Operation(
            summary = "Buscar cliente por teléfono",
            description = "Busca un cliente específico por su número de teléfono"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/buscar-por-telefono")
    public ResponseEntity<ClienteDto> buscarPorTelefono(
            @Parameter(description = "Número de teléfono del cliente") @RequestParam String telefono) {
        log.info("GET /api/v1/clientes/buscar-por-telefono?telefono={}", telefono);

        ClienteDto cliente = clienteService.buscarPorTelefono(telefono);
        return ResponseEntity.ok(cliente);
    }

    /**
     * HU-023: Obtener cliente con historial de pedidos
     *
     * GET /api/v1/clientes/{id}/con-pedidos
     */
    @Operation(
            summary = "Obtener cliente con pedidos",
            description = "Consulta un cliente incluyendo su historial completo de pedidos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente con pedidos encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{id}/con-pedidos")
    public ResponseEntity<ClienteDto> obtenerClienteConPedidos(
            @Parameter(description = "ID único del cliente") @PathVariable Long id) {
        log.info("GET /api/v1/clientes/{}/con-pedidos - Consultando cliente con pedidos", id);

        ClienteDto cliente = clienteService.obtenerClienteConPedidos(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Eliminar cliente (eliminación lógica)
     *
     * DELETE /api/v1/clientes/{id}
     */
    @Operation(
            summary = "Eliminar cliente",
            description = "Realiza eliminación lógica de un cliente (marca como eliminado)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID único del cliente") @PathVariable Long id) {
        log.info("DELETE /api/v1/clientes/{} - Eliminando cliente", id);

        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}