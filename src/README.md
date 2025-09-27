# Sistema de Gestión de Restaurante - API REST con Spring Boot

Un sistema completo de gestión de restaurantes desarrollado en Java con **Spring Boot 3.2.0**, arquitectura REST API, base de datos H2 embebida y documentación OpenAPI. Sistema diseñado para manejar múltiples sucursales, gestión de inventario, pedidos, clientes y facturación con operaciones CRUD completas.

## 🚀 Características Principales

### 📡 **API REST Completa**
- **45+ Endpoints REST**: CRUD completo para todas las entidades
- **Documentación OpenAPI**: Swagger UI integrado con ejemplos interactivos
- **Validación de Datos**: Bean Validation con manejo de errores centralizado
- **Arquitectura de 3 Capas**: Controller → Service → Repository

### 🏢 **Gestión Empresarial Avanzada**
- **Multi-Empresa**: Soporte para múltiples empresas del grupo corporativo
- **Multi-Sucursal**: Gestión de múltiples ubicaciones con horarios independientes
- **Estructura Geográfica**: Sistema completo País → Provincia → Localidad → Domicilio

### 📦 **Gestión de Productos e Inventario**
- **Catálogo Dual**: Artículos manufacturados (con recetas) e insumos
- **Categorización Jerárquica**: Categorías y subcategorías ilimitadas
- **Control de Stock**: Gestión de stock actual y máximo por insumo
- **Unidades de Medida**: Sistema flexible de unidades

### 🛒 **Sistema de Pedidos y Facturación**
- **Procesamiento de Pedidos**: Estados (PENDIENTE, PREPARACION, LISTO, ENTREGADO)
- **Facturación Integrada**: Soporte para EFECTIVO y MERCADOPAGO
- **Estadísticas de Ventas**: Reportes y métricas de facturación
- **Tipos de Envío**: DELIVERY y TAKEAWAY

### 🎯 **Sistema de Promociones**
- **Promociones Temporales**: Con restricciones de fechas y horarios
- **Tipos de Promoción**: HAPPYHOUR, PROMOCION1 (extensible)
- **Aplicación por Productos**: Promociones vinculadas a artículos específicos
- **Validación Automática**: Control de vigencia en tiempo real

### 👥 **Gestión de Clientes**
- **Registro Completo**: Datos personales, contacto y múltiples domicilios
- **Historial de Pedidos**: Trazabilidad completa de compras
- **Búsquedas Avanzadas**: Por email, teléfono, nombre, apellido

## 📋 Requisitos Previos

- **Java**: JDK 17 o superior
- **Gradle**: 7.0 o superior (incluido wrapper)
- **IDE**: IntelliJ IDEA, Eclipse, o VS Code recomendados
- **RAM**: Mínimo 2GB para desarrollo

## 🛠️ Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone <url-del-repositorio>
cd ComercioJpa
```

### 2. Verificar Instalación de Java
```bash
java -version
# Debe mostrar Java 17 o superior
```

### 3. Construir el Proyecto
```bash
# En Windows
gradlew.bat build

# En Linux/Mac
./gradlew build
```

### 4. Ejecutar la Aplicación
```bash
# En Windows
gradlew.bat bootRun

# En Linux/Mac
./gradlew bootRun
```

## 🎯 Comandos de Desarrollo

### Comandos Principales

| Comando | Windows | Linux/Mac | Descripción |
|---------|---------|-----------|-------------|
| **Ejecutar App** | `gradlew.bat bootRun` | `./gradlew bootRun` | Inicia el servidor Spring Boot |
| **Construir** | `gradlew.bat build` | `./gradlew build` | Compila y construye todo el proyecto |
| **Limpiar** | `gradlew.bat clean` | `./gradlew clean` | Elimina archivos de construcción |
| **Compilar** | `gradlew.bat compileJava` | `./gradlew compileJava` | Solo compila el código Java |
| **Pruebas** | `gradlew.bat test` | `./gradlew test` | Ejecuta las pruebas unitarias |

### URLs de la Aplicación (cuando está ejecutándose)

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **API REST** | `http://localhost:8080/api/` | Base de todos los endpoints |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` | Documentación interactiva |
| **API Docs** | `http://localhost:8080/api-docs` | Especificación OpenAPI JSON |
| **H2 Console** | `http://localhost:8080/h2-console` | Consola de base de datos |
| **Health Check** | `http://localhost:8080/actuator/health` | Estado del sistema |

