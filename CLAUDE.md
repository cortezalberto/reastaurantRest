# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

This is a Spring Boot 3.2.0 project using Gradle with H2 database and comprehensive REST API architecture.

### Essential Commands
- **Run the application**: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)
- **Build the project**: `./gradlew build`
- **Clean and rebuild**: `./gradlew clean build`
- **Run tests**: `./gradlew test`
- **Run single test**: `./gradlew test --tests ClassName.methodName`
- **Stop running application**: `Ctrl+C` in terminal or kill gradle process

### Application URLs (when running)
- **API Base**: `http://localhost:8080/api/v1/`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./restaurante_db`, User: `sa`, Password: empty)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`
- **Health Check**: `http://localhost:8080/actuator/health`

### Dependencies
- **Spring Boot 3.2.0**: Web, Data JPA, Validation, Actuator
- **H2 Database 2.2.224**: Embedded database with file persistence
- **Lombok 1.18.32**: Boilerplate code reduction
- **OpenAPI 2.2.0**: API documentation (springdoc-openapi-starter-webmvc-ui)
- **Java 17**: Required version
- **Gradle 8.14**: Build tool (wrapper included)
- **JUnit Platform**: Testing framework (Spring Boot Test starter)

### Database
- **File**: `restaurante_db.mv.db` (auto-created in project root)
- **Mode**: `create-drop` (recreates schema on each startup)
- **Console Access**: Available at `/h2-console` endpoint
- **SQL Logging**: Enabled with formatted output

## Project Architecture

This is a complete Spring Boot REST API for restaurant management following Controller-Service-Repository pattern with 9 controllers, 8 services, 15 repositories, and 25 entity files (20 entities + 5 enums).

### Core Architecture Layers

#### 1. Controller Layer (`src/main/java/org/example/controller/`)
REST endpoints with OpenAPI documentation:
- **Business Management**: EmpresaController, SucursalController
- **Customer Management**: ClienteController
- **Product Management**: ArticuloController, CategoriaController, PromocionController
- **Order Management**: PedidoController, FacturaController
- **System**: HealthController
- **Error Handling**: GlobalExceptionHandler with centralized exception handling

#### 2. Service Layer (`src/main/java/org/example/service/`)
Business logic with declarative transactions:
- **Business Services**: EmpresaService, ClienteService, ArticuloService, CategoriaService, PromocionService
- **Order Services**: PedidoService, FacturaService
- **System Services**: DataInitializationService (automated data seeding on startup)
- **Transaction Management**: All services use `@Transactional` for consistency
- **DTO Conversion**: Service layer handles entity-to-DTO conversion

#### 3. Repository Layer (`src/main/java/org/example/repository/`)
Spring Data JPA with 15 repository interfaces:
- **Custom Queries**: JPQL queries for complex operations and enum-based searches
- **Soft Delete Support**: All repositories include `AndEliminadoFalse` methods
- **Enum Support**: Proper handling of Estado, FormaPago, TipoPromocion enums in query methods
- **Geographic**: PaisRepository, ProvinciaRepository, LocalidadRepository, DomicilioRepository
- **Business**: EmpresaRepository, SucursalRepository, ClienteRepository, PedidoRepository, FacturaRepository
- **Products**: ArticuloRepository, CategoriaRepository, PromocionRepository
- **Support**: UsuarioRepository, ImagenRepository, UnidadMedidaRepository

#### 4. Entity Layer (`src/main/java/org/example/entidades/`)
25 JPA entity files with inheritance and relationships (20 entities + 5 enums):
- **Base Entity**: Abstract class with common fields (`id`, `nombre`, `eliminado`)
- **JPA Annotations**: Standard Jakarta JPA with Hibernate
- **Lombok Integration**: `@SuperBuilder`, `@Getter`, `@Setter`, `@ToString` with circular reference exclusions
- **Inheritance Strategy**: JOINED table inheritance for Articulo hierarchy (ArticuloInsumo, ArticuloManufacturado)
- **Soft Delete**: Uses `eliminado` boolean flag instead of physical deletion

#### 5. DTO Layer (`src/main/java/org/example/dto/`)
Data Transfer Objects for API isolation (13 total classes):
- **Response DTOs**: Clean API responses (EmpresaDto, ClienteDto, ArticuloDto, etc.)
- **Request DTOs**: Input validation (CreateEmpresaRequest, CreateClienteRequest, etc.)
- **Bean Validation**: Jakarta validation annotations for input validation

### Application Enums (`src/main/java/org/example/entidades/`)
- **Estado**: PREPARACION, PENDIENTE, CANCELADO, RECHAZADO, ENTREGADO
- **FormaPago**: EFECTIVO, MERCADOPAGO
- **TipoPromocion**: HAPPYHOUR, PROMOCION1
- **Rol**: User role definitions
- **TipoDeEnvio**: Delivery type options

### Key Domain Model

#### Geographic Hierarchy
```
Pais -> Provincia -> Localidad -> Domicilio
```

