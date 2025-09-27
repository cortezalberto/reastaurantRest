# Sistema de Gestión de Restaurante - API REST

**TechFood Solutions Restaurant Management System**

Una API REST completa desarrollada con Spring Boot 3.2.0 para la gestión integral de restaurantes, incluyendo manejo de empresas, sucursales, productos, clientes, pedidos y estructura geográfica.

## 🚀 Características Principales

- **API REST Completa** con documentación OpenAPI/Swagger
- **Base de datos H2** embebida con persistencia en archivo
- **Arquitectura en capas** siguiendo patrón Controller-Service-Repository
- **Validación automática** de datos con Bean Validation mejorada
- **Manejo de errores** centralizado y consistente
- **Monitoreo** con Spring Boot Actuator
- **Soft Delete** para mantener integridad histórica
- **Seeding automático** de datos de prueba
- **Índices optimizados** para consultas de alto rendimiento
- **Tipos de datos corregidos** para mayor precisión (CUIL Long)
- **Validaciones robustas** en todas las capas

## 📋 Requisitos

- **Java 17** o superior
- **Gradle 7.0+** (wrapper incluido)
- Puerto **8080** disponible (configurable)

## 🛠️ Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd ComercioJpa
```

### 2. Ejecutar la aplicación
```bash
# Linux/Mac
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

### 3. Verificar la instalación
- **API Base**: http://localhost:8080/api/v1/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health
- **API Docs**: http://localhost:8080/api-docs

## 🏗️ Arquitectura del Sistema

### Estructura de Capas

```
┌─────────────────────────────────────┐
│           Controller Layer          │  ← REST Endpoints
├─────────────────────────────────────┤
│            Service Layer            │  ← Business Logic
├─────────────────────────────────────┤
│          Repository Layer           │  ← Data Access
├─────────────────────────────────────┤
│            Entity Layer             │  ← Domain Model
└─────────────────────────────────────┘
```

### Tecnologías Utilizadas

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Framework | Spring Boot | 3.2.0 |
| Base de Datos | H2 Database | 2.2.224 |
| Persistencia | Spring Data JPA | 3.2.0 |
| Documentación | OpenAPI | 2.2.0 |
| Boilerplate | Lombok | 1.18.32 |
| Build Tool | Gradle | 8.0+ |
| Java | OpenJDK | 17 |

## 📊 Modelo de Dominio

### Jerarquía Geográfica
```
Pais → Provincia → Localidad → Domicilio
```

### Estructura Empresarial
```
Empresa → Sucursal → Categoria
                 → Promocion
                 → Articulo (Insumo/Manufacturado)
```

### Gestión de Usuarios y Pedidos
```
Usuario → Cliente → Domicilio (many-to-many)
       → Pedido → DetallePedido → Factura
```

### Catálogo de Productos
```
Articulo (Abstract)
├── ArticuloInsumo (Ingredientes)
└── ArticuloManufacturado (Productos finales)
    └── ArticuloManufacturadoDetalle (Recetas)
```

## 🔗 API Endpoints

### Empresas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/empresas` | Listar todas las empresas |
| GET | `/api/v1/empresas/{id}` | Obtener empresa por ID |
| POST | `/api/v1/empresas` | Crear nueva empresa |
| PUT | `/api/v1/empresas/{id}` | Actualizar empresa |
| DELETE | `/api/v1/empresas/{id}` | Eliminar empresa (soft delete) |

### Clientes
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/clientes` | Listar todos los clientes |
| GET | `/api/v1/clientes/{id}` | Obtener cliente por ID |
| POST | `/api/v1/clientes` | Crear nuevo cliente |
| PUT | `/api/v1/clientes/{id}` | Actualizar cliente |
| DELETE | `/api/v1/clientes/{id}` | Eliminar cliente (soft delete) |

### Productos (Artículos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/articulos` | Listar todos los productos |
| GET | `/api/v1/articulos/{id}` | Obtener producto por ID |
| POST | `/api/v1/articulos` | Crear nuevo producto |
| PUT | `/api/v1/articulos/{id}` | Actualizar producto |
| DELETE | `/api/v1/articulos/{id}` | Eliminar producto (soft delete) |
| GET | `/api/v1/articulos/buscar-por-tipo` | Buscar por tipo (INSUMO/MANUFACTURADO) |

