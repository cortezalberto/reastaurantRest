package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ArticuloDto;
import org.example.dto.CreateArticuloRequest;
import org.example.entidades.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de artículos
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final CategoriaRepository categoriaRepository;

    /**
     * Crear nuevo artículo
     */
    public ArticuloDto crearArticulo(CreateArticuloRequest request) {
        log.info("Creando nuevo artículo: {}", request.getNombre());

        UnidadMedida unidadMedida = unidadMedidaRepository.findById(request.getUnidadMedidaId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad de medida no encontrada"));

        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        }

        Articulo articulo;
        if ("INSUMO".equals(request.getTipo())) {
            articulo = ArticuloInsumo.builder()
                    .nombre(request.getNombre())
                    .denominacion(request.getDenominacion())
                    .precioVenta(request.getPrecioVenta())
                    .unidadMedida(unidadMedida)
                    .categoria(categoria)
                    .precioCompra(request.getPrecioCompra())
                    .stockActual(request.getStockActual())
                    .stockMaximo(request.getStockMaximo())
                    .esParaElaborar(request.getEsParaElaborar())
                    .build();
        } else {
            articulo = ArticuloManufacturado.builder()
                    .nombre(request.getNombre())
                    .denominacion(request.getDenominacion())
                    .precioVenta(request.getPrecioVenta())
                    .unidadMedida(unidadMedida)
                    .categoria(categoria)
                    .descripcion(request.getDescripcion())
                    .tiempoEstimadoMinutos(request.getTiempoEstimadoMinutos())
                    .preparacion(request.getPreparacion())
                    .build();
        }

        Articulo articuloGuardado = articuloRepository.save(articulo);
        log.info("Artículo creado exitosamente con ID: {}", articuloGuardado.getId());

        return convertirADto(articuloGuardado);
    }

    /**
     * Obtener artículo por ID
     */
    @Transactional(readOnly = true)
    public ArticuloDto obtenerArticuloPorId(Long id) {
        log.info("Consultando artículo con ID: {}", id);

        Articulo articulo = articuloRepository.findById(id)
                .filter(a -> !a.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado con ID: " + id));

        return convertirADto(articulo);
    }

    /**
     * Listar todos los artículos activos
     */
    @Transactional(readOnly = true)
    public List<ArticuloDto> listarArticulosActivos() {
        log.info("Consultando todos los artículos activos");

        List<Articulo> articulos = articuloRepository.findByEliminadoFalse();
        return articulos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar artículo
     */
    public ArticuloDto actualizarArticulo(Long id, CreateArticuloRequest request) {
        log.info("Actualizando artículo con ID: {}", id);

        Articulo articulo = articuloRepository.findById(id)
                .filter(a -> !a.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado con ID: " + id));

        UnidadMedida unidadMedida = unidadMedidaRepository.findById(request.getUnidadMedidaId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad de medida no encontrada"));

        Categoria categoria = null;
        if (request.getCategoriaId() != null) {
            categoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        }

        // Actualizar campos comunes
        articulo.setNombre(request.getNombre());
        articulo.setDenominacion(request.getDenominacion());
        articulo.setPrecioVenta(request.getPrecioVenta());
        articulo.setUnidadMedida(unidadMedida);
        articulo.setCategoria(categoria);

        // Actualizar campos específicos según el tipo
        if (articulo instanceof ArticuloInsumo insumo) {
            insumo.setPrecioCompra(request.getPrecioCompra());
            insumo.setStockActual(request.getStockActual());
            insumo.setStockMaximo(request.getStockMaximo());
            insumo.setEsParaElaborar(request.getEsParaElaborar());
        } else if (articulo instanceof ArticuloManufacturado manufacturado) {
            manufacturado.setDescripcion(request.getDescripcion());
            manufacturado.setTiempoEstimadoMinutos(request.getTiempoEstimadoMinutos());
            manufacturado.setPreparacion(request.getPreparacion());
        }

        Articulo articuloActualizado = articuloRepository.save(articulo);
        log.info("Artículo actualizado exitosamente: {}", articuloActualizado.getNombre());

        return convertirADto(articuloActualizado);
    }

    /**
     * Eliminar artículo (eliminación lógica)
     */
    public void eliminarArticulo(Long id) {
        log.info("Eliminando artículo con ID: {}", id);

        Articulo articulo = articuloRepository.findById(id)
                .filter(a -> !a.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Artículo no encontrado con ID: " + id));

        articulo.setEliminado(true);
        articuloRepository.save(articulo);
        log.info("Artículo eliminado exitosamente: {}", articulo.getNombre());
    }

    /**
     * Buscar artículos por nombre
     */
    @Transactional(readOnly = true)
    public List<ArticuloDto> buscarPorNombre(String nombre) {
        log.info("Buscando artículos por nombre: {}", nombre);

        List<Articulo> articulos = articuloRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
        return articulos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar artículos por tipo
     */
    @Transactional(readOnly = true)
    public List<ArticuloDto> buscarPorTipo(String tipo) {
        log.info("Buscando artículos por tipo: {}", tipo);

        List<Articulo> articulos = articuloRepository.findByEliminadoFalse();

        if ("INSUMO".equals(tipo)) {
            articulos = articulos.stream()
                    .filter(a -> a instanceof ArticuloInsumo)
                    .collect(Collectors.toList());
        } else if ("MANUFACTURADO".equals(tipo)) {
            articulos = articulos.stream()
                    .filter(a -> a instanceof ArticuloManufacturado)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Tipo de artículo inválido: " + tipo);
        }

        return articulos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private ArticuloDto convertirADto(Articulo articulo) {
        ArticuloDto.ArticuloDtoBuilder builder = ArticuloDto.builder()
                .id(articulo.getId())
                .nombre(articulo.getNombre())
                .denominacion(articulo.getDenominacion())
                .precioVenta(articulo.getPrecioVenta())
                .eliminado(articulo.isEliminado())
                .unidadMedida(ArticuloDto.UnidadMedidaDto.builder()
                        .id(articulo.getUnidadMedida().getId())
                        .nombre(articulo.getUnidadMedida().getNombre())
                        .denominacion(articulo.getUnidadMedida().getDenominacion())
                        .build());

        if (articulo instanceof ArticuloInsumo insumo) {
            builder.tipo("INSUMO")
                    .precioCompra(insumo.getPrecioCompra())
                    .stockActual(insumo.getStockActual())
                    .stockMaximo(insumo.getStockMaximo())
                    .esParaElaborar(insumo.getEsParaElaborar());
        } else if (articulo instanceof ArticuloManufacturado manufacturado) {
            builder.tipo("MANUFACTURADO")
                    .descripcion(manufacturado.getDescripcion())
                    .tiempoEstimadoMinutos(manufacturado.getTiempoEstimadoMinutos())
                    .preparacion(manufacturado.getPreparacion());

            List<ArticuloDto.ArticuloManufacturadoDetalleDto> detalles = manufacturado.getDetallesDeArticulo().stream()
                    .filter(d -> !d.isEliminado())
                    .map(detalle -> ArticuloDto.ArticuloManufacturadoDetalleDto.builder()
                            .id(detalle.getId())
                            .nombre(detalle.getNombre())
                            .cantidad(detalle.getCantidad())
                            .articuloInsumo(detalle.getArticuloInsumo().getNombre())
                            .build())
                    .collect(Collectors.toList());

            builder.detalles(detalles);
        }

        return builder.build();
    }
}