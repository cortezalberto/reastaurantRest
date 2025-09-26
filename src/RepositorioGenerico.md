# Funcionamiento del Repositorio Genérico

## 📋 Tabla de Contenidos
1. [Introducción al Patrón Repository](#introducción-al-patrón-repository)
2. [Arquitectura del GenericRepository](#arquitectura-del-genericrepository)
3. [Funcionamiento Interno](#funcionamiento-interno)
4. [Operaciones CRUD](#operaciones-crud)
5. [Gestión de EntityManager](#gestión-de-entitymanager)
6. [Transacciones y Consistencia](#transacciones-y-consistencia)
7. [Consultas Dinámicas](#consultas-dinámicas)
8. [Flujo de Ejecución](#flujo-de-ejecución)
9. [Casos de Error y Recuperación](#casos-de-error-y-recuperación)
10. [Integración con el Sistema](#integración-con-el-sistema)

---

## 🎯 Introducción al Patrón Repository

### ¿Qué es el Patrón Repository?

El **Patrón Repository** encapsula la lógica necesaria para acceder a fuentes de datos. Centraliza la funcionalidad de acceso a datos común, proporcionando un mejor mantenimiento y desacoplando la infraestructura o tecnología utilizada para acceder a las bases de datos.

### Implementación Genérica

```java
public class GenericRepository<T> {
    private static EntityManagerFactory entityManagerFactory;
    private final Class<T> entityClass;

    // Implementación única para todas las entidades
}
```

**Ventajas del Enfoque Genérico:**
- **Reutilización de Código**: Una implementación para N entidades
- **Type Safety**: Seguridad de tipos en tiempo de compilación
- **Consistencia**: Comportamiento uniforme en toda la aplicación
- **Mantenibilidad**: Cambios centralizados en una sola clase

---

## 🏗️ Arquitectura del GenericRepository

### Diagrama de Componentes

```
┌──────────────────────────────────────────────────────────┐
│                    Application Layer                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐      │
│  │ Main.java   │  │ Entity      │  │ Business    │      │
│  │ (Demo)      │  │ Classes     │  │ Logic       │      │
│  └─────────────┘  └─────────────┘  └─────────────┘      │
└──────────────────────────┬───────────────────────────────┘
                           │
┌──────────────────────────▼───────────────────────────────┐
│                Repository Layer                          │
│  ┌─────────────────────────────────────────────────────┐ │
│  │           GenericRepository<T>                      │ │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │ │
│  │  │   save()    │ │  findById() │ │  findAll()  │   │ │
│  │  └─────────────┘ └─────────────┘ └─────────────┘   │ │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │ │
│  │  │  update()   │ │ deleteById()│ │findByField()│   │ │
│  │  └─────────────┘ └─────────────┘ └─────────────┘   │ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────────┬───────────────────────────────┘
                           │
┌──────────────────────────▼───────────────────────────────┐
│                 Persistence Layer                        │
│  ┌─────────────────────────────────────────────────────┐ │
│  │         EntityManagerFactory (Singleton)           │ │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │ │
│  │  │EntityManager│ │EntityManager│ │EntityManager│   │ │
│  │  │ Instance 1  │ │ Instance 2  │ │ Instance N  │   │ │
│  │  └─────────────┘ └─────────────┘ └─────────────┘   │ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────────┬───────────────────────────────┘
                           │
┌──────────────────────────▼───────────────────────────────┐
│                    Database Layer                        │
│  ┌─────────────────────────────────────────────────────┐ │
│  │                H2 Database                          │ │
│  │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐   │ │
│  │  │   Tables    │ │ Constraints │ │   Indexes   │   │ │
│  │  └─────────────┘ └─────────────┘ └─────────────┘   │ │
│  └─────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

### Componentes Clave

#### 1. EntityManagerFactory (Singleton)
```java
private static EntityManagerFactory entityManagerFactory;

static {
    try {
        entityManagerFactory = Persistence.createEntityManagerFactory("restaurantePU");
        System.out.println("EntityManagerFactory iniciado correctamente");
    } catch (Exception e) {
        System.err.println("Error al inicializar EntityManagerFactory: " + e.getMessage());
        throw new ExceptionInInitializerError(e);
    }
}
```

**Responsabilidades:**
- **Inicialización única**: Se crea una sola vez al cargar la clase
- **Configuración de persistencia**: Lee `persistence.xml`
- **Connection pool management**: Gestiona el pool de conexiones
- **Session factory**: Crea EntityManagers bajo demanda

#### 2. Class<T> entityClass
```java
private final Class<T> entityClass;

public GenericRepository(Class<T> entityClass) {
    this.entityClass = entityClass;
}
```

**Propósito:**
- **Runtime type information**: Información del tipo en tiempo de ejecución
- **Query construction**: Construcción dinámica de consultas JPQL
- **Error messaging**: Mensajes de error específicos por entidad
- **Reflection operations**: Operaciones reflexivas si fuera necesario

---

## ⚙️ Funcionamiento Interno

### Patrón de Ejecución Estándar

Cada operación del repositorio sigue este patrón:

```java
public T operacion(parametros) {
    EntityManager em = getEntityManager();    // 1. Obtener EM
    try {
        em.getTransaction().begin();           // 2. Iniciar transacción

        // 3. Lógica de negocio específica
        T resultado = logicaEspecifica(em, parametros);

        em.getTransaction().commit();          // 4. Confirmar cambios
        return resultado;                      // 5. Retornar resultado

    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();    // 6. Rollback en error
        }
        throw new RuntimeException("Error...", e); // 7. Propagar error
    } finally {
        em.close();                           // 8. Liberar recursos
    }
}
```

### Lifecycle del EntityManager

```
Operación Iniciada
        │
        ▼
┌───────────────┐
│ getEntityManager() │ ──────┐
└───────────────┘           │
        │                   │ Nuevo EM por operación
        ▼                   │
┌───────────────┐           │
│ begin()       │ ◄─────────┘
└───────────────┘
        │
        ▼
┌───────────────┐
│ Operación JPA │ (persist, merge, find, remove)
└───────────────┘
        │
        ▼
┌───────────────┐     ┌───────────────┐
│ commit()      │ ──► │ rollback()    │ (en caso de error)
└───────────────┘     └───────────────┘
        │                     │
        ▼                     ▼
┌─────────────────────────────┐
│ close()                     │ (siempre ejecutado)
└─────────────────────────────┘
        │
        ▼
EntityManager Destroyed
```

---

## 🔧 Operaciones CRUD

### CREATE - save(T entity)

#### Funcionamiento Paso a Paso

```java
public T save(T entity) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
        em.persist(entity);                    // Marcar para inserción
        em.getTransaction().commit();          // Ejecutar INSERT

        // Logging automático
        System.out.println(entityClass.getSimpleName() + " guardado con ID: " +
            ((org.example.entidades.Base) entity).getId());

        return entity;
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al guardar " + entityClass.getSimpleName(), e);
    } finally {
        em.close();
    }
}
```

#### Estados de la Entidad durante save()

```
ANTES:  entity (Transient State)
         │
         │ em.persist(entity)
         ▼
DURANTE: entity (Managed State)
         │
         │ em.commit()
         ▼
DESPUÉS: entity (Detached State) + ID asignado
```

#### SQL Generado
```sql
-- Para Cliente.class
INSERT INTO clientes (
    eliminado, nombre, apellido, email, telefono,
    fecha_nacimiento, imagen_id, usuario_id
) VALUES (?, ?, ?, ?, ?, ?, ?, ?);
```

### READ - findById(Long id)

```java
public Optional<T> findById(Long id) {
    EntityManager em = getEntityManager();
    try {
        T entity = em.find(entityClass, id);   // Primary key lookup
        return Optional.ofNullable(entity);    // Null-safe return
    } finally {
        em.close();                           // No transaction needed
    }
}
```

#### Características Especiales
- **No Transaction Required**: Operaciones de lectura no necesitan transacción
- **Primary Key Optimization**: Búsqueda optimizada por clave primaria
- **Lazy Loading Support**: Las relaciones lazy se cargan bajo demanda
- **Optional Pattern**: Evita NullPointerException

#### SQL Generado
```sql
-- Para Cliente.class con id=1
SELECT c.id, c.eliminado, c.nombre, c.apellido, c.email,
       c.telefono, c.fecha_nacimiento, c.imagen_id, c.usuario_id
FROM clientes c
WHERE c.id = 1;
```

### READ ALL - findAll()

```java
public List<T> findAll() {
    EntityManager em = getEntityManager();
    try {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    } finally {
        em.close();
    }
}
```

#### Criteria API en Acción

```java
// Paso 1: Obtener CriteriaBuilder
CriteriaBuilder cb = em.getCriteriaBuilder();

// Paso 2: Crear query para el tipo específico
CriteriaQuery<T> cq = cb.createQuery(entityClass);

// Paso 3: Definir FROM clause
Root<T> rootEntry = cq.from(entityClass);

// Paso 4: Definir SELECT clause
CriteriaQuery<T> all = cq.select(rootEntry);

// Paso 5: Crear TypedQuery ejecutable
TypedQuery<T> allQuery = em.createQuery(all);
```

**Equivalencia SQL:**
```sql
SELECT * FROM [tabla_entidad];
```

### UPDATE - update(T entity)

```java
public Optional<T> update(T entity) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
        T updatedEntity = em.merge(entity);    // Merge into context
        em.getTransaction().commit();
        return Optional.of(updatedEntity);
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al actualizar " + entityClass.getSimpleName(), e);
    } finally {
        em.close();
    }
}
```

#### Comportamiento de merge()

```
ESCENARIO 1: Entidad existe en contexto
entity (Detached) ──merge()──► Actualiza entidad existente

ESCENARIO 2: Entidad no está en contexto pero existe en BD
entity (Detached) ──merge()──► Carga de BD y actualiza

ESCENARIO 3: Entidad completamente nueva
entity (Transient) ──merge()──► Crea nueva entidad
```

#### SQL Generado
```sql
-- Primero verifica existencia
SELECT c.id, c.eliminado, c.nombre, ... FROM clientes c WHERE c.id = ?;

-- Luego actualiza
UPDATE clientes SET
    eliminado = ?, nombre = ?, apellido = ?, email = ?,
    telefono = ?, fecha_nacimiento = ?, imagen_id = ?, usuario_id = ?
WHERE id = ?;
```

### DELETE - deleteById(Long id)

```java
public Optional<T> deleteById(Long id) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();
        T entity = em.find(entityClass, id);   // Buscar primero
        if (entity != null) {
            em.remove(entity);                 // Marcar para eliminación
            em.getTransaction().commit();
            return Optional.of(entity);        // Retornar entidad eliminada
        }
        em.getTransaction().commit();
        return Optional.empty();               // No encontrada
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        throw new RuntimeException("Error al eliminar " + entityClass.getSimpleName(), e);
    } finally {
        em.close();
    }
}
```

#### Patrón Find-Before-Remove

```
id ──► em.find() ──► entity existe? ──┐
                         │            │
                         ▼ SÍ         ▼ NO
                    em.remove()    return Optional.empty()
                         │
                         ▼
                   return Optional.of(entity)
```

#### SQL Generado
```sql
-- Paso 1: Buscar
SELECT c.id, c.eliminado, c.nombre, ... FROM clientes c WHERE c.id = ?;

-- Paso 2: Eliminar (si existe)
DELETE FROM clientes WHERE id = ?;
```

---

## 🧠 Gestión de EntityManager

### Patrón EntityManager-Per-Operation

```java
private EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
}
```

### Ciclo de Vida del EntityManager

```
┌─────────────────────────────────────────────────────────┐
│                   Operación Iniciada                   │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ EntityManager em = getEntityManager()                  │
│ • Nuevo EntityManager creado                           │
│ • Contexto de persistencia limpio                      │
│ • Conexión obtenida del pool                           │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ try { em.getTransaction().begin() }                     │
│ • Transacción iniciada                                 │
│ • Auto-commit deshabilitado                            │
│ • Locks adquiridos según isolation level               │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ Operación JPA (persist, merge, find, remove)           │
│ • Entidades gestionadas en el contexto                 │
│ • Dirty checking automático                            │
│ • Lazy loading disponible                              │
└─────────────────────┬───────────────────────────────────┘
                      │
                 ┌────┴────┐
                 │ Éxito?  │
                 └────┬────┘
            SÍ ──────┼────── NO
                     │
┌────────────────────▼─────┐ ┌─────────────────────────────┐
│ em.getTransaction()      │ │ em.getTransaction()         │
│   .commit()              │ │   .rollback()               │
│ • Cambios persistidos    │ │ • Cambios descartados       │
│ • Locks liberados        │ │ • Estado previo restaurado  │
└────────────────────┬─────┘ └─────────────────┬───────────┘
                     │                         │
                     └─────────┬───────────────┘
┌─────────────────────────────▼───────────────────────────┐
│ finally { em.close() }                                  │
│ • EntityManager cerrado                                 │
│ • Contexto de persistencia destruido                   │
│ • Conexión retornada al pool                           │
│ • Memoria liberada                                      │
└─────────────────────────────────────────────────────────┘
```

### Ventajas del Patrón

#### ✅ Aislamiento de Operaciones
```java
// Operación 1
clienteRepo.save(cliente1);     // EM1 creado y cerrado

// Operación 2
clienteRepo.save(cliente2);     // EM2 creado y cerrado

// No hay interferencia entre operaciones
```

#### ✅ Thread Safety
```java
// Thread 1
clienteRepo.findById(1L);       // EM1 en Thread 1

// Thread 2
clienteRepo.findById(2L);       // EM2 en Thread 2

// Cada thread tiene su propio EntityManager
```

#### ✅ Memory Management
```java
// Después de cada operación
em.close(); // Libera memoria inmediatamente
```

### Desventajas del Patrón

#### ❌ Overhead de Creación
```java
// Cada operación crea un nuevo EntityManager
EntityManager em = entityManagerFactory.createEntityManager(); // Costo computacional
```

#### ❌ No Reutilización de Cache
```java
// Primera operación
Cliente cliente = clienteRepo.findById(1L); // Carga desde BD

// Segunda operación (inmediata)
Cliente mismo = clienteRepo.findById(1L);    // Carga desde BD nuevamente
```

#### ❌ No Lazy Loading Cross-Operation
```java
Cliente cliente = clienteRepo.findById(1L);
// EntityManager cerrado aquí

cliente.getDomicilios().size(); // LazyInitializationException!
```

---

## 🔄 Transacciones y Consistencia

### Modelo de Transacciones

El GenericRepository utiliza **transacciones explícitas** para operaciones de modificación:

```java
// READ operations - No transaction
public Optional<T> findById(Long id) {
    // No em.getTransaction().begin()
}

// WRITE operations - Explicit transaction
public T save(T entity) {
    em.getTransaction().begin();
    // ... operación
    em.getTransaction().commit();
}
```

### Garantías ACID

#### Atomicidad
```java
public T save(T entity) {
    em.getTransaction().begin();
    em.persist(entity);              // Operación 1
    // Si falla aquí, nada se guarda
    em.getTransaction().commit();    // Operación 2
    // Si falla aquí, se hace rollback
}
```

#### Consistencia
```java
// Las restricciones se validan automáticamente
Cliente cliente = Cliente.builder()
    .email("duplicado@test.com")     // Email que ya existe
    .build();

clienteRepo.save(cliente);          // ConstraintViolationException
```

#### Aislamiento
```java
// Configurado en persistence.xml y base de datos
// Default para H2: READ_COMMITTED
```

#### Durabilidad
```java
// Una vez commit exitoso, datos persisten
em.getTransaction().commit();        // Datos en disco H2
```

### Manejo de Rollback

```java
catch (Exception e) {
    if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();  // Deshacer cambios
    }
    throw new RuntimeException("Error...", e);
}
```

#### Estados de Transacción

```
ACTIVE ──┐
         │ commit() exitoso
         ▼
     COMMITTED

ACTIVE ──┐
         │ excepción o rollback()
         ▼
     ROLLED_BACK
```

#### Casos que Activan Rollback

1. **EntityExistsException**
```java
// Intentar persistir entidad ya gestionada
Cliente existente = clienteRepo.findById(1L).get();
clienteRepo.save(existente); // Rollback automático
```

2. **ConstraintViolationException**
```java
// Violación de unique constraint
Cliente cliente = Cliente.builder()
    .email("email-existente@test.com")
    .build();
clienteRepo.save(cliente); // Rollback automático
```

3. **TransientPropertyValueException**
```java
// Referencia a entidad no persistida
Usuario nuevo = Usuario.builder().build(); // No persistido
Cliente cliente = Cliente.builder()
    .usuario(nuevo) // Error!
    .build();
clienteRepo.save(cliente); // Rollback automático
```

---

## 🔍 Consultas Dinámicas

### findByField() - JPQL Dinámico

```java
public List<T> findByField(String fieldName, Object value) {
    EntityManager em = getEntityManager();
    try {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() +
                     " e WHERE e." + fieldName + " = :value";
        TypedQuery<T> query = em.createQuery(jpql, entityClass);
        query.setParameter("value", value);
        return query.getResultList();
    } catch (Exception e) {
        throw new RuntimeException("Error al buscar por campo " + fieldName, e);
    } finally {
        em.close();
    }
}
```

### Construcción Dinámica de Consultas

#### Paso 1: Construcción del JPQL
```java
// Para Cliente.class con fieldName="nombre"
String jpql = "SELECT e FROM Cliente e WHERE e.nombre = :value";

// Para Articulo.class con fieldName="precioVenta"
String jpql = "SELECT e FROM Articulo e WHERE e.precioVenta = :value";
```

#### Paso 2: Creación de TypedQuery
```java
TypedQuery<T> query = em.createQuery(jpql, entityClass);
```

**Ventajas del TypedQuery:**
- Type safety en tiempo de compilación
- Conversión automática de tipos de resultado
- Better IDE support con autocompletado

#### Paso 3: Parameter Binding
```java
query.setParameter("value", value);
```

**Ventajas del Parameter Binding:**
- **SQL Injection Prevention**: Parámetros escapados automáticamente
- **Type Conversion**: JPA maneja conversión de tipos Java ↔ SQL
- **Query Plan Caching**: Base de datos puede reutilizar planes de ejecución

### Ejemplos de Uso Real

#### Búsqueda de Clientes por Nombre
```java
List<Cliente> juanes = clienteRepo.findByField("nombre", "Juan");

// JPQL generado:
// SELECT e FROM Cliente e WHERE e.nombre = :value

// SQL equivalente:
// SELECT c.id, c.eliminado, c.nombre, c.apellido, c.email, ...
// FROM clientes c WHERE c.nombre = 'Juan'
```

#### Búsqueda de Artículos por Precio
```java
List<Articulo> caros = articuloRepo.findByField("precioVenta", 850.0);

// JPQL generado:
// SELECT e FROM Articulo e WHERE e.precioVenta = :value

// SQL equivalente (con herencia JOINED):
// SELECT a.id, a.eliminado, a.nombre, a.precio_venta, ...,
//        CASE WHEN ai.id IS NOT NULL THEN 1
//             WHEN am.id IS NOT NULL THEN 2 END
// FROM articulo a
// LEFT JOIN articulo_insumo ai ON a.id = ai.id
// LEFT JOIN articulo_manufacturado am ON a.id = am.id
// WHERE a.precio_venta = 850.0
```

#### Búsqueda de Usuarios por Auth0ID
```java
List<Usuario> usuarios = usuarioRepo.findByField("auth0Id", "001");

// SQL equivalente:
// SELECT u.id, u.eliminado, u.nombre, u.username, u.auth0_id
// FROM usuarios u WHERE u.auth0_id = '001'
```

### Limitaciones Actuales

#### Solo Operador de Igualdad
```java
// ✅ SOPORTADO
findByField("precio", 100.0)          // precio = 100.0

// ❌ NO SOPORTADO
findByField("precio > ", 100.0)       // precio > 100.0
findByField("nombre LIKE", "Juan%")   // nombre LIKE 'Juan%'
```

#### Una Sola Condición
```java
// ✅ SOPORTADO
findByField("nombre", "Juan")

// ❌ NO SOPORTADO
findByFields(Map.of("nombre", "Juan", "apellido", "Pérez"))
```

#### Sin Joins
```java
// ❌ NO SOPORTADO
findByField("usuario.username", "admin")  // Join con tabla usuarios
```

### Posibles Extensiones

```java
// Extensiones futuras recomendadas
public List<T> findByFieldLike(String fieldName, String pattern);
public List<T> findByFieldBetween(String fieldName, Object min, Object max);
public List<T> findByFields(Map<String, Object> criteria);
public List<T> findByFieldOrderBy(String fieldName, Object value, String orderBy);
```

---

## 🌊 Flujo de Ejecución

### Flujo Completo de una Operación save()

```
┌─────────────────────────────────────────────────────────┐
│ 1. Aplicación llama clienteRepo.save(cliente)          │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 2. GenericRepository.save(T entity)                    │
│    • Método genérico invocado                          │
│    • T = Cliente (resuelto en tiempo de ejecución)     │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 3. EntityManager em = getEntityManager()               │
│    • Nuevo EntityManager creado desde factory          │
│    • Conexión obtenida del pool C3P0                   │
│    • Contexto de persistencia inicializado             │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 4. em.getTransaction().begin()                          │
│    • Transacción iniciada                              │
│    • Auto-commit deshabilitado                         │
│    • Isolation level aplicado (READ_COMMITTED)         │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 5. em.persist(entity)                                   │
│    • Entidad marcada para inserción                    │
│    • Validaciones de entidad ejecutadas                │
│    • ID será generado en flush/commit                  │
│    • Estado: Transient → Managed                       │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 6. em.getTransaction().commit()                         │
│    • Flush automático ejecutado                        │
│    • SQL INSERT generado y ejecutado                   │
│    • ID auto-generado asignado a la entidad            │
│    • Constraints validados                             │
│    • Cambios persistidos en H2                         │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 7. Logging de confirmación                              │
│    • System.out.println("Cliente guardado con ID: X")  │
│    • Cast a Base para obtener ID                       │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 8. return entity                                        │
│    • Entidad retornada con ID asignado                 │
│    • Estado: Managed → Detached (cuando EM se cierre)  │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ 9. finally { em.close() }                               │
│    • EntityManager cerrado                             │
│    • Contexto de persistencia destruido                │
│    • Conexión retornada al pool                        │
│    • Recursos liberados                                │
└─────────────────────────────────────────────────────────┘
```

### Flujo de Error y Recuperación

```
┌─────────────────────────────────────────────────────────┐
│ Operación en progreso...                                │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ ❌ Exception lanzada                                     │
│    • EntityExistsException                             │
│    • ConstraintViolationException                      │
│    • TransientPropertyValueException                   │
│    • SQLException                                      │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ catch (Exception e)                                     │
│    • Exception capturada                               │
│    • Información de contexto disponible                │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ if (em.getTransaction().isActive())                     │
│    • Verificar si transacción está activa              │
│    • Prevenir IllegalStateException                    │
└─────────────────────┬───────────────────────────────────┘
                      │ SÍ
┌─────────────────────▼───────────────────────────────────┐
│ em.getTransaction().rollback()                          │
│    • Deshacer todos los cambios en la transacción      │
│    • Liberar locks adquiridos                          │
│    • Restaurar estado previo de la base de datos       │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ throw new RuntimeException(mensaje, e)                  │
│    • Wrapping de la excepción original                 │
│    • Mensaje específico con tipo de entidad            │
│    • Preservación del stack trace original             │
└─────────────────────┬───────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────┐
│ finally { em.close() }                                  │
│    • SIEMPRE ejecutado, incluso con excepciones        │
│    • Liberación garantizada de recursos                │
│    • Conexión retornada al pool                        │
└─────────────────────────────────────────────────────────┘
```

### Timing de Operaciones

```
Timeline de save() típico:

t=0ms    │ Inicio de operación
         │
t=5ms    │ EntityManager creado
         │ Conexión obtenida del pool
         │
t=10ms   │ Transacción iniciada
         │
t=15ms   │ persist() ejecutado
         │ Entidad en contexto managed
         │
t=45ms   │ commit() ejecutado
         │ SQL INSERT ejecutado
         │ ID generado y asignado
         │
t=50ms   │ Logging ejecutado
         │
t=55ms   │ EntityManager cerrado
         │ Operación completada
```

---

## 💥 Casos de Error y Recuperación

### Jerarquía de Excepciones

```
Exception
│
├── RuntimeException (lanzada por repositorio)
│   ├── "Error al guardar [EntityType]"
│   ├── "Error al actualizar [EntityType]"
│   ├── "Error al eliminar [EntityType]"
│   └── "Error al buscar por campo [fieldName]"
│
└── Causa raíz (Exception.getCause())
    ├── PersistenceException
    │   ├── EntityExistsException
    │   ├── TransientPropertyValueException
    │   └── ConstraintViolationException
    ├── SQLException
    └── IllegalStateException
```

### Casos de Error Comunes

#### 1. EntityExistsException

**Causa:**
```java
// Intentar persistir entidad ya gestionada/persistida
Cliente existente = clienteRepo.findById(1L).get();
clienteRepo.save(existente); // ❌ Error!
```

**Detección y Manejo:**
```java
try {
    clienteRepo.save(entidad);
} catch (RuntimeException e) {
    if (e.getCause() instanceof EntityExistsException) {
        System.err.println("Entidad ya existe, usar update() en su lugar");
        // Opción: intentar update automáticamente
        return clienteRepo.update(entidad);
    }
}
```

**Solución:**
```java
// ✅ Correcto: Usar update() para entidades existentes
Cliente existente = clienteRepo.findById(1L).get();
existente.setNombre("Nuevo Nombre");
clienteRepo.update(existente); // Correcto
```

#### 2. TransientPropertyValueException

**Causa:**
```java
// Referenciar entidad no persistida
Usuario nuevoUsuario = Usuario.builder()
    .nombre("Test")
    .build(); // No persistido

Cliente cliente = Cliente.builder()
    .usuario(nuevoUsuario) // ❌ Referencia transient
    .build();

clienteRepo.save(cliente); // Error!
```

**Detección:**
```java
try {
    clienteRepo.save(cliente);
} catch (RuntimeException e) {
    if (e.getCause() instanceof TransientPropertyValueException) {
        TransientPropertyValueException tpve =
            (TransientPropertyValueException) e.getCause();

        System.err.println("Entidad no persistida: " + tpve.getPropertyName());
        System.err.println("Valor transient: " + tpve.getTransientEntityName());
    }
}
```

**Solución:**
```java
// ✅ Correcto: Persistir dependencias primero
Usuario usuarioPersistido = usuarioRepo.save(nuevoUsuario);

Cliente cliente = Cliente.builder()
    .usuario(usuarioPersistido) // Referencia a entidad persistida
    .build();

clienteRepo.save(cliente); // Correcto
```

#### 3. ConstraintViolationException

**Causa:**
```java
// Violación de constraint UNIQUE
Cliente cliente1 = Cliente.builder()
    .email("juan@test.com")
    .build();
clienteRepo.save(cliente1); // OK

Cliente cliente2 = Cliente.builder()
    .email("juan@test.com") // ❌ Email duplicado
    .build();
clienteRepo.save(cliente2); // Error!
```

**Detección:**
```java
try {
    clienteRepo.save(cliente);
} catch (RuntimeException e) {
    if (e.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException cve =
            (ConstraintViolationException) e.getCause();

        System.err.println("Violación de constraint: " + cve.getConstraintName());
        System.err.println("SQL State: " + cve.getSQLException().getSQLState());
    }
}
```

#### 4. LazyInitializationException

**Causa:**
```java
// Acceder a colección lazy después de cerrar EntityManager
Cliente cliente = clienteRepo.findById(1L).get();
// EntityManager cerrado aquí

cliente.getDomicilios().size(); // ❌ LazyInitializationException
```

**Solución:**
```java
// ✅ Opción 1: Eager fetching
@OneToMany(fetch = FetchType.EAGER)
private Set<Domicilio> domicilios;

// ✅ Opción 2: Acceso dentro de transacción
// (No aplicable con patrón EM-per-operation actual)

// ✅ Opción 3: DTO pattern
public class ClienteDTO {
    private String nombre;
    private List<String> direcciones;

    public static ClienteDTO from(Cliente cliente) {
        return ClienteDTO.builder()
            .nombre(cliente.getNombre())
            .direcciones(cliente.getDomicilios().stream()
                .map(Domicilio::getNombre)
                .collect(toList()))
            .build();
    }
}
```

### Estrategias de Recuperación

#### 1. Retry Pattern
```java
public T saveWithRetry(T entity, int maxRetries) {
    for (int i = 0; i < maxRetries; i++) {
        try {
            return save(entity);
        } catch (RuntimeException e) {
            if (i == maxRetries - 1) throw e;

            // Log retry attempt
            System.err.println("Retry " + (i+1) + "/" + maxRetries +
                             " para " + entityClass.getSimpleName());

            // Wait before retry
            try { Thread.sleep(100 * (i + 1)); }
            catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        }
    }
    throw new RuntimeException("Max retries exceeded");
}
```

#### 2. Fallback to Update
```java
public T saveOrUpdate(T entity) {
    try {
        return save(entity);
    } catch (RuntimeException e) {
        if (e.getCause() instanceof EntityExistsException) {
            System.out.println("Entity exists, trying update instead...");
            return update(entity).orElse(entity);
        }
        throw e;
    }
}
```

#### 3. Validation Before Save
```java
public T safeSave(T entity) {
    // Pre-validation
    if (entity instanceof Base) {
        Base base = (Base) entity;
        if (base.getId() != null) {
            Optional<T> existing = findById(base.getId());
            if (existing.isPresent()) {
                throw new IllegalArgumentException(
                    "Entity with ID " + base.getId() + " already exists. Use update() instead.");
            }
        }
    }

    return save(entity);
}
```

---

## 🔗 Integración con el Sistema

### Instanciación de Repositorios en Main.java

```java
public class Main {
    // Repositorios estáticos para cada tipo de entidad
    private static GenericRepository<Empresa> empresaRepo =
        new GenericRepository<>(Empresa.class);

    private static GenericRepository<Sucursal> sucursalRepo =
        new GenericRepository<>(Sucursal.class);

    private static GenericRepository<Cliente> clienteRepo =
        new GenericRepository<>(Cliente.class);

    private static GenericRepository<Articulo> articuloRepo =
        new GenericRepository<>(Articulo.class);

    // ... más repositorios para cada entidad
}
```

### Patrón de Uso en Inicialización

```java
private static ConfiguracionBase crearConfiguracionBaseConRepositorio() {
    // 1. Crear entidades usando builder pattern
    Usuario usuario1 = Usuario.builder()
        .nombre("Usuario Principal")
        .auth0Id("001")
        .username("DavidLopez")
        .build();

    // 2. Persistir usando repositorio
    usuario1 = usuarioRepo.save(usuario1);

    // 3. Usar entidad persistida en relaciones
    return new ConfiguracionBase(List.of(usuario1), ...);
}
```

### Integración con Entidades Base

```java
// Todas las entidades extienden Base
public abstract class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // GenericRepository aprovecha estos campos comunes
}

