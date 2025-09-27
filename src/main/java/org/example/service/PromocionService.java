package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PromocionDto;
import org.example.dto.CreatePromocionRequest;
import org.example.entidades.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de promociones
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PromocionService {

    private final PromocionRepository promocionRepository;
    private final SucursalRepository sucursalRepository;
    private final ArticuloRepository articuloRepository;

    /**
     * Crear nueva promoción
     */
    public PromocionDto crearPromocion(CreatePromocionRequest request) {
        log.info("Creando nueva promoción: {}", request.getNombre());

        // Validar fechas
        if (request.getFechaHasta().isBefore(request.getFechaDesde())) {
            throw new IllegalArgumentException("La fecha hasta debe ser posterior a la fecha desde");
        }

        // Validar horarios
        if (request.getHoraHasta().isBefore(request.getHoraDesde())) {
            throw new IllegalArgumentException("La hora hasta debe ser posterior a la hora desde");
        }

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        Promocion promocion = Promocion.builder()
                .nombre(request.getNombre())
                .denominacion(request.getDenominacion())
                .fechaDesde(request.getFechaDesde())
                .fechaHasta(request.getFechaHasta())
                .horaDesde(request.getHoraDesde())
                .horaHasta(request.getHoraHasta())
                .precioDescuento(request.getPrecioDescuento())
                .precioPromocional(request.getPrecioPromocional())
                .tipoPromocion(TipoPromocion.valueOf(request.getTipoPromocion()))
                .sucursal(sucursal)
                .build();

        // Agregar artículos si se especificaron
        if (request.getArticuloIds() != null && !request.getArticuloIds().isEmpty()) {
            List<Articulo> articulos = articuloRepository.findAllById(request.getArticuloIds());
            for (Articulo articulo : articulos) {
                if (!articulo.isEliminado()) {
                    promocion.addArticulo(articulo);
                }
            }
        }

        sucursal.addPromocion(promocion);

        Promocion promocionGuardada = promocionRepository.save(promocion);
        log.info("Promoción creada exitosamente con ID: {}", promocionGuardada.getId());

        return convertirADto(promocionGuardada);
    }

    /**
     * Obtener promoción por ID
     */
    @Transactional(readOnly = true)
    public PromocionDto obtenerPromocionPorId(Long id) {
        log.info("Consultando promoción con ID: {}", id);

        Promocion promocion = promocionRepository.findById(id)
                .filter(p -> !p.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada con ID: " + id));

        return convertirADto(promocion);
    }

    /**
     * Listar todas las promociones activas
     */
    @Transactional(readOnly = true)
    public List<PromocionDto> listarPromocionesActivas() {
        log.info("Consultando todas las promociones activas");

        List<Promocion> promociones = promocionRepository.findByEliminadoFalse();
        return promociones.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener promociones vigentes
     */
    @Transactional(readOnly = true)
    public List<PromocionDto> obtenerPromocionesVigentes() {
        log.info("Obteniendo promociones vigentes");

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        List<Promocion> promociones = promocionRepository.findPromocionesVigentes(hoy);
        return promociones.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar promoción
     */
    public PromocionDto actualizarPromocion(Long id, CreatePromocionRequest request) {
        log.info("Actualizando promoción con ID: {}", id);

        Promocion promocion = promocionRepository.findById(id)
                .filter(p -> !p.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada con ID: " + id));

        // Validar fechas
        if (request.getFechaHasta().isBefore(request.getFechaDesde())) {
            throw new IllegalArgumentException("La fecha hasta debe ser posterior a la fecha desde");
        }

        // Validar horarios
        if (request.getHoraHasta().isBefore(request.getHoraDesde())) {
            throw new IllegalArgumentException("La hora hasta debe ser posterior a la hora desde");
        }

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        promocion.setNombre(request.getNombre());
        promocion.setDenominacion(request.getDenominacion());
        promocion.setFechaDesde(request.getFechaDesde());
        promocion.setFechaHasta(request.getFechaHasta());
        promocion.setHoraDesde(request.getHoraDesde());
        promocion.setHoraHasta(request.getHoraHasta());
        promocion.setPrecioDescuento(request.getPrecioDescuento());
        promocion.setPrecioPromocional(request.getPrecioPromocional());
        promocion.setTipoPromocion(TipoPromocion.valueOf(request.getTipoPromocion()));
        promocion.setSucursal(sucursal);

        // Actualizar artículos
        promocion.getArticulos().clear();
        if (request.getArticuloIds() != null && !request.getArticuloIds().isEmpty()) {
            List<Articulo> articulos = articuloRepository.findAllById(request.getArticuloIds());
            for (Articulo articulo : articulos) {
                if (!articulo.isEliminado()) {
                    promocion.addArticulo(articulo);
                }
            }
        }

        Promocion promocionActualizada = promocionRepository.save(promocion);
        log.info("Promoción actualizada exitosamente: {}", promocionActualizada.getNombre());

        return convertirADto(promocionActualizada);
    }

    /**
     * Eliminar promoción (eliminación lógica)
     */
    public void eliminarPromocion(Long id) {
        log.info("Eliminando promoción con ID: {}", id);

        Promocion promocion = promocionRepository.findById(id)
                .filter(p -> !p.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Promoción no encontrada con ID: " + id));

        promocion.setEliminado(true);
        promocionRepository.save(promocion);
        log.info("Promoción eliminada exitosamente: {}", promocion.getNombre());
    }

    /**
     * Buscar promociones por nombre
     */
    @Transactional(readOnly = true)
    public List<PromocionDto> buscarPorNombre(String nombre) {
        log.info("Buscando promociones por nombre: {}", nombre);

        List<Promocion> promociones = promocionRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
        return promociones.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar promociones por tipo
     */
    @Transactional(readOnly = true)
    public List<PromocionDto> buscarPorTipo(String tipo) {
        log.info("Buscando promociones por tipo: {}", tipo);

        TipoPromocion tipoPromocion = TipoPromocion.valueOf(tipo);
        List<Promocion> promociones = promocionRepository.findByTipoPromocionAndEliminadoFalse(tipoPromocion);
        return promociones.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar promociones por sucursal
     */
    @Transactional(readOnly = true)
    public List<PromocionDto> buscarPorSucursal(Long sucursalId) {
        log.info("Buscando promociones por sucursal ID: {}", sucursalId);

        List<Promocion> promociones = promocionRepository.findBySucursalIdAndEliminadoFalse(sucursalId);
        return promociones.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Verificar si una promoción está activa
     */
    private boolean estaActiva(Promocion promocion) {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return !promocion.isEliminado() &&
               !hoy.isBefore(promocion.getFechaDesde()) &&
               !hoy.isAfter(promocion.getFechaHasta()) &&
               !ahora.isBefore(promocion.getHoraDesde()) &&
               !ahora.isAfter(promocion.getHoraHasta());
    }

    /**
     * Convertir entidad a DTO
     */
    private PromocionDto convertirADto(Promocion promocion) {
        return PromocionDto.builder()
                .id(promocion.getId())
                .nombre(promocion.getNombre())
                .denominacion(promocion.getDenominacion())
                .fechaDesde(promocion.getFechaDesde())
                .fechaHasta(promocion.getFechaHasta())
                .horaDesde(promocion.getHoraDesde())
                .horaHasta(promocion.getHoraHasta())
                .precioDescuento(promocion.getPrecioDescuento())
                .precioPromocional(promocion.getPrecioPromocional())
                .tipoPromocion(promocion.getTipoPromocion().name())
                .eliminado(promocion.isEliminado())
                .sucursal(promocion.getSucursal() != null ? promocion.getSucursal().getNombre() : null)
                .cantidadArticulos(promocion.getArticulos().size())
                .activa(estaActiva(promocion))
                .build();
    }
}