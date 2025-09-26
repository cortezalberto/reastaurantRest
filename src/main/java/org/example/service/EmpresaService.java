package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CreateEmpresaRequest;
import org.example.dto.EmpresaDto;
import org.example.entidades.Empresa;
import org.example.entidades.Sucursal;
import org.example.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de empresas
 *
 * Implementa la lógica de negocio para las historias de usuario:
 * - HU-001: Crear Nueva Empresa
 * - HU-002: Consultar Información de Empresa
 * - HU-003: Actualizar Datos de Empresa
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    /**
     * HU-001: Crear Nueva Empresa
     */
    public EmpresaDto crearEmpresa(CreateEmpresaRequest request) {
        log.info("Creando nueva empresa: {}", request.getNombre());

        // Validar que no existe empresa con el mismo CUIL
        if (empresaRepository.existsByCuilAndNotId(request.getCuil(), null)) {
            throw new IllegalArgumentException("Ya existe una empresa con el CUIL: " + request.getCuil());
        }

        Empresa empresa = Empresa.builder()
                .nombre(request.getNombre())
                .razonSocial(request.getRazonSocial())
                .cuil(request.getCuil())
                .build();

        Empresa empresaGuardada = empresaRepository.save(empresa);
        log.info("Empresa creada exitosamente con ID: {}", empresaGuardada.getId());

        return convertirADto(empresaGuardada);
    }

    /**
     * HU-002: Consultar Información de Empresa
     */
    @Transactional(readOnly = true)
    public EmpresaDto obtenerEmpresaPorId(Long id) {
        log.info("Consultando empresa con ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
                .filter(e -> !e.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con ID: " + id));

        return convertirADto(empresa);
    }

    /**
     * HU-002: Listar todas las empresas activas
     */
    @Transactional(readOnly = true)
    public List<EmpresaDto> listarEmpresasActivas() {
        log.info("Consultando todas las empresas activas");

        List<Empresa> empresas = empresaRepository.findAllActiveWithSucursales();
        return empresas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * HU-003: Actualizar Datos de Empresa
     */
    public EmpresaDto actualizarEmpresa(Long id, CreateEmpresaRequest request) {
        log.info("Actualizando empresa con ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
                .filter(e -> !e.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con ID: " + id));

        // Validar que no existe otra empresa con el mismo CUIL
        if (empresaRepository.existsByCuilAndNotId(request.getCuil(), id)) {
            throw new IllegalArgumentException("Ya existe otra empresa con el CUIL: " + request.getCuil());
        }

        empresa.setNombre(request.getNombre());
        empresa.setRazonSocial(request.getRazonSocial());
        empresa.setCuil(request.getCuil());

        Empresa empresaActualizada = empresaRepository.save(empresa);
        log.info("Empresa actualizada exitosamente: {}", empresaActualizada.getNombre());

        return convertirADto(empresaActualizada);
    }

    /**
     * HU-002: Buscar empresa por nombre
     */
    @Transactional(readOnly = true)
    public EmpresaDto buscarPorNombre(String nombre) {
        log.info("Buscando empresa por nombre: {}", nombre);

        Empresa empresa = empresaRepository.findByNombreAndEliminadoFalse(nombre)
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con nombre: " + nombre));

        return convertirADto(empresa);
    }

    /**
     * HU-002: Buscar empresas por razón social (contiene texto)
     */
    @Transactional(readOnly = true)
    public List<EmpresaDto> buscarPorRazonSocial(String razonSocial) {
        log.info("Buscando empresas por razón social: {}", razonSocial);

        List<Empresa> empresas = empresaRepository.findByRazonSocialContainingIgnoreCaseAndEliminadoFalse(razonSocial);
        return empresas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar empresa (eliminación lógica)
     */
    public void eliminarEmpresa(Long id) {
        log.info("Eliminando empresa con ID: {}", id);

        Empresa empresa = empresaRepository.findById(id)
                .filter(e -> !e.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada con ID: " + id));

        empresa.setEliminado(true);
        empresaRepository.save(empresa);
        log.info("Empresa eliminada exitosamente: {}", empresa.getNombre());
    }

    /**
     * Convertir entidad a DTO
     */
    private EmpresaDto convertirADto(Empresa empresa) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<EmpresaDto.SucursalDto> sucursalesDto = empresa.getSucursales().stream()
                .filter(s -> !s.isEliminado())
                .map(sucursal -> EmpresaDto.SucursalDto.builder()
                        .id(sucursal.getId())
                        .nombre(sucursal.getNombre())
                        .horarioApertura(sucursal.getHorarioApertura() != null ?
                                sucursal.getHorarioApertura().format(timeFormatter) : null)
                        .horarioCierre(sucursal.getHorarioCierre() != null ?
                                sucursal.getHorarioCierre().format(timeFormatter) : null)
                        .direccion(sucursal.getDomicilio() != null ? sucursal.getDomicilio().getInfo() : null)
                        .cantidadCategorias(sucursal.getCategorias().size())
                        .cantidadPromociones(sucursal.getPromociones().size())
                        .build())
                .collect(Collectors.toList());

        return EmpresaDto.builder()
                .id(empresa.getId())
                .nombre(empresa.getNombre())
                .razonSocial(empresa.getRazonSocial())
                .cuil(empresa.getCuil())
                .eliminado(empresa.isEliminado())
                .sucursales(sucursalesDto)
                .cantidadSucursales(sucursalesDto.size())
                .build();
    }
}