### Configuración de H2 Console
- **JDBC URL**: `jdbc:h2:file:./restaurante_db`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

## 🏗️ Arquitectura del Sistema

### Estructura del Proyecto
```
src/main/java/org/example/
├── controller/          # Controladores REST (10 controladores)
│   ├── EmpresaController.java
│   ├── ClienteController.java
│   ├── ArticuloController.java
│   ├── CategoriaController.java
│   ├── PromocionController.java
│   ├── SucursalController.java
│   ├── PedidoController.java
│   ├── FacturaController.java
│   ├── HealthController.java
│   └── GlobalExceptionHandler.java
├── service/             # Servicios de negocio (8 servicios)
│   ├── EmpresaService.java
│   ├── ClienteService.java
│   ├── ArticuloService.java
│   ├── CategoriaService.java
│   ├── PromocionService.java
│   ├── PedidoService.java
│   ├── FacturaService.java
│   └── DataInitializationService.java
├── repository/          # Repositorios Spring Data JPA (15 repositorios)
│   ├── EmpresaRepository.java
│   ├── ClienteRepository.java
│   ├── ArticuloRepository.java
│   ├── CategoriaRepository.java
│   ├── PromocionRepository.java
│   ├── PedidoRepository.java
│   ├── FacturaRepository.java
│   └── ... (geograficos y soporte)
├── entidades/           # Entidades JPA del dominio (15 entidades)
│   ├── Base.java        # Entidad base abstracta
│   ├── Empresa.java
│   ├── Sucursal.java
│   ├── Articulo.java    # Clase abstracta con herencia
│   ├── Cliente.java
│   ├── Pedido.java
│   ├── Factura.java
│   └── ... (entidades geograficas)
├── dto/                 # Data Transfer Objects
│   ├── EmpresaDto.java
│   ├── CreateEmpresaRequest.java
│   └── ... (DTOs para cada entidad)
└── Application.java     # Clase principal Spring Boot

src/main/resources/
├── application.yml      # Configuración Spring Boot
└── META-INF/
    └── persistence.xml  # Configuración JPA adicional
```

### Patrones de Diseño Implementados

1. **Controller-Service-Repository**: Arquitectura en 3 capas completa
2. **Builder Pattern**: Construcción fluida con Lombok `@SuperBuilder`
3. **Template Method**: Clase `Base` abstracta con método `getInfo()`
4. **DTO Pattern**: Separación entre entidades y API responses
5. **Dependency Injection**: Inyección automática con Spring
6. **Singleton**: Servicios y repositorios gestionados por Spring
7. **Factory**: EntityManager factory para JPA

## 📡 API REST Endpoints

### 🏢 Gestión de Empresas (`/api/v1/empresas`)
```http
GET    /api/v1/empresas              # Listar todas las empresas
GET    /api/v1/empresas/{id}         # Obtener empresa por ID
POST   /api/v1/empresas              # Crear nueva empresa
PUT    /api/v1/empresas/{id}         # Actualizar empresa
DELETE /api/v1/empresas/{id}         # Eliminar empresa (lógica)
```

### 👥 Gestión de Clientes (`/api/v1/clientes`)
```http
GET    /api/v1/clientes                           # Listar todos los clientes
GET    /api/v1/clientes/{id}                      # Obtener cliente por ID
POST   /api/v1/clientes                           # Crear nuevo cliente
PUT    /api/v1/clientes/{id}                      # Actualizar cliente
DELETE /api/v1/clientes/{id}                      # Eliminar cliente
GET    /api/v1/clientes/buscar-por-email          # Buscar por email
GET    /api/v1/clientes/buscar-por-telefono       # Buscar por teléfono
GET    /api/v1/clientes/buscar-por-nombre         # Buscar por nombre
GET    /api/v1/clientes/buscar-por-apellido       # Buscar por apellido
GET    /api/v1/clientes/{id}/con-pedidos          # Cliente con historial
```