#### Business Structure
```
Empresa -> Sucursal -> Categoria/Promocion
                  -> Articulo (Insumo/Manufacturado)
```

#### User Management and Orders
```
Usuario -> Cliente -> Domicilio (many-to-many)
        -> Pedido -> DetallePedido -> Factura
```

### Critical Spring Boot Patterns

#### Entity Relationships
- **Cascade Handling**: Careful cascade configuration to avoid EntityExistsException
- **Bidirectional Mapping**: Proper @OneToMany/@ManyToOne with mappedBy
- **Lazy Loading**: FetchType.LAZY for collections, specific queries for eager loading
- **Join Columns**: Proper @JoinColumn configuration for foreign keys

#### Repository Conventions
All repositories follow consistent patterns:
```java
List<Entity> findByEliminadoFalse(); // Active records only
List<Entity> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre); // Search
List<Entity> findBySucursalIdAndEliminadoFalse(Long sucursalId); // Relationship queries
List<Entity> findByEstadoAndEliminadoFalse(Estado estado); // Enum-based queries
List<Entity> findByFormaPagoAndEliminadoFalse(FormaPago formaPago); // Enum support
List<Entity> findByTipoPromocionAndEliminadoFalse(TipoPromocion tipo); // Type filtering
```

#### Service Layer Patterns
```java
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EntityService {
    private final EntityRepository repository;

    private EntityDto convertirADto(Entity entity) {
        // DTO conversion in service layer
    }
}
```

#### Controller Patterns
```java
@RestController
@RequestMapping("/api/v1/entities")
@RequiredArgsConstructor
@Tag(name = "Entity Management")
public class EntityController {
    private final EntityService service;
    // REST endpoints with OpenAPI docs
}
```

## Data Initialization

### Automatic Seeding
The `DataInitializationService` runs on startup and creates comprehensive test data:

1. **Geographic Structure**: Argentina -> Mendoza -> Maipú/Godoy Cruz -> Addresses
2. **Base Configuration**: Users, Images, Measurement Units
3. **Product Catalog**: Ingredients (cerveza, masa) and Manufactured items (pizza, combo)
4. **Categories**: Hierarchical category structure
5. **Promotions**: Time-based promotional campaigns
6. **Company Structure**: TechFood Solutions with 2 branches
7. **Customer Data**: Sample customers with addresses
8. **Order Data**: 5 diverse orders with different states (ENTREGADO, PREPARACION, PENDIENTE, CANCELADO)
9. **Invoice Data**: Automatic invoice generation for delivered orders with MercadoPago simulation

### Entity Creation Order
Critical persistence order to avoid transient instance errors:
```
Pais -> Provincia -> Localidad -> Domicilio (geographic)
Usuario, Imagen, UnidadMedida (support entities)
ArticuloInsumo, ArticuloManufacturado (products)
Categoria (categories)
Promocion (promotions)
Empresa -> Sucursal (company structure)
Cliente (customers with existing entities)
Pedido (orders with complete relationships)
Factura (invoices for delivered orders)
```

## Critical Implementation Details

### Enum Handling in Services
Services properly convert String parameters to enums:
```java
// FacturaService
public List<FacturaDto> buscarPorFormaPago(String formaPago) {
    FormaPago formaPagoEnum = FormaPago.valueOf(formaPago.toUpperCase());
    List<Factura> facturas = facturaRepository.findByFormaPagoAndEliminadoFalse(formaPagoEnum);
    return facturas.stream().map(this::convertirADto).collect(Collectors.toList());
}

// PedidoService
public List<PedidoDto> buscarPorEstado(String estado) {
    Estado estadoEnum = Estado.valueOf(estado.toUpperCase());
    List<Pedido> pedidos = pedidoRepository.findByEstadoAndEliminadoFalse(estadoEnum);
    return pedidos.stream().map(this::convertirADto).collect(Collectors.toList());
}
```

### Entity Field Access Patterns
Entities use proper field names for collections:
```java
// Pedido entity uses 'detallePedidos' field
pedido.getDetallePedidos().stream()...

// ArticuloManufacturado uses 'detallesDeArticulo' field
manufacturado.getDetallesDeArticulo().stream()...
```

### Type Casting Requirements
When working with numeric types, ensure proper casting:
```java
// MercadoPago payment ID requires Integer, but generated as long
factura.setMpPaymentId((int) basePaymentId);
```

## Configuration

### Application Configuration (`application.yml`)
- **Application Name**: `comercio-jpa-api`
- **Database**: H2 file-based with console enabled at `/h2-console`
  - **H2 Settings**: `DB_CLOSE_ON_EXIT=FALSE`, `AUTO_RECONNECT=TRUE`
- **JPA**: DDL auto-creation (`create-drop`), SQL logging enabled at DEBUG level
  - **SQL Logging**: Includes parameter binding with TRACE level
- **Server**: Port 8080, detailed error responses included
- **Actuator**: Health, info, env endpoints specifically exposed
- **OpenAPI**: Swagger UI enabled at `/swagger-ui.html`, package scanning configured
- **Logging**: SQL queries and parameters visible, custom console pattern
- **CORS**: Configured for frontend integration at `http://localhost:3000`

