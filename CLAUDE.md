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
- **JUnit Platform**: Testing framework (Spring Boot Test starter)

### Database
- **File**: `restaurante_db.mv.db` (auto-created in project root)
- **Mode**: `create-drop` (recreates schema on each startup)
- **Console Access**: Available at `/h2-console` endpoint
- **SQL Logging**: Enabled with formatted output

## Project Architecture

This is a complete Spring Boot REST API for restaurant management following Controller-Service-Repository pattern.

### Core Architecture Layers

#### 1. Controller Layer (`src/main/java/org/example/controller/`)
- **EmpresaController**: Company management endpoints
- **ClienteController**: Customer management endpoints
- **ArticuloController**: Product and ingredient management
- **CategoriaController**: Product category management
- **PromocionController**: Promotion and offers management
- **SucursalController**: Branch management
- **PedidoController**: Order management
- **FacturaController**: Invoice management
- **HealthController**: System health monitoring
- **GlobalExceptionHandler**: Centralized error handling
- **OpenAPI Documentation**: Comprehensive API docs with examples

#### 2. Service Layer (`src/main/java/org/example/service/`)
- **EmpresaService**: Business logic for company operations
- **ClienteService**: Business logic for customer operations
- **ArticuloService**: Business logic for product operations
- **CategoriaService**: Business logic for category operations
- **PromocionService**: Business logic for promotion operations
- **PedidoService**: Business logic for order management
- **FacturaService**: Business logic for invoice management
- **DataInitializationService**: Automated data seeding on startup
- **Transaction Management**: Declarative transactions with Spring
- **Validation Logic**: Business rule enforcement

#### 3. Repository Layer (`src/main/java/org/example/repository/`)
- **Spring Data JPA**: 15 repository interfaces (including newly added PedidoRepository and FacturaRepository)
- **Custom Queries**: JPQL queries for complex operations and enum-based searches
- **Enum Support**: Proper handling of Estado, FormaPago, TipoPromocion enums in query methods
- **Geographic Entities**: PaisRepository, ProvinciaRepository, LocalidadRepository, DomicilioRepository
- **Business Entities**: EmpresaRepository, SucursalRepository, ClienteRepository, PedidoRepository, FacturaRepository
- **Product Entities**: ArticuloRepository, CategoriaRepository, PromocionRepository
- **Support Entities**: UsuarioRepository, ImagenRepository, UnidadMedidaRepository

#### 4. Entity Layer (`src/main/java/org/example/entidades/`)
- **Base Entity**: Abstract class with common fields (`id`, `nombre`, `eliminado`)
- **JPA Annotations**: Standard Jakarta JPA with Hibernate
- **Lombok Integration**: Builders, getters, setters, toString
- **Inheritance Strategy**: JOINED table inheritance for Articulo hierarchy

#### 5. DTO Layer (`src/main/java/org/example/dto/`)
- **Response DTOs**: Clean API responses (EmpresaDto, ClienteDto, ArticuloDto, etc.)
- **Request DTOs**: Input validation (CreateEmpresaRequest, CreateClienteRequest, etc.)
- **API Isolation**: Separate data transfer objects from entities

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

#### User Management
```
Usuario -> Cliente -> Domicilio (many-to-many)
        -> Pedido -> DetallePedido
```

### Critical Spring Boot Patterns