### 📦 Gestión de Productos (`/api/v1/articulos`)
```http
GET    /api/v1/articulos                    # Listar todos los artículos
GET    /api/v1/articulos/{id}               # Obtener artículo por ID
POST   /api/v1/articulos                    # Crear nuevo artículo
PUT    /api/v1/articulos/{id}               # Actualizar artículo
DELETE /api/v1/articulos/{id}               # Eliminar artículo
GET    /api/v1/articulos/buscar-por-tipo    # Buscar por tipo (INSUMO/MANUFACTURADO)
```

### 🏷️ Gestión de Categorías (`/api/v1/categorias`)
```http
GET    /api/v1/categorias                      # Listar todas las categorías
GET    /api/v1/categorias/{id}                 # Obtener categoría por ID
POST   /api/v1/categorias                      # Crear nueva categoría
PUT    /api/v1/categorias/{id}                 # Actualizar categoría
DELETE /api/v1/categorias/{id}                 # Eliminar categoría
GET    /api/v1/categorias/principales          # Categorías principales (sin padre)
GET    /api/v1/categorias/buscar-por-nombre    # Buscar por nombre
GET    /api/v1/categorias/buscar-por-sucursal  # Buscar por sucursal
```

### 🎯 Gestión de Promociones (`/api/v1/promociones`)
```http
GET    /api/v1/promociones                      # Listar todas las promociones
GET    /api/v1/promociones/{id}                 # Obtener promoción por ID
POST   /api/v1/promociones                      # Crear nueva promoción
PUT    /api/v1/promociones/{id}                 # Actualizar promoción
DELETE /api/v1/promociones/{id}                 # Eliminar promoción
GET    /api/v1/promociones/vigentes             # Promociones activas ahora
GET    /api/v1/promociones/buscar-por-tipo      # Buscar por tipo
GET    /api/v1/promociones/buscar-por-sucursal  # Buscar por sucursal
```

### 🏪 Gestión de Sucursales (`/api/v1/sucursales`)
```http
GET    /api/v1/sucursales                      # Listar todas las sucursales
GET    /api/v1/sucursales/{id}                 # Obtener sucursal por ID
GET    /api/v1/sucursales/abiertas             # Sucursales abiertas ahora
GET    /api/v1/sucursales/buscar-por-nombre    # Buscar por nombre
GET    /api/v1/sucursales/buscar-por-empresa   # Buscar por empresa
```

### 🛒 Gestión de Pedidos (`/api/v1/pedidos`)
```http
GET    /api/v1/pedidos                       # Listar todos los pedidos
GET    /api/v1/pedidos/{id}                  # Obtener pedido por ID
GET    /api/v1/pedidos/buscar-por-cliente    # Buscar por cliente
GET    /api/v1/pedidos/buscar-por-estado     # Buscar por estado
GET    /api/v1/pedidos/buscar-por-fecha      # Buscar por fecha
GET    /api/v1/pedidos/buscar-por-sucursal   # Buscar por sucursal
```

### 💰 Gestión de Facturas (`/api/v1/facturas`)
```http
GET    /api/v1/facturas                           # Listar todas las facturas
GET    /api/v1/facturas/{id}                      # Obtener factura por ID
GET    /api/v1/facturas/buscar-por-fecha          # Buscar por fecha
GET    /api/v1/facturas/buscar-por-rango-fechas   # Buscar por rango
GET    /api/v1/facturas/buscar-por-forma-pago     # Buscar por forma de pago
GET    /api/v1/facturas/estadisticas              # Estadísticas de ventas
```

### 🔧 Endpoints del Sistema
```http
GET    /api/v1/health          # Estado de la aplicación
GET    /actuator/health        # Health check de Spring Boot
```

## 🔧 Tecnologías Utilizadas

### 🌱 Stack Principal
- **Spring Boot 3.2.0**: Framework principal con auto-configuración
- **Spring Web**: Para crear API REST
- **Spring Data JPA**: Abstracción de persistencia sobre Hibernate
- **Spring Boot Validation**: Validación de beans con anotaciones
- **Spring Boot Actuator**: Métricas y monitoring de la aplicación

