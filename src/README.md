# Sistema de Gestión de Restaurante con JPA/H2

Un sistema completo de gestión de restaurantes desarrollado en Java con arquitectura JPA/Hibernate y base de datos H2, diseñado para manejar múltiples sucursales, gestión de inventario, pedidos y clientes con persistencia real de datos.

## 🚀 Características Principales

- **Persistencia JPA/H2**: Base de datos embebida con persistencia en archivo
- **Gestión Multi-Sucursal**: Soporte para múltiples empresas y sucursales
- **Catálogo de Productos**: Manejo de artículos manufacturados e insumos
- **Sistema de Pedidos**: Gestión completa de pedidos con detalles
- **Gestión de Clientes**: Registro de usuarios y direcciones
- **Sistema de Promociones**: Promociones con restricciones temporales y por tipo
- **Categorización Jerárquica**: Organización de productos en categorías y subcategorías
- **Gestión Geográfica**: Estructura completa país → provincia → localidad → domicilio
- **Operaciones CRUD Completas**: Con EntityManager y transacciones

## 📋 Requisitos Previos

- **Java**: JDK 17 o superior
- **Gradle**: 7.0 o superior (incluido wrapper)
- **IDE**: IntelliJ IDEA, Eclipse, o VS Code recomendados

## 🛠️ Instalación y Configuración

### 1. Clonar el Repositorio
```bash
git clone <url-del-repositorio>
cd sistemaRestau-main
```

### 2. Verificar Instalación de Java
```bash
java -version
```

### 3. Construir el Proyecto
```bash
# En Windows
gradlew.bat build

# En Linux/Mac
./gradlew build
```

## 🎯 Comandos de Desarrollo

### Comandos Principales

| Comando | Windows | Linux/Mac | Descripción |
|---------|---------|-----------|-------------|
| **Construir** | `gradlew.bat build` | `./gradlew build` | Compila y construye todo el proyecto |
| **Ejecutar** | `gradlew.bat run` | `./gradlew run` | Ejecuta la aplicación principal |
| **Limpiar** | `gradlew.bat clean` | `./gradlew clean` | Elimina archivos de construcción |
| **Compilar** | `gradlew.bat compileJava` | `./gradlew compileJava` | Solo compila el código Java |

### Ejemplo de Ejecución
```bash
# Limpiar y construir
gradlew.bat clean build

# Ejecutar la aplicación
gradlew.bat run
```

### Base de Datos
- **Archivo de BD**: Se crea automáticamente como `restaurante_db.mv.db` en el directorio raíz
- **Consola H2**: Disponible en `http://localhost:8082/h2-console` (si se configura)
- **Usuario**: `sa` (sin contraseña)

## 🏗️ Arquitectura del Sistema

### Estructura del Proyecto
```
src/main/java/org/example/
├── entidades/          # Entidades JPA del dominio
├── repositorio/        # Patrón Repository con EntityManager
└── Main.java          # Punto de entrada de la aplicación

src/main/resources/
└── META-INF/
    └── persistence.xml # Configuración JPA/Hibernate
```

### Patrones de Diseño Implementados

1. **Patrón Repository**: Abstracción del acceso a datos con JPA
2. **Patrón Builder**: Construcción fluida de objetos complejos (Lombok)
3. **Template Method**: Clase base con métodos abstractos
4. **Composition**: Relaciones JPA entre entidades

### Entidades Principales

#### 🏢 Estructura Empresarial
- **Empresa**: Entidad raíz del sistema
- **Sucursal**: Ubicaciones físicas de la empresa

#### 📦 Catálogo de Productos
- **Articulo** (abstracto con herencia JOINED)
  - **ArticuloInsumo**: Ingredientes y materias primas
  - **ArticuloManufacturado**: Productos terminados con recetas

#### 🛒 Gestión de Pedidos
- **Pedido**: Orden principal
- **DetallePedido**: Líneas de pedido con cantidades y precios

#### 👥 Gestión de Clientes
- **Cliente**: Información del cliente
- **Usuario**: Datos de acceso y contacto
- **Domicilio**: Direcciones de entrega

#### 🌍 Estructura Geográfica
- **Pais** → **Provincia** → **Localidad** → **Domicilio**

#### 🏷️ Organización
- **Categoria**: Clasificación jerárquica de productos
- **Promocion**: Campañas promocionales con restricciones

## 🔧 Tecnologías Utilizadas

### Persistencia y Base de Datos
- **Jakarta JPA 3.1.0**: Especificación de persistencia
- **Hibernate 6.4.4.Final**: Proveedor JPA
- **H2 Database 2.2.224**: Base de datos embebida
- **SLF4J**: Sistema de logging

### Core
- **Java 17+**: Lenguaje principal
- **Gradle**: Sistema de construcción
- **Lombok**: Reducción de código boilerplate

### Características de JPA Implementadas
- **Entity Mapping**: Mapeo completo de entidades
- **Relationship Mapping**: Relaciones `@OneToMany`, `@ManyToOne`, `@OneToOne`, `@ManyToMany`
- **Inheritance Mapping**: Estrategia JOINED para jerarquías
- **Lazy Loading**: Optimización de carga de datos
- **Transaction Management**: Manejo automático de transacciones
- **Schema Generation**: Creación automática del esquema

### Características de Lombok
- `@SuperBuilder`: Patrón Builder para jerarquías de herencia
- `@Getter/@Setter`: Generación automática de métodos
- `@NoArgsConstructor/@AllArgsConstructor`: Constructores
- `@ToString`: Con exclusiones para evitar ciclos

## 📊 Modelo de Datos JPA