### Categorías
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/categorias` | Listar todas las categorías |
| GET | `/api/v1/categorias/{id}` | Obtener categoría por ID |
| POST | `/api/v1/categorias` | Crear nueva categoría |
| PUT | `/api/v1/categorias/{id}` | Actualizar categoría |
| DELETE | `/api/v1/categorias/{id}` | Eliminar categoría (soft delete) |
| GET | `/api/v1/categorias/principales` | Obtener categorías raíz |

### Promociones
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/promociones` | Listar todas las promociones |
| GET | `/api/v1/promociones/{id}` | Obtener promoción por ID |
| POST | `/api/v1/promociones` | Crear nueva promoción |
| PUT | `/api/v1/promociones/{id}` | Actualizar promoción |
| DELETE | `/api/v1/promociones/{id}` | Eliminar promoción (soft delete) |
| GET | `/api/v1/promociones/vigentes` | Obtener promociones activas |

### Sucursales
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/sucursales` | Listar todas las sucursales |
| GET | `/api/v1/sucursales/{id}` | Obtener sucursal por ID |
| GET | `/api/v1/sucursales/abiertas` | Obtener sucursales actualmente abiertas |

### Pedidos
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/pedidos` | Listar todos los pedidos |
| GET | `/api/v1/pedidos/{id}` | Obtener pedido por ID |
| GET | `/api/v1/pedidos/buscar-por-cliente` | Buscar pedidos por cliente |
| GET | `/api/v1/pedidos/buscar-por-estado` | Buscar pedidos por estado |

### Facturas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/facturas` | Listar todas las facturas |
| GET | `/api/v1/facturas/{id}` | Obtener factura por ID |
| GET | `/api/v1/facturas/estadisticas` | Obtener estadísticas de facturación |

### Sistema
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/actuator/health` | Estado de salud de la aplicación |
| GET | `/api/v1/health` | Health check personalizado |
| GET | `/swagger-ui.html` | Documentación interactiva |
| GET | `/h2-console` | Consola de base de datos H2 |

## 💾 Base de Datos

### Configuración H2
- **Archivo**: `restaurante_db.mv.db` (creado automáticamente)
- **URL JDBC**: `jdbc:h2:file:./restaurante_db`
- **Usuario**: `sa`
- **Contraseña**: (vacía)
- **Modo**: `create-drop` (recrea esquema en cada inicio)

### Acceso a H2 Console
1. Navegar a http://localhost:8080/h2-console
2. Usar la URL JDBC: `jdbc:h2:file:./restaurante_db`
3. Usuario: `sa`, Contraseña: (dejar vacía)

## 🌱 Datos de Prueba

La aplicación incluye un servicio de inicialización automática que crea datos de prueba:

### Estructura Geográfica
- **País**: Argentina
- **Provincia**: Mendoza
- **Localidades**: Maipú, Godoy Cruz
- **Domicilios**: Direcciones de ejemplo

### Empresa de Ejemplo
- **Nombre**: TechFood Solutions
- **Sucursales**:
  - Sucursal Central (Maipú)
  - Sucursal Norte (Godoy Cruz)

### Catálogo de Productos
- **Insumos**: Cerveza Quilmes 473ml, Masa fresca para pizza mediana
- **Manufacturados**: Pizza Especial con ingredientes premium, Combo completo pizza + bebida

### Clientes de Prueba
- **David López**: Cliente con domicilio en Maipú
- **Tomás Ferro**: Cliente con domicilio en Godoy Cruz
- Estructura completa con usuarios, domicilios y relaciones

### Pedidos y Facturas
- **5 Pedidos de Prueba**: Con diferentes estados (ENTREGADO, PREPARACION, PENDIENTE, CANCELADO)
- **Facturación Automática**: Facturas generadas para pedidos entregados
- **Formas de Pago**: EFECTIVO, MERCADO_PAGO con simulación de datos

## 🎯 Comandos de Desarrollo

### Comandos Gradle Esenciales
```bash
# Ejecutar aplicación
./gradlew bootRun