// En save(), se hace cast para logging
System.out.println(entityClass.getSimpleName() + " guardado con ID: " +
    ((org.example.entidades.Base) entity).getId());
```

### Flujo de Demostración CRUD

```java
private static void demostrarOperacionesCRUD() {
    // CREATE
    Usuario nuevoUsuario = Usuario.builder()...build();
    Usuario guardado = usuarioRepo.save(nuevoUsuario);

    // READ
    Optional<Usuario> encontrado = usuarioRepo.findById(guardado.getId());

    // UPDATE
    if (encontrado.isPresent()) {
        Usuario usuario = encontrado.get();
        usuario.setNombre("Actualizado");
        usuarioRepo.update(usuario);
    }

    // SEARCH
    List<Usuario> usuarios = usuarioRepo.findByField("nombre", "Actualizado");

    // DELETE
    usuarioRepo.deleteById(guardado.getId());

    // VERIFY
    List<Usuario> todos = usuarioRepo.findAll();
    System.out.println("Total después de eliminar: " + todos.size());
}
```

### Gestión de Recursos

```java
public static void main(String[] args) {
    try {
        // Operaciones del sistema...
        Empresa empresa = inicializarSistemaConRepositorios();
        mostrarInformacionCompleta(empresa);
        demostrarOperacionesCRUD();

    } catch (Exception e) {
        System.err.println("Error en el sistema: " + e.getMessage());
        e.printStackTrace();
    } finally {
        // Limpieza de recursos
        GenericRepository.closeEntityManagerFactory();
    }
}
```

### Configuración de Persistence Unit

```xml
<!-- persistence.xml referenciado por repositorio -->
<persistence-unit name="restaurantePU" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <!-- Todas las entidades que usa el repositorio -->
    <class>org.example.entidades.Base</class>
    <class>org.example.entidades.Cliente</class>
    <class>org.example.entidades.Empresa</class>
    <!-- ... más clases ... -->

    <properties>
        <!-- Configuración H2 -->
        <property name="jakarta.persistence.jdbc.url"
                  value="jdbc:h2:file:./restaurante_db"/>
        <property name="hibernate.hbm2ddl.auto" value="create-drop"/>
    </properties>