### 🗄️ Persistencia y Base de Datos
- **Jakarta JPA 3.1.0**: Especificación de persistencia Java
- **Hibernate 6.4.4.Final**: Proveedor JPA con ORM completo
- **H2 Database 2.2.224**: Base de datos embebida con persistencia en archivo
- **HikariCP**: Connection pool de alto rendimiento (incluido en Spring Boot)

### 🛠️ Herramientas de Desarrollo
- **Java 17+**: Lenguaje principal con características modernas
- **Gradle**: Sistema de construcción con wrapper incluido
- **Lombok 1.18.32**: Reducción de código boilerplate
- **SLF4J**: Sistema de logging estándar

### 📚 Documentación y Testing
- **SpringDoc OpenAPI 2.2.0**: Generación automática de documentación API
- **Swagger UI**: Interfaz web para probar endpoints
- **Spring Boot Test**: Framework de testing (preparado para futuras pruebas)

## 📊 Modelo de Datos y Relaciones JPA

### Entidades Principales y Relaciones

#### 🏢 Estructura Empresarial
```java
Empresa (1) ←→ (N) Sucursal
    └── @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)

Sucursal (1) ←→ (N) Categoria
    └── @OneToMany with @JoinColumn(name = "sucursal_id")

Sucursal (1) ←→ (N) Promocion
    └── @OneToMany with @JoinColumn(name = "sucursal_id")
```

#### 📦 Gestión de Productos
```java
Categoria (1) ←→ (N) Articulo
    └── @OneToMany(mappedBy = "categoria")

Categoria (1) ←→ (N) Subcategorias
    └── @OneToMany(mappedBy = "categoriaPadre") // Auto-referencia

Articulo (herencia JOINED)
    ├── ArticuloInsumo (con stock y precios)
    └── ArticuloManufacturado (con tiempo preparación)
        └── (1) ←→ (N) ArticuloManufacturadoDetalle // Recetas
```

#### 👥 Gestión de Clientes y Pedidos
```java
Cliente (1) ←→ (1) Usuario
    └── @OneToOne with @JoinColumn(name = "usuario_id")

Cliente (1) ←→ (N) Domicilio
    └── @OneToMany with @JoinColumn(name = "cliente_id")

Cliente (1) ←→ (N) Pedido
    └── @OneToMany(mappedBy = "cliente")

Pedido (1) ←→ (N) DetallePedido
    └── @OneToMany with @JoinColumn(name = "pedido_id")
```

#### 🌍 Estructura Geográfica
```java
Pais (1) ←→ (N) Provincia (1) ←→ (N) Localidad (1) ←→ (N) Domicilio
    └── Jerarquía completa para direcciones precisas
```

### Características JPA Implementadas

#### 🔹 **Mapeo de Entidades**
- **Clase Base**: `@MappedSuperclass` con campos comunes (`id`, `nombre`, `eliminado`)
- **Herencia**: Estrategia `JOINED` para jerarquía `Articulo`
- **Generación de IDs**: `@GeneratedValue(strategy = GenerationType.IDENTITY)`
- **Soft Delete**: Flag `eliminado` para eliminación lógica

#### 🔹 **Relaciones Avanzadas**
- **Bidireccionales**: Con `mappedBy` para evitar tablas extras
- **Lazy Loading**: `FetchType.LAZY` por defecto para optimización
- **Cascade**: Configuración cuidadosa para evitar eliminaciones accidentales
- **Join Columns**: Claves foráneas explícitas donde es necesario

#### 🔹 **Consultas Personalizadas**
- **Query Methods**: Métodos de repositorio generados automáticamente
- **JPQL**: Consultas personalizadas con `@Query`
- **Enum Support**: Búsquedas por enums (Estado, FormaPago, TipoPromocion)
- **Soft Delete Queries**: Todos los métodos incluyen `AndEliminadoFalse`

## 🗄️ Configuración de Base de Datos

### application.yml
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./restaurante_db
    username: sa
    password:
    driver-class-name: org.h2.Driver

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

  h2:
    console:
      enabled: true
      path: /h2-console