#### Entity Relationships
- **Cascade Handling**: Careful cascade configuration to avoid EntityExistsException
- **Bidirectional Mapping**: Proper @OneToMany/@ManyToOne with mappedBy
- **Lazy Loading**: FetchType.LAZY for collections, specific queries for eager loading
- **Soft Delete**: Uses `eliminado` boolean flag instead of physical deletion

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
public class EntityService {
    private final EntityRepository repository;
    // Business logic with validation
}
```

#### Controller Patterns
```java
@RestController
@RequestMapping("/api/entities")
@RequiredArgsConstructor
@Tag(name = "Entity Management")
public class EntityController {
    private final EntityService service;
    // REST endpoints with OpenAPI docs
}
```

## Data Initialization

### Automatic Seeding
The `DataInitializationService` runs on startup and creates:

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

### Enhanced Seed Data Features
- **Comprehensive Order Scenarios**: Orders cover all business states and delivery types
- **Realistic Business Data**: Order totals, estimated completion times, payment methods
- **Automatic Invoice Generation**: Invoices created for ENTREGADO orders with proper MercadoPago data
- **Performance Optimized**: Database indexes added for frequently queried fields
- **Data Type Corrections**: CUIL fields updated to Long type for 11-digit number support

## API Endpoints

### Companies (Empresas)
- `GET /api/v1/empresas` - List all companies
- `GET /api/v1/empresas/{id}` - Get company by ID
- `POST /api/v1/empresas` - Create new company
- `PUT /api/v1/empresas/{id}` - Update company
- `DELETE /api/v1/empresas/{id}` - Soft delete company

### Customers (Clientes)
- `GET /api/v1/clientes` - List all customers
- `GET /api/v1/clientes/{id}` - Get customer by ID
- `POST /api/v1/clientes` - Create new customer
- `PUT /api/v1/clientes/{id}` - Update customer
- `DELETE /api/v1/clientes/{id}` - Soft delete customer

### Products (Articulos)
- `GET /api/v1/articulos` - List all products
- `GET /api/v1/articulos/{id}` - Get product by ID
- `POST /api/v1/articulos` - Create new product
- `PUT /api/v1/articulos/{id}` - Update product
- `DELETE /api/v1/articulos/{id}` - Soft delete product
- `GET /api/v1/articulos/buscar-por-tipo` - Search by type (INSUMO/MANUFACTURADO)

### Categories (Categorias)
- `GET /api/v1/categorias` - List all categories
- `GET /api/v1/categorias/{id}` - Get category by ID
- `POST /api/v1/categorias` - Create new category
- `PUT /api/v1/categorias/{id}` - Update category
- `DELETE /api/v1/categorias/{id}` - Soft delete category
- `GET /api/v1/categorias/principales` - Get root categories

### Promotions (Promociones)
- `GET /api/v1/promociones` - List all promotions
- `GET /api/v1/promociones/{id}` - Get promotion by ID
- `POST /api/v1/promociones` - Create new promotion
- `PUT /api/v1/promociones/{id}` - Update promotion
- `DELETE /api/v1/promociones/{id}` - Soft delete promotion
- `GET /api/v1/promociones/vigentes` - Get active promotions

### Branches (Sucursales)
- `GET /api/v1/sucursales` - List all branches
- `GET /api/v1/sucursales/{id}` - Get branch by ID
- `GET /api/v1/sucursales/abiertas` - Get currently open branches

### Orders (Pedidos)
- `GET /api/v1/pedidos` - List all orders
- `GET /api/v1/pedidos/{id}` - Get order by ID
- `GET /api/v1/pedidos/buscar-por-cliente` - Search by customer
- `GET /api/v1/pedidos/buscar-por-estado` - Search by status

### Invoices (Facturas)
- `GET /api/v1/facturas` - List all invoices
- `GET /api/v1/facturas/{id}` - Get invoice by ID
- `GET /api/v1/facturas/estadisticas` - Get billing statistics

### System
- `GET /api/v1/health` - Application health status
- `GET /actuator/health` - Spring Boot health endpoint

## Configuration

### Application Configuration (`application.yml`)
- **Database**: H2 file-based with console enabled
- **JPA**: DDL auto-creation, SQL logging enabled
- **Server**: Port 8080, detailed error responses
- **Actuator**: Health, info, env endpoints exposed
- **OpenAPI**: Swagger UI enabled, package scanning configured

### Development Settings
- **SQL Logging**: Full SQL with parameters visible (DEBUG level)
- **H2 Console**: Available for direct database access
- **Hot Reload**: Spring Boot DevTools not included (add if needed)
- **Profiles**: Currently uses default profile
- **Logging Pattern**: Custom console pattern with timestamp

## Common Development Tasks

### Adding New Entity
1. Create entity class extending `Base` in `entidades/` package
2. Add Lombok annotations (`@SuperBuilder`, `@Getter`, `@Setter`)
3. Create repository interface in `repository/` package
4. Add to `DataInitializationService` if seeding needed
5. Create service class for business logic
6. Add controller for REST endpoints
7. Create DTOs for API responses

### Adding New Endpoint
1. Add method to appropriate service class
2. Add controller method with OpenAPI annotations
3. Create request/response DTOs if needed
4. Test with Swagger UI or HTTP client

### Database Schema Changes
- Schema recreated on each startup (create-drop mode)
- For persistent changes, modify entity annotations
- Check SQL logs to verify correct DDL generation

## Error Handling

### Global Exception Handler
- **Validation Errors**: Detailed field-level error messages
- **Not Found**: Custom exceptions for missing entities
- **Server Errors**: Generic error response with correlation ID
- **Response Format**: Consistent error structure across all endpoints

### Common Error Scenarios
- **Unique Constraint Violations**: Email/username duplicates
- **Foreign Key Violations**: Invalid entity references
- **Validation Failures**: Bean validation annotations
- **Transient Instance Errors**: Incorrect entity persistence order

## Testing and Debugging

### Debugging Application Startup
1. Check application logs for Spring Boot startup messages
2. Verify H2 database connection in logs
3. Monitor DataInitializationService execution
4. Check for JPA entity scanning and repository creation

### API Testing
1. Use Swagger UI at `/swagger-ui.html` for interactive testing
2. Check `/actuator/health` for application status
3. Monitor SQL logs for query analysis
4. Use H2 console for direct database inspection

### Performance Monitoring
- SQL query logging enabled for performance analysis
- Actuator endpoints provide runtime metrics
- H2 console allows direct query execution and analysis

## Security Considerations

### Current Security Model
- **No Authentication**: Open API for development
- **H2 Console**: Publicly accessible (development only)
- **Error Details**: Full error information exposed

### Production Readiness
- Add Spring Security for authentication/authorization
- Disable H2 console and detailed error messages
- Configure proper database with connection pooling
- Add input sanitization and rate limiting

## Lombok Usage Patterns

### Entity Pattern
```java
@Entity
@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"collections"})
public class MyEntity extends Base {
    // Entity fields
}
```

### Service Pattern
```java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MyService {
    private final MyRepository repository;

    private MyDto convertirADto(MyEntity entity) {
        // DTO conversion in service layer
    }
}
```

All entities follow consistent Lombok patterns with proper exclusions to prevent circular references in toString() methods.

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
- **CUIL Field**: Changed from Integer to Long to support 11-digit numbers
- **Validation Enhancements**: Added Jakarta Bean Validation annotations
- **Constraint Improvements**: Proper nullable and unique constraints

### Compilation Verification
- **Build Status**: ✅ Successful compilation with `./gradlew build`
- **Runtime Testing**: ✅ Verified with `./gradlew bootRun`
- **Data Seeding**: ✅ All seed data creates successfully without errors

## Critical Implementation Details

### Enum Handling in Services
Services properly convert String parameters to enums and handle business logic:
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

### Entity Relationship Fixes
- **Bidirectional Mapping**: Fixed Sucursal-Categoria and Sucursal-Promocion relationships
- **JoinColumn Configuration**: Proper @JoinColumn(name = "sucursal_id") in both Categoria and Promocion entities
- **Repository Methods**: Added findBySucursalIdAndEliminadoFalse methods to support relationship queries

## Recent Architecture Improvements

### Service Layer Migration (2024)
- **Architectural Consistency**: All controllers now use services instead of direct repository access
- **PedidoService**: Migrated PedidoController from direct repository access to service layer
- **FacturaService**: Migrated FacturaController from direct repository access to service layer
- **Centralized Business Logic**: DTO conversion moved from controllers to services
- **Transaction Management**: Proper @Transactional annotations at service level
- **Logging Consistency**: Unified logging patterns across all services

### Repository Layer Enhancements
- **PedidoRepository**: Complete repository with Estado enum support and comprehensive query methods
- **FacturaRepository**: Full repository with FormaPago enum support and date range queries
- **Enhanced Queries**: Proper enum-based query methods in CategoriaRepository and PromocionRepository

### API Endpoint Improvements
- **Type Safety**: String to Enum conversion in all applicable endpoints for Estado, FormaPago, TipoPromocion
- **Error Handling**: Improved validation and error responses with proper exception handling
- **Documentation**: Comprehensive OpenAPI specification with parameter descriptions and response examples

### Build and Compilation Fixes
- **Repository Dependencies**: All service classes now have corresponding repository interfaces
- **Method Signatures**: Repository methods use proper enum types instead of String parameters
- **Field Access**: Corrected collection field access patterns throughout the application
- **Import Statements**: Proper enum imports in all relevant controller and service classes

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
- Exclude circular references in @ToString annotations
- Use proper cascade and fetch strategies for performance

### Repository Pattern Best Practices
- All query methods should include `AndEliminadoFalse` for soft delete support
- Use enum parameters in repository method signatures, not String
- Add proper JPQL @Query annotations for complex queries
- Follow consistent naming conventions for search methods

## User Stories Documentation

### Comprehensive Requirements Documentation
The project includes detailed user stories documentation in `src/historiasUsuario.md` with:

- **18 Principal User Stories**: Fully implemented and documented with code references
- **Code Traceability**: Direct links from requirements to implementation (controller methods, service classes, specific line numbers)
- **Implementation Status**: Complete coverage showing what's implemented vs. planned
- **Actor-based Organization**: Stories organized by system actors (admin, manager, customer, etc.)
- **Technical Examples**: Code snippets and API endpoint examples for each story

### User Story to Code Mapping
Controllers include comments linking to specific user stories:
```java
/**
 * Controlador REST para la gestión de empresas
 *
 * Implementa las siguientes historias de usuario:
 * - HU-001: Crear Nueva Empresa
 * - HU-002: Consultar Información de Empresa
 * - HU-003: Actualizar Datos de Empresa
 */