</persistence-unit>
```

---

## 🎯 Conclusión

El `GenericRepository<T>` representa una **implementación elegante y eficiente** del patrón Repository que proporciona:

### ✅ Ventajas Principales

1. **Código DRY**: Una implementación para todas las entidades
2. **Type Safety**: Seguridad de tipos con generics
3. **Transacciones Robustas**: Manejo automático con rollback
4. **Consistencia**: Comportamiento uniforme en toda la aplicación
5. **Simplicidad**: Interfaz clara y fácil de usar
6. **Mantenibilidad**: Cambios centralizados

### 🔧 Características Técnicas

- **EntityManager per Operation**: Aislamiento y thread safety
- **Explicit Transactions**: Control preciso sobre transacciones
- **Exception Wrapping**: Manejo consistente de errores
- **Resource Management**: Liberación automática de recursos
- **JPQL Dynamic Queries**: Consultas flexibles por campo
- **JPA Integration**: Aprovecha todas las características de JPA

### 📊 Rendimiento

- **Operaciones simples**: 10-50ms según complejidad
- **Connection pooling**: Gestión eficiente de conexiones
- **Memory management**: Liberación inmediata de recursos
- **Query optimization**: Uso de Criteria API y parameter binding

### 🚀 Ideal Para

- ✅ Aplicaciones CRUD simples a medianas
- ✅ Prototipado rápido
- ✅ Sistemas educativos/demostración
- ✅ Bases para arquitecturas más complejas

El GenericRepository del sistema de restaurante es un **ejemplo excelente** de cómo implementar persistencia JPA de manera limpia, segura y eficiente, proporcionando una base sólida para el desarrollo de aplicaciones empresariales.