```

### Características de la Base de Datos
- **Archivo Persistente**: `restaurante_db.mv.db` en el directorio raíz
- **Modo**: `create-drop` (recrea el esquema en cada inicio)
- **SQL Logging**: Queries visibles en consola con formato
- **Consola Web**: Acceso directo via browser para debugging
- **Transacciones**: Manejo automático por Spring

## 🚦 Inicialización Automática del Sistema

### DataInitializationService
El sistema incluye un servicio que ejecuta automáticamente al inicio y carga datos de demostración completos:

#### Datos Pre-cargados
1. **🌍 Estructura Geográfica**
   - País: Argentina
   - Provincia: Mendoza
   - Localidades: Maipú, Godoy Cruz
   - Múltiples domicilios de ejemplo

2. **🏢 Estructura Empresarial**
   - Empresa: "TechFood Solutions"
   - 2 Sucursales con horarios diferenciados
   - Domicilios específicos por sucursal

3. **📦 Catálogo de Productos**
   - **Insumos**: Cerveza, masa para pizza, queso mozzarella, etc.
   - **Manufacturados**: Pizza grande, pizza chica, combos
   - **Recetas**: Ingredientes y cantidades para productos manufacturados

4. **🏷️ Sistema de Categorización**
   - Categorías principales: Comidas, Bebidas, Postres
   - Subcategorías organizadas jerárquicamente

5. **🎯 Promociones Activas**
   - Happy Hour con restricciones horarias
   - Promociones especiales por tipo
   - Validación automática de vigencia

6. **👥 Clientes de Ejemplo**
   - Clientes con usuarios asociados
   - Múltiples domicilios por cliente
   - Datos de contacto completos

### Orden de Inicialización (Crítico)
```java
1. Pais → Provincia → Localidad → Domicilio    // Base geográfica
2. Usuario, Imagen, UnidadMedida               // Entidades de soporte
3. ArticuloInsumo, ArticuloManufacturado       // Catálogo de productos
4. Categoria                                   // Categorización
5. Promocion                                   // Ofertas y promociones
6. Empresa → Sucursal                          // Estructura empresarial
7. Cliente                                     // Clientes finales
```

## 🔍 Funcionalidades de los Servicios

### Patrones de Servicio Implementados

#### 🔹 **Estructura Consistente**
```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntityService {
    private final EntityRepository repository;

    // Métodos CRUD con validaciones de negocio
    // Conversión DTO privada en el servicio
    // Logging estructurado con contexto
}
```

#### 🔹 **Operaciones Disponibles por Servicio**
- **CRUD Completo**: Create, Read, Update, Delete (lógico)
- **Búsquedas Especializadas**: Por criterios específicos de cada entidad
- **Validaciones de Negocio**: Reglas específicas del dominio
- **Conversión DTO**: Mapeo automático de entidades a DTOs
- **Manejo de Transacciones**: Atomicidad garantizada

#### 🔹 **Servicios Especializados**
- **EmpresaService**: Gestión de empresas y relaciones con sucursales
- **ClienteService**: CRM completo con historial de pedidos
- **ArticuloService**: Catálogo con soporte para insumos y manufacturados
- **CategoriaService**: Jerarquías de categorías con validación de ciclos
- **PromocionService**: Promociones con validación temporal
- **PedidoService**: Workflow de pedidos con estados
- **FacturaService**: Facturación y estadísticas de ventas

## 🎯 Casos de Uso Principales

### 📊 **Gestión Empresarial**
1. **Registro de Empresas**: Crear nuevas empresas del grupo
2. **Gestión de Sucursales**: Administrar múltiples ubicaciones
3. **Control de Horarios**: Horarios independientes por sucursal
4. **Estructura Geográfica**: Direcciones precisas con jerarquía completa

### 🛒 **Operaciones Comerciales**
1. **Catálogo de Productos**: Gestión completa de inventario
2. **Procesamiento de Pedidos**: Workflow desde creación hasta entrega
3. **Facturación**: Procesamiento de pagos con múltiples formas
4. **Promociones**: Campañas de marketing con validación temporal

### 👥 **Gestión de Clientes**
1. **CRM Completo**: Registro y seguimiento de clientes
2. **Historial de Compras**: Trazabilidad completa de pedidos
3. **Gestión de Direcciones**: Múltiples domicilios por cliente
4. **Búsquedas Avanzadas**: Localización rápida de clientes

### 📈 **Análisis y Reportes**
1. **Estadísticas de Ventas**: Métricas de facturación por período
2. **Análisis de Promociones**: Efectividad por tipo de promoción
3. **Reportes de Productos**: Productos por categoría
4. **Seguimiento de Pedidos**: Estados y tiempos de procesamiento

## 🚀 Funcionalidades Implementadas

### ✅ **Completamente Implementado**
- [x] **API REST Completa**: 45+ endpoints con documentación OpenAPI
- [x] **Arquitectura 3 Capas**: Controller-Service-Repository consistente
- [x] **Persistencia JPA**: Hibernate con H2 y persistencia en archivo
- [x] **Validación Completa**: Bean Validation con manejo de errores
- [x] **Documentación Automática**: Swagger UI con ejemplos interactivos
- [x] **Inicialización Automática**: Datos de prueba pre-cargados
- [x] **Gestión de Transacciones**: Manejo automático con Spring
- [x] **Logging Estructurado**: SLF4J con contexto en cada operación
- [x] **Soft Delete**: Eliminación lógica en todas las entidades
- [x] **Enum Support**: Manejo robusto de tipos enumerados
- [x] **Relaciones Bidireccionales**: JPA optimizado con lazy loading
- [x] **Búsquedas Avanzadas**: Múltiples criterios por entidad
- [x] **Health Checks**: Monitoreo de estado de la aplicación
- [x] **Error Handling**: Manejo centralizado de excepciones

### ✅ **Entidades Funcionales (15 total)**
- [x] **Base**: Entidad abstracta con campos comunes
- [x] **Empresa**: Gestión de empresas del grupo
- [x] **Sucursal**: Ubicaciones con horarios independientes
- [x] **Articulo**: Jerarquía con insumos y manufacturados
- [x] **Categoria**: Organización jerárquica de productos
- [x] **Promocion**: Campañas con validación temporal
- [x] **Cliente**: CRM con datos completos
- [x] **Usuario**: Sistema de usuarios vinculado a clientes
- [x] **Pedido**: Gestión de órdenes con estados
- [x] **DetallePedido**: Líneas de pedido con cantidades
- [x] **Factura**: Facturación con múltiples formas de pago
- [x] **Domicilio**: Direcciones con estructura geográfica
- [x] **Pais/Provincia/Localidad**: Jerarquía geográfica completa
- [x] **Imagen**: Sistema de imágenes asociadas
- [x] **UnidadMedida**: Unidades flexibles para productos

## 📈 Próximas Mejoras Recomendadas

### 🔧 **Funcionalidades Adicionales**
- [ ] **Sistema de Autenticación**: JWT con Spring Security
- [ ] **Pruebas Unitarias**: JUnit 5 con TestContainers
- [ ] **Paginación**: Resultados paginados en endpoints de listado
- [ ] **Filtros Avanzados**: Criterios dinámicos de búsqueda
- [ ] **Cache**: Redis para optimización de consultas frecuentes
- [ ] **Auditoría**: Hibernate Envers para tracking de cambios
- [ ] **Notificaciones**: Sistema de eventos con Spring Events
- [ ] **Importación**: Carga masiva de datos desde Excel/CSV

### 🚀 **Escalabilidad y Rendimiento**
- [ ] **Base de Datos**: Migración a PostgreSQL/MySQL
- [ ] **Connection Pool**: Configuración avanzada de HikariCP
- [ ] **Índices**: Optimización de consultas con índices estratégicos
- [ ] **Profiling**: Métricas detalladas con Micrometer
- [ ] **Docker**: Containerización para deployment
- [ ] **Kubernetes**: Orchestration para múltiples instancias

### 📊 **Análisis y Reportes**
- [ ] **Dashboard**: Panel de control con métricas en tiempo real
- [ ] **Reportes PDF**: Generación automática de reportes
- [ ] **Exportación**: Excel/CSV para análisis externos
- [ ] **Gráficos**: Visualización de datos con Chart.js

## 🔧 Guía de Desarrollo

### Agregando Nueva Entidad

1. **Crear Entidad JPA**
```java
@Entity
@Table(name = "mi_entidad")
@SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true, exclude = {"relaciones"})
public class MiEntidad extends Base {
    // Campos específicos
}
```

2. **Crear Repository**
```java
@Repository
public interface MiEntidadRepository extends JpaRepository<MiEntidad, Long> {
    List<MiEntidad> findByEliminadoFalse();
    List<MiEntidad> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}