```

### Key User Story Categories
1. **HU-001 to HU-003**: Business management (companies, branches)
2. **HU-004 to HU-005**: Branch operations and scheduling
3. **HU-006 to HU-007**: Product catalog and inventory management
4. **HU-008 to HU-009**: Category organization and hierarchy
5. **HU-010 to HU-011**: Promotional campaigns and offers
6. **HU-012 to HU-014**: Customer relationship management
7. **HU-015**: Order processing and workflow
8. **HU-016 to HU-017**: Billing and sales analytics
9. **HU-018**: Marketing analysis and reporting
10. **HU-019 to HU-021**: System administration and geographic structure

This documentation ensures complete traceability from business requirements through technical implementation, making the codebase highly maintainable and allowing new developers to understand both the why and how of each feature.

## 🔄 Recent Improvements (2025)

### Enhanced Data Seeding (Latest)
- **Comprehensive Order Creation**: Added `crearPedidos()` method creating 5 diverse orders
  - Different states: ENTREGADO, PREPARACION, PENDIENTE, CANCELADO
  - Various delivery types: DELIVERY, TAKE_AWAY
  - Multiple payment methods: EFECTIVO, MERCADO_PAGO
  - Realistic totals and estimated completion times
- **Automatic Invoice Generation**: Added `crearFacturas()` method
  - Automatic invoice creation for delivered orders
  - Proper MercadoPago simulation data
  - Complete billing information with dates and totals
- **Enhanced Client Management**: Modified `crearClientes()` to return client list for order association

### Performance Optimizations
- **Database Indexing**: Added strategic indexes to Pedido entity
  - `idx_pedido_fecha`: Index on fecha_pedido for date-based queries
  - `idx_pedido_estado`: Index on estado for status filtering
  - `idx_pedido_cliente`: Index on cliente_id for customer-based searches
- **Query Performance**: Improved query performance for high-traffic endpoints

### Data Type Corrections
- **CUIL Field Fix**: Updated CUIL from Integer to Long across all layers
  - Entity: Empresa.java - CUIL field updated
  - DTO: EmpresaDto.java, CreateEmpresaRequest.java - consistent types
  - Repository: EmpresaRepository.java - proper Long parameter handling
  - Critical for supporting 11-digit CUIL numbers without overflow

### Validation Enhancements
- **Jakarta Bean Validation**: Added comprehensive validation annotations
  - Base entity: Enhanced with @NotBlank and @Size validations
  - Entity constraints: Proper nullable and unique constraints
  - Input validation: Improved request validation in DTOs

### Build and Compilation
- **✅ Verified Compilation**: Successfully tested with `./gradlew build`
- **✅ Runtime Testing**: Verified application startup with `./gradlew bootRun`
- **✅ Data Integrity**: All seed data creates without persistence errors
- **✅ Type Safety**: No casting or conversion errors with updated data types

### Code Quality Improvements
- **Repository Consistency**: All repositories follow consistent naming patterns
- **Service Layer**: Proper transaction management and error handling
- **Enum Handling**: Robust enum conversion in service methods
- **Entity Relationships**: Proper cascade and fetch configurations