# Construir proyecto
./gradlew build

# Limpiar y construir
./gradlew clean build

# Ejecutar tests
./gradlew test

# Ver dependencias
./gradlew dependencies
```

### Comandos de Base de Datos
```bash
# La base de datos se crea automáticamente
# Archivo: restaurante_db.mv.db

# Para recrear completamente:
rm restaurante_db.mv.db
./gradlew bootRun
```

## 🔧 Configuración

### Variables de Entorno
```bash
# Puerto del servidor (default: 8080)
SERVER_PORT=8080

# Perfil activo (default: default)
SPRING_PROFILES_ACTIVE=default
```

### Configuración de Base de Datos
Modificar `application.yml` para usar diferentes bases de datos:
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./restaurante_db
    username: sa
    password:
    driver-class-name: org.h2.Driver
```

## 📖 Documentación API

### Swagger UI
Acceder a http://localhost:8080/swagger-ui.html para:
- Explorar todos los endpoints disponibles
- Probar las APIs directamente
- Ver esquemas de request/response
- Descargar especificación OpenAPI

### Ejemplos de Request

#### Crear Empresa
```json
POST /api/v1/empresas
{
  "nombre": "Nueva Empresa",
  "razonSocial": "Nueva Empresa S.A.",
  "cuil": 20123456789
}
```

#### Crear Cliente
```json
POST /api/v1/clientes
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "261-1234567",
  "email": "juan.perez@email.com"
}
```

#### Buscar Pedidos por Estado
```bash
GET /api/v1/pedidos/buscar-por-estado?estado=PENDIENTE
```

#### Buscar Promociones Vigentes
```bash
GET /api/v1/promociones/vigentes
```

#### Obtener Estadísticas de Facturación
```bash
GET /api/v1/facturas/estadisticas
```

## 🧪 Testing

### Ejecutar Tests
```bash
./gradlew test
```

### Testing Manual
1. **Swagger UI**: Interface web para probar endpoints
2. **H2 Console**: Verificar datos directamente en BD
3. **Actuator**: Monitorear salud de la aplicación
4. **Logs**: Revisar logs de SQL para debugging

### Herramientas Recomendadas
- **Postman**: Para testing de APIs
- **curl**: Para testing desde línea de comandos
- **IntelliJ IDEA**: IDE recomendado con soporte completo
- **VS Code**: Con extensiones de Java y Spring Boot

## 🔍 Debugging y Solución de Problemas

### Logs Importantes
```bash
# Ver logs en tiempo real
./gradlew bootRun --console=verbose

# Los logs incluyen:
# - SQL queries ejecutadas
# - Mapeo de entidades JPA
# - Inicialización de Spring Boot
# - Errores de validación
```

### Problemas Comunes

#### Puerto 8080 en uso
```bash
# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <pid> /F
```

#### Base de datos corrupta
```bash
rm restaurante_db.mv.db
./gradlew bootRun
```

#### Error de compilación Lombok
```bash
./gradlew clean build
# Verificar que IDE tenga plugin de Lombok instalado
```

## 📁 Estructura del Proyecto

```
src/main/java/org/example/
├── ComercioJpaApplication.java          # Punto de entrada Spring Boot
├── controller/                          # Controladores REST
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
├── service/                             # Lógica de negocio
│   ├── EmpresaService.java
│   ├── ClienteService.java
│   ├── ArticuloService.java
│   ├── CategoriaService.java
│   ├── PromocionService.java
│   ├── PedidoService.java
│   ├── FacturaService.java
│   └── DataInitializationService.java
├── repository/                          # Acceso a datos
│   ├── EmpresaRepository.java
│   ├── ClienteRepository.java
│   ├── ArticuloRepository.java
│   ├── CategoriaRepository.java
│   ├── PromocionRepository.java
│   ├── PedidoRepository.java
│   ├── FacturaRepository.java
│   └── ... (8 repositorios más)
├── entidades/                           # Modelo de dominio
│   ├── Base.java                       # Entidad base
│   ├── Empresa.java
│   ├── Cliente.java
│   ├── Articulo.java                   # Jerarquía JOINED
│   ├── ArticuloInsumo.java
│   ├── ArticuloManufacturado.java
│   ├── Categoria.java
│   ├── Promocion.java
│   ├── Pedido.java
│   ├── Factura.java
│   └── ... (12 entidades más)
└── dto/                                # Data Transfer Objects
    ├── EmpresaDto.java
    ├── ClienteDto.java
    ├── CategoriaDto.java
    ├── PromocionDto.java
    ├── PedidoDto.java
    ├── FacturaDto.java
    ├── SucursalDto.java
    └── CreateXxxRequest.java (multiple)
```