```

3. **Crear DTOs**
```java
@Data @Builder
public class MiEntidadDto {
    // Campos para API response
}

@Data @Builder
public class CreateMiEntidadRequest {
    @NotBlank private String nombre;
    // Campos para API request
}
```

4. **Crear Service**
```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MiEntidadService {
    private final MiEntidadRepository repository;

    // Métodos CRUD con lógica de negocio
    // Conversión DTO privada
}
```

5. **Crear Controller**
```java
@RestController
@RequestMapping("/api/v1/mi-entidad")
@RequiredArgsConstructor
@Tag(name = "Mi Entidad", description = "API para gestión")
public class MiEntidadController {
    private final MiEntidadService service;

    // Endpoints REST con OpenAPI docs
}
```

### Patrones de Código Recomendados

#### 🔹 **Anotaciones Lombok Estándar**
```java
// Para entidades
@SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true, exclude = {"collections"})

// Para servicios
@RequiredArgsConstructor
@Slf4j

// Para DTOs
@Data @Builder
```

#### 🔹 **Manejo de Enums en Endpoints**
```java
@GetMapping("/buscar-por-tipo")
public ResponseEntity<List<EntityDto>> buscarPorTipo(@RequestParam String tipo) {
    TipoEnum tipoEnum = TipoEnum.valueOf(tipo.toUpperCase());
    // Conversión segura con validación
}
```

#### 🔹 **Documentación OpenAPI**
```java
@Operation(summary = "Descripción corta", description = "Descripción detallada")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Operación exitosa"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
```

## 🤝 Contribución

### Proceso de Desarrollo
1. **Fork** del repositorio
2. **Crear rama** para nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. **Implementar** siguiendo los patrones establecidos
4. **Agregar pruebas** para la nueva funcionalidad
5. **Commit** de cambios (`git commit -m 'feat: agregar nueva funcionalidad'`)
6. **Push** a la rama (`git push origin feature/nueva-funcionalidad`)
7. **Crear Pull Request** con descripción detallada

### Convenciones de Código
- **Naming**: CamelCase para clases, camelCase para métodos
- **Packages**: Estructura modular clara (controller, service, repository, etc.)
- **Comments**: JavaDoc para APIs públicas, comentarios inline para lógica compleja
- **Commits**: Conventional Commits (feat:, fix:, docs:, refactor:)

## 📚 Documentación Adicional

### Archivos de Documentación
- **CLAUDE.md**: Guía completa para Claude Code con patrones técnicos
- **historiasUsuario.md**: 18 historias de usuario implementadas con trazabilidad al código
- **API Documentation**: Swagger UI disponible en `/swagger-ui.html`

### Enlaces de Referencia
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Reference](https://spring.io/projects/spring-data-jpa)
- [Hibernate User Guide](https://hibernate.org/orm/documentation/)
- [OpenAPI Specification](https://swagger.io/specification/)

## 📄 Información del Proyecto

### Métricas del Sistema
- **Líneas de Código**: ~15,000 líneas
- **Entidades JPA**: 15 entidades con relaciones complejas
- **Endpoints REST**: 45+ endpoints documentados
- **Servicios**: 8 servicios de negocio
- **Repositorios**: 15 repositorios Spring Data JPA
- **Controladores**: 10 controladores REST

### Licencia
Este proyecto está bajo la licencia [especificar licencia].

---

**Desarrollado por**: Los Cortez Team
**Versión**: 3.0.0 (Spring Boot REST API)
**Última actualización**: Septiembre 2025
**Stack**: Java 17, Spring Boot 3.2.0, Spring Data JPA, Hibernate 6.4, H2 Database, Lombok, OpenAPI
**Arquitectura**: Controller-Service-Repository con API REST completa