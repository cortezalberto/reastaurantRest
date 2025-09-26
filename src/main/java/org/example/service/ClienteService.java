package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ClienteDto;
import org.example.dto.CreateClienteRequest;
import org.example.entidades.Cliente;
import org.example.entidades.Domicilio;
import org.example.entidades.Imagen;
import org.example.entidades.Usuario;
import org.example.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de clientes
 *
 * Implementa la lógica de negocio para las historias de usuario:
 * - HU-021: Registrar Nuevo Cliente
 * - HU-023: Consultar Información de Cliente
 * - HU-024: Actualizar Datos de Cliente
 * - HU-025: Buscar Clientes por Criterios
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * HU-021: Registrar Nuevo Cliente
     */
    public ClienteDto crearCliente(CreateClienteRequest request) {
        log.info("Creando nuevo cliente: {} {}", request.getNombre(), request.getApellido());

        // Validar que no existe cliente con el mismo email
        if (clienteRepository.existsByEmailAndNotId(request.getEmail(), null)) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + request.getEmail());
        }

        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .fechaNacimiento(request.getFechaNacimiento())
                .build();

        // Asignar usuario e imagen si se proporcionaron
        if (request.getUsuarioId() != null) {
            Usuario usuario = Usuario.builder().id(request.getUsuarioId()).build();
            cliente.setUsuario(usuario);
        }

        if (request.getImagenId() != null) {
            Imagen imagen = Imagen.builder().id(request.getImagenId()).build();
            cliente.setImagen(imagen);
        }

        Cliente clienteGuardado = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());

        return convertirADto(clienteGuardado);
    }

    /**
     * HU-023: Consultar Información de Cliente
     */
    @Transactional(readOnly = true)
    public ClienteDto obtenerClientePorId(Long id) {
        log.info("Consultando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        return convertirADto(cliente);
    }

    /**
     * HU-023: Listar todos los clientes activos
     */
    @Transactional(readOnly = true)
    public List<ClienteDto> listarClientesActivos() {
        log.info("Consultando todos los clientes activos");

        List<Cliente> clientes = clienteRepository.findAllActiveWithCompleteInfo();
        return clientes.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * HU-024: Actualizar Datos de Cliente
     */
    public ClienteDto actualizarCliente(Long id, CreateClienteRequest request) {
        log.info("Actualizando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Validar que no existe otro cliente con el mismo email
        if (clienteRepository.existsByEmailAndNotId(request.getEmail(), id)) {
            throw new IllegalArgumentException("Ya existe otro cliente con el email: " + request.getEmail());
        }

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setEmail(request.getEmail());
        cliente.setFechaNacimiento(request.getFechaNacimiento());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado exitosamente: {} {}", clienteActualizado.getNombre(), clienteActualizado.getApellido());

        return convertirADto(clienteActualizado);
    }

    /**
     * HU-025: Buscar cliente por email
     */
    @Transactional(readOnly = true)
    public ClienteDto buscarPorEmail(String email) {
        log.info("Buscando cliente por email: {}", email);

        Cliente cliente = clienteRepository.findByEmailAndEliminadoFalse(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con email: " + email));

        return convertirADto(cliente);
    }

    /**
     * HU-025: Buscar clientes por nombre
     */
    @Transactional(readOnly = true)
    public List<ClienteDto> buscarPorNombre(String nombre) {
        log.info("Buscando clientes por nombre: {}", nombre);

        List<Cliente> clientes = clienteRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
        return clientes.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * HU-025: Buscar clientes por apellido
     */
    @Transactional(readOnly = true)
    public List<ClienteDto> buscarPorApellido(String apellido) {
        log.info("Buscando clientes por apellido: {}", apellido);

        List<Cliente> clientes = clienteRepository.findByApellidoContainingIgnoreCaseAndEliminadoFalse(apellido);
        return clientes.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * HU-025: Buscar cliente por teléfono
     */
    @Transactional(readOnly = true)
    public ClienteDto buscarPorTelefono(String telefono) {
        log.info("Buscando cliente por teléfono: {}", telefono);

        Cliente cliente = clienteRepository.findByTelefonoAndEliminadoFalse(telefono)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con teléfono: " + telefono));

        return convertirADto(cliente);
    }

    /**
     * HU-023: Obtener cliente con pedidos
     */
    @Transactional(readOnly = true)
    public ClienteDto obtenerClienteConPedidos(Long id) {
        log.info("Consultando cliente con pedidos para ID: {}", id);

        Cliente cliente = clienteRepository.findByIdWithPedidos(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        return convertirADto(cliente);
    }

    /**
     * Eliminar cliente (eliminación lógica)
     */
    public void eliminarCliente(Long id) {
        log.info("Eliminando cliente con ID: {}", id);

        Cliente cliente = clienteRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        cliente.setEliminado(true);
        clienteRepository.save(cliente);
        log.info("Cliente eliminado exitosamente: {} {}", cliente.getNombre(), cliente.getApellido());
    }

    /**
     * Convertir entidad a DTO
     */
    private ClienteDto convertirADto(Cliente cliente) {
        ClienteDto.UsuarioDto usuarioDto = null;
        if (cliente.getUsuario() != null) {
            usuarioDto = ClienteDto.UsuarioDto.builder()
                    .id(cliente.getUsuario().getId())
                    .nombre(cliente.getUsuario().getNombre())
                    .username(cliente.getUsuario().getUsername())
                    .auth0Id(cliente.getUsuario().getAuth0Id())
                    .build();
        }

        List<ClienteDto.DomicilioDto> domiciliosDto = cliente.getDomicilios().stream()
                .map(domicilio -> ClienteDto.DomicilioDto.builder()
                        .id(domicilio.getId())
                        .nombre(domicilio.getNombre())
                        .numero(domicilio.getNumero())
                        .cp(domicilio.getCp())
                        .localidad(domicilio.getLocalidad() != null ? domicilio.getLocalidad().getNombre() : null)
                        .provincia(domicilio.getLocalidad() != null && domicilio.getLocalidad().getProvincia() != null ?
                                domicilio.getLocalidad().getProvincia().getNombre() : null)
                        .pais(domicilio.getLocalidad() != null && domicilio.getLocalidad().getProvincia() != null &&
                                domicilio.getLocalidad().getProvincia().getPais() != null ?
                                domicilio.getLocalidad().getProvincia().getPais().getNombre() : null)
                        .build())
                .collect(Collectors.toList());

        return ClienteDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .telefono(cliente.getTelefono())
                .email(cliente.getEmail())
                .fechaNacimiento(cliente.getFechaNacimiento())
                .eliminado(cliente.isEliminado())
                .usuario(usuarioDto)
                .domicilios(domiciliosDto)
                .cantidadPedidos(cliente.getPedidos().size())
                .build();
    }
}