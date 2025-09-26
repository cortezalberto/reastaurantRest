package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entidades.*;
import org.example.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Servicio para inicializar datos de ejemplo en la base de datos
 *
 * Implementa CommandLineRunner para ejecutarse al iniciar la aplicación
 * y popular la base de datos con datos de ejemplo que cubren todas las
 * historias de usuario del sistema.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    private final ClienteRepository clienteRepository;
    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImagenRepository imagenRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final PaisRepository paisRepository;
    private final ProvinciaRepository provinciaRepository;
    private final LocalidadRepository localidadRepository;
    private final DomicilioRepository domicilioRepository;
    private final PromocionRepository promocionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("=== INICIALIZANDO DATOS DE EJEMPLO ===");

        if (empresaRepository.count() > 0) {
            log.info("Base de datos ya contiene datos. Saltando inicialización.");
            return;
        }

        try {
            inicializarDatosCompletos();
            log.info("=== DATOS DE EJEMPLO INICIALIZADOS EXITOSAMENTE ===");
        } catch (Exception e) {
            log.error("Error al inicializar datos de ejemplo", e);
            throw e;
        }
    }

    private void inicializarDatosCompletos() {
        log.info("Creando estructura geográfica...");
        var datosGeograficos = crearEstructuraGeografica();

        log.info("Creando configuración base...");
        var configuracionBase = crearConfiguracionBase();

        log.info("Creando productos y artículos...");
        var productos = crearProductosYArticulos(configuracionBase.unidadMedida, configuracionBase.imagenes);

        log.info("Creando categorías...");
        var categorias = crearCategorias(productos.articulosManufacturados);

        log.info("Creando promociones...");
        var promociones = crearPromociones(productos.articulosManufacturados, configuracionBase.imagenes);

        log.info("Creando empresa y sucursales...");
        var empresa = crearEmpresaYSucursales(datosGeograficos.domicilios, categorias, promociones);

        log.info("Creando clientes...");
        crearClientes(datosGeograficos, configuracionBase);

        log.info("Inicialización completada. Empresa '{}' creada con {} sucursales",
                empresa.getNombre(), empresa.getSucursales().size());
    }

    // ===== MÉTODOS DE CREACIÓN DE DATOS =====

    private DatosGeograficos crearEstructuraGeografica() {
        // Crear estructura: País -> Provincia -> Localidad -> Domicilio
        Pais argentina = Pais.builder()
                .nombre("Argentina")
                .build();
        argentina = paisRepository.save(argentina);

        Provincia mendoza = Provincia.builder()
                .nombre("Mendoza")
                .pais(argentina)
                .build();
        mendoza = provinciaRepository.save(mendoza);

        Localidad maipu = Localidad.builder()
                .nombre("Maipú")
                .provincia(mendoza)
                .build();
        maipu = localidadRepository.save(maipu);

        Localidad godoyCruz = Localidad.builder()
                .nombre("Godoy Cruz")
                .provincia(mendoza)
                .build();
        godoyCruz = localidadRepository.save(godoyCruz);

        Domicilio domicilio1 = Domicilio.builder()
                .nombre("San Martín")
                .localidad(maipu)
                .cp(5501)
                .numero(1000)
                .build();
        domicilio1 = domicilioRepository.save(domicilio1);

        Domicilio domicilio2 = Domicilio.builder()
                .nombre("San Juan")
                .localidad(godoyCruz)
                .cp(5502)
                .numero(500)
                .build();
        domicilio2 = domicilioRepository.save(domicilio2);

        return new DatosGeograficos(java.util.List.of(domicilio1, domicilio2));
    }

    private ConfiguracionBase crearConfiguracionBase() {
        // Usuarios
        Usuario usuario1 = Usuario.builder()
                .nombre("Usuario Principal")
                .auth0Id("001")
                .username("DavidLopez")
                .build();

        Usuario usuario2 = Usuario.builder()
                .nombre("Usuario Secundario")
                .auth0Id("002")
                .username("TomasFerro")
                .build();

        usuario1 = usuarioRepository.save(usuario1);
        usuario2 = usuarioRepository.save(usuario2);

        // Imágenes
        Imagen imagen1 = Imagen.builder()
                .nombre("Imagen Principal")
                .denominacion("imagen-producto-principal.jpg")
                .build();

        Imagen imagen2 = Imagen.builder()
                .nombre("Imagen Secundaria")
                .denominacion("imagen-producto-secundaria.jpg")
                .build();

        imagen1 = imagenRepository.save(imagen1);
        imagen2 = imagenRepository.save(imagen2);

        // Unidad de medida
        UnidadMedida unidadMedida = UnidadMedida.builder()
                .nombre("Kilogramo")
                .denominacion("Kg")
                .build();

        unidadMedida = unidadMedidaRepository.save(unidadMedida);

        return new ConfiguracionBase(
                java.util.List.of(usuario1, usuario2),
                java.util.List.of(imagen1, imagen2),
                unidadMedida
        );
    }

    private Productos crearProductosYArticulos(UnidadMedida unidadMedida, java.util.List<Imagen> imagenes) {
        // Artículos Insumo
        ArticuloInsumo cerveza = ArticuloInsumo.builder()
                .nombre("Cerveza Quilmes")
                .denominacion("Cerveza Quilmes 473ml")
                .precioVenta(150.0)
                .unidadMedida(unidadMedida)
                .precioCompra(80.0)
                .stockActual(50)
                .stockMaximo(200)
                .esParaElaborar(false)
                .build();

        ArticuloInsumo masa = ArticuloInsumo.builder()
                .nombre("Masa para Pizza")
                .denominacion("Masa fresca para pizza mediana")
                .precioVenta(200.0)
                .unidadMedida(unidadMedida)
                .precioCompra(120.0)
                .stockActual(30)
                .stockMaximo(100)
                .esParaElaborar(true)
                .build();

        cerveza.addImagen(imagenes.get(0));
        masa.addImagen(imagenes.get(1));

        cerveza = (ArticuloInsumo) articuloRepository.save(cerveza);
        masa = (ArticuloInsumo) articuloRepository.save(masa);

        // Artículos Manufacturados
        ArticuloManufacturado pizzaEspecial = ArticuloManufacturado.builder()
                .nombre("Pizza Especial")
                .denominacion("Pizza Especial con ingredientes premium")
                .descripcion("Pizza artesanal con masa fresca, mozzarella premium y ingredientes seleccionados")
                .tiempoEstimadoMinutos(25)
                .preparacion("Estirar masa, agregar salsa, queso e ingredientes. Hornear a 250°C por 12-15 min")
                .precioVenta(850.0)
                .unidadMedida(unidadMedida)
                .build();

        ArticuloManufacturado combo = ArticuloManufacturado.builder()
                .nombre("Combo Pizza + Bebida")
                .denominacion("Combo completo pizza mediana + bebida")
                .descripcion("Combinación perfecta para una comida completa")
                .tiempoEstimadoMinutos(30)
                .preparacion("Preparar pizza según receta estándar y servir con bebida fría")
                .precioVenta(950.0)
                .unidadMedida(unidadMedida)
                .build();

        pizzaEspecial.addImagen(imagenes.get(0));
        combo.addImagen(imagenes.get(1));

        pizzaEspecial = (ArticuloManufacturado) articuloRepository.save(pizzaEspecial);
        combo = (ArticuloManufacturado) articuloRepository.save(combo);

        // Crear detalles de manufacturados (recetas)
        ArticuloManufacturadoDetalle detalleMasa = ArticuloManufacturadoDetalle.builder()
                .nombre("Detalle Masa")
                .cantidad(1)
                .articuloInsumo(masa)
                .build();

        ArticuloManufacturadoDetalle detalleCerveza = ArticuloManufacturadoDetalle.builder()
                .nombre("Detalle Cerveza")
                .cantidad(2)
                .articuloInsumo(cerveza)
                .build();

        pizzaEspecial.addDetalle(detalleMasa);
        combo.addDetalle(detalleMasa);
        combo.addDetalle(detalleCerveza);

        articuloRepository.save(pizzaEspecial);
        articuloRepository.save(combo);

        return new Productos(
                java.util.List.of(cerveza, masa),
                java.util.List.of(pizzaEspecial, combo)
        );
    }

    private java.util.List<Categoria> crearCategorias(java.util.List<ArticuloManufacturado> articulos) {
        Categoria principales = Categoria.builder()
                .nombre("Platos Principales")
                .denominacion("Platos Principales")
                .build();

        Categoria pizzas = Categoria.builder()
                .nombre("Pizzas Especiales")
                .denominacion("Pizzas Gourmet y Especiales")
                .build();

        Categoria bebidas = Categoria.builder()
                .nombre("Bebidas")
                .denominacion("Bebidas Frías y Calientes")
                .build();

        principales = categoriaRepository.save(principales);
        pizzas = categoriaRepository.save(pizzas);
        bebidas = categoriaRepository.save(bebidas);

        principales.addSubcategoria(pizzas);
        principales.addSubcategoria(bebidas);

        articulos.forEach(principales::addArticulo);
        articulos.forEach(pizzas::addArticulo);

        categoriaRepository.save(principales);

        return java.util.List.of(principales, pizzas, bebidas);
    }

    private java.util.List<Promocion> crearPromociones(java.util.List<ArticuloManufacturado> articulos,
                                                       java.util.List<Imagen> imagenes) {
        Promocion promoOtono = Promocion.builder()
                .nombre("Promo Otoño 2024")
                .denominacion("Descuento especial de otoño")
                .fechaDesde(LocalDate.now().minusDays(10))
                .fechaHasta(LocalDate.now().plusDays(20))
                .horaDesde(LocalTime.of(18, 0))
                .horaHasta(LocalTime.of(23, 0))
                .precioDescuento(150.0)
                .precioPromocional(700.0)
                .tipoPromocion(TipoPromocion.PROMOCION1)
                .build();

        Promocion happyHour = Promocion.builder()
                .nombre("Happy Hour")
                .denominacion("2x1 en bebidas seleccionadas")
                .fechaDesde(LocalDate.now())
                .fechaHasta(LocalDate.now().plusDays(30))
                .horaDesde(LocalTime.of(17, 0))
                .horaHasta(LocalTime.of(20, 0))
                .precioDescuento(200.0)
                .precioPromocional(750.0)
                .tipoPromocion(TipoPromocion.HAPPYHOUR)
                .build();

        promoOtono.addImagen(imagenes.get(0));
        promoOtono.addArticulo(articulos.get(0));

        happyHour.addImagen(imagenes.get(1));
        happyHour.addArticulo(articulos.get(1));

        promoOtono = promocionRepository.save(promoOtono);
        happyHour = promocionRepository.save(happyHour);

        return java.util.List.of(promoOtono, happyHour);
    }

    private Empresa crearEmpresaYSucursales(java.util.List<Domicilio> domicilios,
                                           java.util.List<Categoria> categorias,
                                           java.util.List<Promocion> promociones) {
        // Crear empresa
        Empresa empresa = Empresa.builder()
                .nombre("TechFood Solutions")
                .razonSocial("TechFood Solutions S.A.")
                .cuil(2035620636)
                .build();

        empresa = empresaRepository.save(empresa);

        // Crear sucursales
        Sucursal matriz = Sucursal.builder()
                .nombre("Casa Matriz Centro")
                .horarioApertura(LocalTime.of(11, 0))
                .horarioCierre(LocalTime.of(23, 0))
                .domicilio(domicilios.get(0))
                .empresa(empresa)
                .build();

        Sucursal sucursal2 = Sucursal.builder()
                .nombre("Sucursal Godoy Cruz")
                .horarioApertura(LocalTime.of(10, 30))
                .horarioCierre(LocalTime.of(23, 30))
                .domicilio(domicilios.get(1))
                .empresa(empresa)
                .build();

        matriz = sucursalRepository.save(matriz);
        sucursal2 = sucursalRepository.save(sucursal2);

        // Asignar categorías y promociones
        categorias.forEach(matriz::addCategoria);
        promociones.forEach(matriz::addPromocion);

        categorias.forEach(sucursal2::addCategoria);
        promociones.forEach(sucursal2::addPromocion);

        empresa.addSucursal(matriz);
        empresa.addSucursal(sucursal2);

        return empresaRepository.save(empresa);
    }

    private void crearClientes(DatosGeograficos geograficos, ConfiguracionBase configuracion) {
        Cliente cliente1 = Cliente.builder()
                .nombre("David")
                .apellido("López")
                .telefono("2616649039")
                .email("david.lopez@email.com")
                .fechaNacimiento(LocalDate.of(1990, 5, 15))
                .imagen(configuracion.imagenes.get(0))
                .usuario(configuracion.usuarios.get(0))
                .build();

        Cliente cliente2 = Cliente.builder()
                .nombre("Tomás")
                .apellido("Ferro")
                .telefono("2616849039")
                .email("tomas.ferro@email.com")
                .fechaNacimiento(LocalDate.of(1988, 8, 20))
                .imagen(configuracion.imagenes.get(1))
                .usuario(configuracion.usuarios.get(1))
                .build();

        cliente1.addDomicilio(geograficos.domicilios.get(0));
        cliente2.addDomicilio(geograficos.domicilios.get(1));

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);
    }

    // ===== CLASES AUXILIARES =====

    private record DatosGeograficos(java.util.List<Domicilio> domicilios) {}

    private record ConfiguracionBase(
            java.util.List<Usuario> usuarios,
            java.util.List<Imagen> imagenes,
            UnidadMedida unidadMedida
    ) {}

    private record Productos(
            java.util.List<ArticuloInsumo> articulosInsumo,
            java.util.List<ArticuloManufacturado> articulosManufacturados
    ) {}
}