### Relaciones Principales
```
Empresa
├── Sucursal (1:N) [@OneToMany mappedBy="empresa"]
    ├── Categoria (1:N) [@OneToMany with @JoinColumn]
    │   ├── Articulo (1:N) [@OneToMany mappedBy="categoria"]
    │   └── Subcategorias (1:N) [@OneToMany mappedBy="categoriaPadre"]
    ├── Promocion (1:N) [@OneToMany with @JoinColumn]
    └── Pedido (1:N) [through Cliente]

Cliente
├── Usuario (1:1) [@OneToOne with @JoinColumn]
├── Domicilio (1:N) [@OneToMany with @JoinColumn]
└── Pedido (1:N) [@OneToMany mappedBy="cliente"]
    └── DetallePedido (1:N) [@OneToMany with @JoinColumn]
```

### Campos Comunes (Clase Base)
Todas las entidades heredan de `Base.java` (`@MappedSuperclass`):
- `id`: Identificador único (`@Id @GeneratedValue`)
- `nombre`: Nombre descriptivo (`@Column`)
- `eliminado`: Flag de eliminación lógica (`@Column`)

## 🗄️ Configuración de Persistencia

### persistence.xml
```xml
<persistence-unit name="restaurantePU" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <!-- Configuración H2 con persistencia en archivo -->
    <property name="jakarta.persistence.jdbc.url"
              value="jdbc:h2:file:./restaurante_db;DB_CLOSE_ON_EXIT=FALSE;AUTO_RECONNECT=TRUE"/>
    <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
    <property name="hibernate.show_sql" value="true"/>
</persistence-unit>
```

## 🚦 Inicialización del Sistema

El sistema incluye datos de prueba pre-cargados en `Main.java` que se persisten automáticamente:

### Datos Inicializados
- ✅ Estructura geográfica completa (persistida en BD)
- ✅ Empresa con múltiples sucursales
- ✅ Catálogo de productos (insumos y manufacturados)
- ✅ Categorías organizadas jerárquicamente
- ✅ Promociones con restricciones temporales
- ✅ Clientes y usuarios de ejemplo

## 🔍 Funcionalidades del Repository JPA

### Operaciones CRUD Disponibles
```java
// GenericRepository<T> con EntityManager
// Crear
T save(T entity)

// Leer
Optional<T> findById(Long id)
List<T> findAll()

// Actualizar
Optional<T> update(T entity)

// Eliminar
Optional<T> deleteById(Long id)

// Búsquedas personalizadas
List<T> findByField(String fieldName, Object value)
```

### Características Avanzadas
- **Gestión de Transacciones**: Automática con try-catch
- **EntityManager**: Gestión completa del ciclo de vida
- **JPQL**: Consultas personalizadas por campo
- **Lazy Loading**: Optimización de rendimiento
- **Connection Pool**: Configurado para múltiples conexiones

## 🎯 Casos de Uso Principales

1. **Persistencia Real**: Los datos se mantienen entre ejecuciones
2. **Gestión de Sucursales**: Crear y administrar múltiples ubicaciones
3. **Catálogo de Productos**: Mantener inventario con relaciones complejas
4. **Procesamiento de Pedidos**: Crear pedidos con transacciones seguras
5. **Gestión de Clientes**: Registrar clientes con relaciones bidireccionales
6. **Promociones**: Configurar ofertas con validación de datos

## 🚀 Funcionalidades Implementadas

### ✅ Completado
- [x] Persistencia JPA/Hibernate con H2
- [x] Repositorio genérico con EntityManager
- [x] Relaciones bidireccionales JPA
- [x] Transacciones automáticas
- [x] Generación automática de esquema
- [x] Lazy loading optimizado
- [x] Logging SQL detallado
- [x] Manejo de errores robusto

### 📈 Próximos Pasos Recomendados
- [ ] Implementar API REST con Spring Boot
- [ ] Agregar validaciones Bean Validation
- [ ] Implementar autenticación JWT
- [ ] Agregar pruebas unitarias con JPA Test
- [ ] Implementar paginación y ordenamiento
- [ ] Agregar cacheo de segundo nivel
- [ ] Métricas y monitoring

### 🔧 Extensiones Posibles
- [ ] Migrar a PostgreSQL/MySQL
- [ ] Implementar Spring Data JPA
- [ ] Agregar Flyway para migraciones
- [ ] Sistema de auditoría con Envers
- [ ] Implementar criterios dinámicos
- [ ] Connection pooling avanzado (HikariCP)

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama para nueva funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit de cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📝 Notas de Desarrollo

### Convenciones JPA
- Usar `@Entity` en todas las entidades persistentes
- `@MappedSuperclass` para clases base
- `FetchType.LAZY` por defecto para relaciones
- `mappedBy` para relaciones bidireccionales
- `@ToString(exclude={...})` para evitar ciclos

### Manejo de Transacciones
```java
EntityManager em = getEntityManager();
try {
    em.getTransaction().begin();
    // Operaciones
    em.getTransaction().commit();
} catch (Exception e) {
    if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
    }
    throw new RuntimeException("Error", e);
} finally {
    em.close();
}
```

### Debugging
- SQL logging habilitado en `persistence.xml`
- Logs de EntityManager en consola
- Archivo de base de datos visible: `restaurante_db.mv.db`

## 📄 Licencia

Este proyecto está bajo la licencia [especificar licencia].

---

**Desarrollado por**: Los Cortez Team
**Versión**: 2.0.0 (JPA/H2)
**Última actualización**: Septiembre 2025
**Tecnologías**: Java 17, Jakarta JPA 3.1, Hibernate 6.4, H2 Database, Lombok