## 🛡️ Seguridad

### Estado Actual (Desarrollo)
- ⚠️ **Sin autenticación**: API abierta para desarrollo
- ⚠️ **H2 Console público**: Accesible sin credenciales
- ⚠️ **Errores detallados**: Información completa de errores

### Para Producción
- ✅ Implementar Spring Security
- ✅ Desactivar H2 Console
- ✅ Configurar base de datos externa
- ✅ Ocultar detalles de errores
- ✅ Implementar rate limiting
- ✅ Validación y sanitización de inputs

## 🚀 Despliegue

### Construcción para Producción
```bash
./gradlew clean build -Pprod
```

### Variables de Entorno Requeridas
```bash
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://localhost:5432/restaurante
DATABASE_USERNAME=username
DATABASE_PASSWORD=password
```

### Docker (Opcional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contribución

### Agregar Nueva Entidad
1. Crear clase en `entidades/` extendiendo `Base`
2. Agregar anotaciones Lombok estándar
3. Crear repositorio en `repository/`
4. Implementar servicio en `service/`
5. Crear controlador en `controller/`
6. Agregar DTOs correspondientes

### Patrones de Código
- **Lombok**: Usar `@SuperBuilder`, `@Getter`, `@Setter`
- **Servicios**: Usar `@Service`, `@RequiredArgsConstructor`, `@Transactional`
- **Controladores**: Usar `@RestController`, documentación OpenAPI
- **Repositorios**: Extender `JpaRepository<Entity, Long>`

## 📚 Referencias

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [H2 Database](https://www.h2database.com/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Lombok Documentation](https://projectlombok.org/)

## 📝 Licencia

Este proyecto es desarrollado para fines educativos y demostrativos del patrón de arquitectura REST con Spring Boot.

---

**Desarrollo por**: TechFood Solutions
**Versión**: 2.1 (Spring Boot + Refactorización)
**Última actualización**: 2025

## 🔄 Changelog v2.1 (2025)

### Mejoras Críticas Implementadas
- **🔧 Tipos de Datos Corregidos**: CUIL actualizado de Integer a Long para soportar números de 11 dígitos
- **✅ Validaciones Mejoradas**: Agregadas anotaciones Jakarta Validation en entidades base y específicas
- **📈 Optimización de BD**: Añadidos índices en tabla pedidos para mejorar rendimiento
- **🏗️ Arquitectura Consistente**: Mantenimiento de patrones Controller-Service-Repository
- **🛡️ Integridad de Datos**: Constraints nullable y unique aplicados correctamente

### Refactorización de Código
- **Controller Layer**: 8 controladores REST con documentación OpenAPI completa
- **Service Layer**: Capa de servicios con lógica de negocio y manejo de transacciones
- **Repository Layer**: 15 repositorios Spring Data JPA con consultas personalizadas
- **DTO Layer**: DTOs para request/response con validación Bean Validation
- **Entity Layer**: 22 entidades JPA con relaciones bidireccionales y herencia JOINED
- **Data Seeding**: Servicio de inicialización automática con datos de prueba completos

### Verificación de Calidad
- **✅ Compilación Exitosa**: Todas las dependencias actualizadas correctamente
- **✅ Consistencia de Tipos**: Sin errores de casting o conversión
- **✅ Documentación Actualizada**: User stories y ejemplos con tipos correctos

## 📞 Soporte

Para reportar issues o solicitar features:
- Crear issue en el repositorio
- Revisar documentación de Spring Boot
- Consultar logs de aplicación para debugging