## Performance Optimizations

### Database Indexing
Strategic indexes added for high-performance queries:
```java
@Table(name = "pedidos", indexes = {
    @Index(name = "idx_pedido_fecha", columnList = "fecha_pedido"),
    @Index(name = "idx_pedido_estado", columnList = "estado"),
    @Index(name = "idx_pedido_cliente", columnList = "cliente_id")
})
```

### Data Type Corrections
Critical fixes for data integrity:
- **CUIL Field**: Changed from Integer to Long to support 11-digit Argentine CUIL numbers
- **MercadoPago Payment ID**: Requires casting from long to int in DataInitializationService
- **Validation Enhancements**: Jakarta Bean Validation annotations throughout entity hierarchy

## Important Development Notes

### When Adding New Enum-Based Endpoints
1. Create enum parameter in controller method signature as String
2. Convert String to enum using `EnumType.valueOf(parameter.toUpperCase())`
3. Pass enum to repository method, not String
4. Add proper error handling for invalid enum values
5. Document enum values in OpenAPI annotations

### When Working with Entity Collections
- Use correct field names: `detallePedidos`, `detallesDeArticulo`
- Always check for proper getter method names in entity classes
- Exclude circular references in @ToString annotations with `exclude = {"collections"}`
- Use proper cascade and fetch strategies for performance

### Repository Pattern Best Practices
- All query methods should include `AndEliminadoFalse` for soft delete support
- Use enum parameters in repository method signatures, not String
- Add proper JPQL @Query annotations for complex queries
- Follow consistent naming conventions for search methods

### Development Workflow Notes

#### When Making Schema Changes
- Schema recreates on each startup (create-drop mode)
- Monitor DataInitializationService execution logs carefully
- Check SQL output for proper DDL generation
- Verify entity relationship mappings are correct

#### Critical Entity Patterns
Entities must follow exact field naming conventions:
```java
// Pedido entity uses 'detallePedidos' field name
pedido.getDetallePedidos().stream()...

// ArticuloManufacturado uses 'detallesDeArticulo' field name
manufacturado.getDetallesDeArticulo().stream()...
```

#### Repository Enum Handling
All enum-based queries follow consistent pattern:
```java
// Service layer converts String to Enum
Estado estadoEnum = Estado.valueOf(estado.toUpperCase());
List<Pedido> pedidos = pedidoRepository.findByEstadoAndEliminadoFalse(estadoEnum);
```

### Common Development Tasks

#### Adding New Entity
1. Create entity class extending `Base` in `entidades/` package
2. Add Lombok annotations (`@SuperBuilder`, `@Getter`, `@Setter`)
3. Create repository interface in `repository/` package
4. Add to `DataInitializationService` if seeding needed
5. Create service class for business logic
6. Add controller for REST endpoints
7. Create DTOs for API responses

#### Debugging Common Issues

**Port Conflicts:**
```bash
# Linux/Mac: Kill process using port 8080
lsof -ti:8080 | xargs kill -9

# Windows: Find and kill process
netstat -ano | findstr :8080
taskkill /PID <pid> /F
```

**Database Issues:**
```bash
# Reset H2 database completely (Linux/Mac)
rm restaurante_db.mv.db restaurante_db.trace.db
./gradlew bootRun

# Reset H2 database completely (Windows)
del restaurante_db.mv.db restaurante_db.trace.db
gradlew.bat bootRun
```

**Lombok Compilation Issues:**
```bash
# Clean build to resolve annotation processing
./gradlew clean build
# Ensure IDE has Lombok plugin installed
```

## CORS Configuration

### Frontend Integration
- **Configured Origin**: `http://localhost:3000` (React frontend)
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Headers**: All headers allowed for development
- **Credentials**: Enabled for session-based authentication
- **Configuration File**: `src/main/java/org/example/config/CorsConfig.java`

## Testing Infrastructure

### Current State
- **Test Directory**: `src/test/` exists but no test files implemented
- **Testing Dependencies**: JUnit Platform included in build.gradle
- **Next Steps**: Add integration tests for controllers and service layer

## Build and Testing Status

- **✅ Verified Compilation**: Successfully tested with `./gradlew build`
- **✅ Runtime Verified**: Application startup tested with `./gradlew bootRun`
- **✅ Data Integrity**: All seed data creates without persistence errors
- **✅ Type Safety**: No casting or conversion errors with updated data types
- **✅ MercadoPago Integration**: Payment ID casting fix implemented and verified
- **✅ CORS Configuration**: Frontend integration enabled for localhost:3000

## Security Considerations

### Current Security Model (Development)
- **No Authentication**: Open API for development
- **H2 Console**: Publicly accessible (development only)
- **Error Details**: Full error information exposed

### Production Readiness
- Add Spring Security for authentication/authorization
- Disable H2 console and detailed error messages
- Configure proper database with connection pooling
- Add input sanitization and rate limiting