package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CategoriaDto;
import org.example.dto.CreateCategoriaRequest;
import org.example.entidades.Categoria;
import org.example.entidades.Sucursal;
import org.example.repository.CategoriaRepository;
import org.example.repository.SucursalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de categorías
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SucursalRepository sucursalRepository;

    /**
     * Crear nueva categoría
     */
    public CategoriaDto crearCategoria(CreateCategoriaRequest request) {
        log.info("Creando nueva categoría: {}", request.getNombre());

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        Categoria categoriaPadre = null;
        if (request.getCategoriaPadreId() != null) {
            categoriaPadre = categoriaRepository.findById(request.getCategoriaPadreId())
                    .filter(c -> !c.isEliminado())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría padre no encontrada"));
        }

        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .denominacion(request.getDenominacion())
                .sucursal(sucursal)
                .categoriaPadre(categoriaPadre)
                .build();

        if (categoriaPadre != null) {
            categoriaPadre.addSubcategoria(categoria);
        }

        sucursal.addCategoria(categoria);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        log.info("Categoría creada exitosamente con ID: {}", categoriaGuardada.getId());

        return convertirADto(categoriaGuardada);
    }

    /**
     * Obtener categoría por ID
     */
    @Transactional(readOnly = true)
    public CategoriaDto obtenerCategoriaPorId(Long id) {
        log.info("Consultando categoría con ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        return convertirADto(categoria);
    }

    /**
     * Listar todas las categorías activas
     */
    @Transactional(readOnly = true)
    public List<CategoriaDto> listarCategoriasActivas() {
        log.info("Consultando todas las categorías activas");

        List<Categoria> categorias = categoriaRepository.findByEliminadoFalse();
        return categorias.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar categoría
     */
    public CategoriaDto actualizarCategoria(Long id, CreateCategoriaRequest request) {
        log.info("Actualizando categoría con ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        Sucursal sucursal = sucursalRepository.findById(request.getSucursalId())
                .filter(s -> !s.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada"));

        Categoria categoriaPadre = null;
        if (request.getCategoriaPadreId() != null) {
            categoriaPadre = categoriaRepository.findById(request.getCategoriaPadreId())
                    .filter(c -> !c.isEliminado())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría padre no encontrada"));

            // Validar que no sea una referencia circular
            if (esReferenceiaCircular(categoria, categoriaPadre)) {
                throw new IllegalArgumentException("No se puede establecer una referencia circular entre categorías");
            }
        }

        categoria.setNombre(request.getNombre());
        categoria.setDenominacion(request.getDenominacion());
        categoria.setSucursal(sucursal);
        categoria.setCategoriaPadre(categoriaPadre);

        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        log.info("Categoría actualizada exitosamente: {}", categoriaActualizada.getNombre());

        return convertirADto(categoriaActualizada);
    }

    /**
     * Eliminar categoría (eliminación lógica)
     */
    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoría con ID: {}", id);

        Categoria categoria = categoriaRepository.findById(id)
                .filter(c -> !c.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        categoria.setEliminado(true);
        categoriaRepository.save(categoria);
        log.info("Categoría eliminada exitosamente: {}", categoria.getNombre());
    }

    /**
     * Buscar categorías por nombre
     */
    @Transactional(readOnly = true)
    public List<CategoriaDto> buscarPorNombre(String nombre) {
        log.info("Buscando categorías por nombre: {}", nombre);

        List<Categoria> categorias = categoriaRepository.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
        return categorias.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar categorías por sucursal
     */
    @Transactional(readOnly = true)
    public List<CategoriaDto> buscarPorSucursal(Long sucursalId) {
        log.info("Buscando categorías por sucursal ID: {}", sucursalId);

        List<Categoria> categorias = categoriaRepository.findBySucursalIdAndEliminadoFalse(sucursalId);
        return categorias.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener categorías principales (sin padre)
     */
    @Transactional(readOnly = true)
    public List<CategoriaDto> obtenerCategoriasPrincipales() {
        log.info("Obteniendo categorías principales");

        List<Categoria> categorias = categoriaRepository.findByCategoriaPadreIsNullAndEliminadoFalse();
        return categorias.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Validar referencia circular
     */
    private boolean esReferenceiaCircular(Categoria categoria, Categoria posiblePadre) {
        if (posiblePadre == null) {
            return false;
        }

        if (categoria.getId().equals(posiblePadre.getId())) {
            return true;
        }

        return esReferenceiaCircular(categoria, posiblePadre.getCategoriaPadre());
    }

    /**
     * Convertir entidad a DTO
     */
    private CategoriaDto convertirADto(Categoria categoria) {
        return CategoriaDto.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .denominacion(categoria.getDenominacion())
                .eliminado(categoria.isEliminado())
                .categoriaPadre(categoria.getCategoriaPadre() != null ?
                    CategoriaDto.builder()
                        .id(categoria.getCategoriaPadre().getId())
                        .nombre(categoria.getCategoriaPadre().getNombre())
                        .denominacion(categoria.getCategoriaPadre().getDenominacion())
                        .build() : null)
                .sucursal(categoria.getSucursal() != null ? categoria.getSucursal().getNombre() : null)
                .cantidadArticulos(categoria.getArticulos().size())
                .cantidadSubcategorias((int) categoria.getSubcategorias().stream()
                        .filter(sub -> !sub.isEliminado())
                        .count())
                .build();
    }
}