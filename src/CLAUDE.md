# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

This is a Spring Boot 3.2.0 project using Gradle with H2 database and comprehensive REST API architecture.

### Essential Commands
- **Run the application**: `./gradlew bootRun` (Windows: `gradlew.bat bootRun`)
- **Build the project**: `./gradlew build`
- **Clean and rebuild**: `./gradlew clean build`
- **Run tests**: `./gradlew test`
- **Stop running application**: `Ctrl+C` in terminal or kill gradle process

### Application URLs (when running)
- **API Base**: `http://localhost:8080/api/`
- **H2 Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./restaurante_db`, User: `sa`, Password: empty)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`
- **Health Check**: `http://localhost:8080/actuator/health`

### Dependencies
- **Spring Boot 3.2.0**: Web, Data JPA, Validation, Actuator
- **H2 Database 2.2.224**: Embedded database with file persistence
- **Lombok 1.18.32**: Boilerplate code reduction
- **OpenAPI 2.2.0**: API documentation (springdoc-openapi)
- **Java 17**: Required version

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
- **HealthController**: System health monitoring
- **GlobalExceptionHandler**: Centralized error handling
- **OpenAPI Documentation**: Comprehensive API docs with examples

#### 2. Service Layer (`src/main/java/org/example/service/`)
- **EmpresaService**: Business logic for company operations
- **ClienteService**: Business logic for customer operations
- **DataInitializationService**: Automated data seeding on startup
- **Transaction Management**: Declarative transactions with Spring
- **Validation Logic**: Business rule enforcement

#### 3. Repository Layer (`src/main/java/org/example/repository/`)
- **Spring Data JPA**: 13 repository interfaces
- **Custom Queries**: JPQL queries for complex operations
- **Geographic Entities**: PaisRepository, ProvinciaRepository, LocalidadRepository, DomicilioRepository
- **Business Entities**: EmpresaRepository, SucursalRepository, ClienteRepository
- **Product Entities**: ArticuloRepository, CategoriaRepository, PromocionRepository
- **Support Entities**: UsuarioRepository, ImagenRepository, UnidadMedidaRepository

#### 4. Entity Layer (`src/main/java/org/example/entidades/`)
- **Base Entity**: Abstract class with common fields (`id`, `nombre`, `eliminado`)
- **JPA Annotations**: Standard Jakarta JPA with Hibernate
- **Lombok Integration**: Builders, getters, setters, toString
- **Inheritance Strategy**: JOINED table inheritance for Articulo hierarchy

#### 5. DTO Layer (`src/main/java/org/example/dto/`)
- **Response DTOs**: Clean API responses (EmpresaDto, ClienteDto, ArticuloDto)
- **Request DTOs**: Input validation (CreateEmpresaRequest, CreateClienteRequest)
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
All repositories follow this pattern:
```java
List<Entity> findByEliminadoFalse(); // Active records only
List<Entity> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre); // Search
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
```

## API Endpoints

### Companies (Empresas)
- `GET /api/empresas` - List all companies
- `GET /api/empresas/{id}` - Get company by ID
- `POST /api/empresas` - Create new company
- `PUT /api/empresas/{id}` - Update company
- `DELETE /api/empresas/{id}` - Soft delete company

### Customers (Clientes)
- `GET /api/clientes` - List all customers
- `GET /api/clientes/{id}` - Get customer by ID
- `POST /api/clientes` - Create new customer
- `PUT /api/clientes/{id}` - Update customer
- `DELETE /api/clientes/{id}` - Soft delete customer

### System
- `GET /actuator/health` - Application health status
- `GET /api/health` - Custom health endpoint

## Configuration

### Application Configuration (`application.yml`)
- **Database**: H2 file-based with console enabled
- **JPA**: DDL auto-creation, SQL logging enabled
- **Server**: Port 8080, detailed error responses
- **Actuator**: Health, info, env endpoints exposed
- **OpenAPI**: Swagger UI enabled, package scanning configured

### Development Settings
- **SQL Logging**: Full SQL with parameters visible
- **H2 Console**: Available for direct database access
- **Hot Reload**: Spring Boot DevTools not included (add if needed)
- **Profiles**: Currently uses default profile

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
public class MyService {
    private final MyRepository repository;
    // Service methods
}
```

All entities follow consistent Lombok patterns with proper exclusions to prevent circular references in toString() methods.