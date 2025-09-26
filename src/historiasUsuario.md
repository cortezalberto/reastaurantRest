# Historias de Usuario - Sistema de Gestión de Restaurante

## 📋 Índice
1. [Gestión Empresarial](#gestión-empresarial)
2. [Gestión de Sucursales](#gestión-de-sucursales)
3. [Gestión de Productos e Inventario](#gestión-de-productos-e-inventario)
4. [Gestión de Categorías](#gestión-de-categorías)
5. [Gestión de Promociones](#gestión-de-promociones)
6. [Gestión de Clientes](#gestión-de-clientes)
7. [Gestión de Pedidos](#gestión-de-pedidos)
8. [Gestión Geográfica](#gestión-geográfica)
9. [Reportes y Análisis](#reportes-y-análisis)
10. [Administración del Sistema](#administración-del-sistema)

---

## 🏢 Gestión Empresarial

### HU-001: Crear Nueva Empresa
**Como** administrador del sistema
**Quiero** registrar una nueva empresa en el sistema
**Para** poder gestionar múltiples empresas del grupo corporativo

**Criterios de Aceptación:**
- Debo poder ingresar el nombre de la empresa
- Debo poder ingresar la razón social
- Debo poder ingresar el CUIL de la empresa
- El sistema debe generar automáticamente un ID único
- La empresa debe quedar marcada como activa (eliminado = false)
- Debo poder ver la información completa de la empresa creada

**Ejemplo de Implementación:**
```java
Empresa nuevaEmpresa = Empresa.builder()
    .nombre("TechFood Solutions")
    .razonSocial("TechFood Solutions S.A.")
    .cuil(2035620636)
    .build();

Empresa empresaGuardada = empresaRepo.save(nuevaEmpresa);
```

**Resultado Esperado:**
```
Empresa guardado con ID: 1
Empresa: TechFood Solutions - TechFood Solutions S.A. - 0 sucursales
```

### HU-002: Consultar Información de Empresa
**Como** gerente corporativo
**Quiero** ver la información detallada de una empresa
**Para** conocer su estado actual y cantidad de sucursales

**Criterios de Aceptación:**
- Debo poder buscar empresa por ID
- Debo ver nombre, razón social y CUIL
- Debo ver la cantidad de sucursales asociadas
- Debo ver el estado de la empresa (activa/inactiva)

### HU-003: Actualizar Datos de Empresa
**Como** administrador del sistema
**Quiero** modificar los datos de una empresa existente
**Para** mantener la información actualizada

**Criterios de Aceptación:**
- Debo poder modificar nombre, razón social y CUIL
- Los cambios deben persistir en la base de datos
- El sistema debe mantener las relaciones con sucursales
- Debo recibir confirmación de la actualización exitosa

---

## 🏪 Gestión de Sucursales

### HU-004: Registrar Nueva Sucursal
**Como** gerente de expansión
**Quiero** registrar una nueva sucursal
**Para** expandir la presencia geográfica de la empresa

**Criterios de Aceptación:**
- Debo poder ingresar el nombre de la sucursal
- Debo poder asignar un domicilio existente
- Debo poder establecer horarios de apertura y cierre
- La sucursal debe quedar asociada automáticamente a la empresa
- Debo poder ver la sucursal en la lista de sucursales de la empresa

**Ejemplo de Implementación:**
```java
Sucursal nuevaSucursal = Sucursal.builder()
    .nombre("Casa Matriz Centro")
    .horarioApertura(LocalTime.of(11, 0))
    .horarioCierre(LocalTime.of(23, 0))
    .domicilio(domicilioExistente)
    .build();

empresa.addSucursal(nuevaSucursal); // Relación bidireccional automática
```

**Resultado Esperado:**
```
Sucursal guardado con ID: 1
• Sucursal: Casa Matriz Centro - 0 categorías
  Horario: 11:00 - 23:00
  Dirección: Domicilio: San Martín 1000 - CP: 5501
```

### HU-005: Consultar Sucursales por Empresa
**Como** gerente corporativo
**Quiero** ver todas las sucursales de una empresa
**Para** tener una visión general de la red de locales

**Criterios de Aceptación:**
- Debo ver la lista completa de sucursales
- Cada sucursal debe mostrar: nombre, horarios, dirección
- Debo ver la cantidad de categorías y promociones por sucursal
- Debo poder acceder a los detalles de cada sucursal

### HU-006: Actualizar Horarios de Sucursal
**Como** gerente de sucursal
**Quiero** modificar los horarios de atención
**Para** adaptarme a las necesidades del mercado local

**Criterios de Aceptación:**
- Debo poder cambiar hora de apertura y cierre
- Los cambios deben ser inmediatos
- Debo recibir confirmación del cambio
- Los horarios deben mostrarse en formato 24 horas

---

## 📦 Gestión de Productos e Inventario

### HU-007: Registrar Artículo Insumo
**Como** encargado de compras
**Quiero** registrar un nuevo insumo en el sistema
**Para** poder gestionar el inventario de materias primas

**Criterios de Aceptación:**
- Debo poder ingresar nombre y denominación del insumo
- Debo poder establecer precio de compra y venta
- Debo poder definir stock actual y máximo
- Debo poder marcar si es para elaboración o venta directa
- Debo poder asignar unidad de medida
- El sistema debe generar ID único automáticamente

**Ejemplo de Implementación:**
```java
ArticuloInsumo cerveza = ArticuloInsumo.builder()
    .nombre("Cerveza Quilmes")
    .denominacion("Cerveza Quilmes 473ml")
    .precioVenta(150.0)
    .precioCompra(80.0)
    .stockActual(50)
    .stockMaximo(200)
    .esParaElaborar(false)
    .unidadMedida(unidadMedidaExistente)
    .build();
```

**Resultado Esperado:**
```
Articulo guardado con ID: 1
ArticuloInsumo: Cerveza Quilmes 473ml - Stock: 50
```

### HU-008: Registrar Artículo Manufacturado
**Como** chef o encargado de cocina
**Quiero** registrar un producto elaborado
**Para** poder gestionar el menú de productos terminados

**Criterios de Aceptación:**
- Debo poder ingresar nombre, denominación y descripción
- Debo poder establecer tiempo estimado de preparación
- Debo poder describir el proceso de preparación
- Debo poder definir el precio de venta
- Debo poder agregar receta con insumos y cantidades
- El producto debe quedar disponible para pedidos

**Ejemplo de Implementación:**
```java
ArticuloManufacturado pizza = ArticuloManufacturado.builder()
    .nombre("Pizza Especial")
    .denominacion("Pizza Especial con ingredientes premium")
    .descripcion("Pizza artesanal con masa fresca...")
    .tiempoEstimadoMinutos(25)
    .preparacion("Estirar masa, agregar salsa...")
    .precioVenta(850.0)
    .build();

// Agregar receta
pizza.addDetalle(ArticuloManufacturadoDetalle.builder()
    .nombre("Detalle Masa")
    .cantidad(1)
    .articuloInsumo(masaParaPizza)
    .build());
```

### HU-009: Consultar Stock de Insumos
**Como** encargado de cocina
**Quiero** consultar el stock actual de insumos
**Para** saber qué productos están disponibles

**Criterios de Aceptación:**
- Debo ver lista completa de todos los insumos
- Debo ver stock actual vs stock máximo
- Debo identificar fácilmente productos con stock bajo
- Debo ver precios de compra y venta
- Debo poder filtrar por productos para elaboración

**Resultado Esperado:**
```
✓ Total artículos en repositorio: 4
  - ArticuloInsumo: Cerveza Quilmes 473ml - Stock: 50
  - ArticuloInsumo: Masa fresca para pizza mediana - Stock: 30
  - ArticuloManufacturado: Pizza Especial - Tiempo: 25min
  - ArticuloManufacturado: Combo Pizza + Bebida - Tiempo: 30min
```

### HU-010: Actualizar Stock de Insumos
**Como** encargado de almacén
**Quiero** actualizar las cantidades en stock
**Para** mantener el inventario actualizado

**Criterios de Aceptación:**
- Debo poder modificar stock actual
- Debo poder ajustar stock máximo
- Los cambios deben reflejarse inmediatamente
- Debo recibir confirmación de la actualización

### HU-011: Buscar Productos por Precio
**Como** gerente de ventas
**Quiero** buscar productos por rango de precios
**Para** analizar la estructura de precios del menú

**Criterios de Aceptación:**
- Debo poder buscar por precio exacto
- Debo ver todos los productos que coincidan
- Debo ver tanto insumos como manufacturados
- Los resultados deben mostrar precio y tipo de producto

**Ejemplo de Uso:**
```java
List<Articulo> articulosPrecio = articuloRepo.findByField("precioVenta", 150.0);
```

**Resultado Esperado:**
```
✓ Artículos con precio de venta $150: 1
  - ArticuloInsumo: Cerveza Quilmes 473ml - Stock: 50
```

---

## 🏷️ Gestión de Categorías

### HU-012: Crear Categoría Principal
**Como** gerente de menú
**Quiero** crear una categoría principal de productos
**Para** organizar el catálogo de manera jerárquica

**Criterios de Aceptación:**
- Debo poder ingresar nombre y denominación
- La categoría debe quedar sin categoría padre (es principal)
- Debo poder asignar la categoría a una sucursal
- La categoría debe estar disponible para asignar productos

**Ejemplo de Implementación:**
```java
Categoria principales = Categoria.builder()
    .nombre("Platos Principales")
    .denominacion("Platos Principales")
    .build();

sucursal.addCategoria(principales); // Asignación automática
```

### HU-013: Crear Subcategoría
**Como** gerente de menú
**Quiero** crear subcategorías dentro de categorías existentes
**Para** tener una organización más detallada

**Criterios de Aceptación:**
- Debo poder seleccionar una categoría padre existente
- La subcategoría debe quedar anidada bajo la categoría padre
- Debo poder ver la jerarquía completa
- Las subcategorías pueden tener productos asignados

**Ejemplo de Implementación:**
```java
Categoria pizzas = Categoria.builder()
    .nombre("Pizzas Especiales")
    .denominacion("Pizzas Gourmet y Especiales")
    .build();

categoriaPrincipal.addSubcategoria(pizzas); // Relación jerárquica
```

### HU-014: Asignar Productos a Categoría
**Como** gerente de menú
**Quiero** asignar productos a categorías específicas
**Para** que los clientes puedan encontrarlos fácilmente

**Criterios de Aceptación:**
- Debo poder seleccionar productos manufacturados existentes
- Debo poder asignarlos a una o múltiples categorías
- Los productos deben aparecer en todas las categorías asignadas
- Debo poder ver el listado de productos por categoría

### HU-015: Consultar Productos por Categoría
**Como** cliente o mesero
**Quiero** ver todos los productos de una categoría
**Para** conocer las opciones disponibles

**Criterios de Aceptación:**
- Debo ver el nombre y descripción de la categoría
- Debo ver todos los productos asignados
- Debo ver subcategorías si existen
- Debo ver información básica de cada producto (nombre, precio, tiempo)

**Resultado Esperado:**
```
Categoría: Categoria: Platos Principales - 2 artículos
  - ArticuloManufacturado: Pizza Especial - Tiempo: 25min
  - ArticuloManufacturado: Combo Pizza + Bebida - Tiempo: 30min
  Subcategoría: Categoria: Bebidas Frías y Calientes - 0 artículos
  Subcategoría: Categoria: Pizzas Gourmet y Especiales - 2 artículos
```

---

## 🎯 Gestión de Promociones

### HU-016: Crear Promoción Happy Hour
**Como** gerente de marketing
**Quiero** crear una promoción tipo Happy Hour
**Para** aumentar las ventas en horarios específicos

**Criterios de Aceptación:**
- Debo poder definir nombre y descripción de la promoción
- Debo poder establecer fechas de vigencia (desde/hasta)
- Debo poder definir horarios específicos (hora desde/hasta)
- Debo poder establecer tipo de promoción (HAPPYHOUR)
- Debo poder definir precio promocional y descuento
- La promoción debe validar automáticamente vigencia

**Ejemplo de Implementación:**
```java
Promocion happyHour = Promocion.builder()
    .nombre("Happy Hour")
    .denominacion("2x1 en bebidas seleccionadas")
    .fechaDesde(LocalDate.now())
    .fechaHasta(LocalDate.now().plusDays(30))
    .horaDesde(LocalTime.of(17, 0))
    .horaHasta(LocalTime.of(20, 0))
    .tipoPromocion(TipoPromocion.HAPPYHOUR)
    .precioDescuento(200.0)
    .precioPromocional(750.0)
    .build();
```

**Resultado Esperado:**
```
Promocion guardado con ID: 2
• Promoción: 2x1 en bebidas seleccionadas - HAPPYHOUR - $750.0
  Período: 2025-09-26 al 2025-10-26
  Horario: 17:00 - 20:00
  Descuento: $200.0
```

### HU-017: Crear Promoción Estacional
**Como** gerente de marketing
**Quiero** crear promociones estacionales
**Para** aprovechar fechas especiales y estaciones del año

**Criterios de Aceptación:**
- Debo poder crear promoción tipo PROMOCION1
- Debo poder establecer período extendido de vigencia
- Debo poder definir horarios de aplicación
- La promoción debe aplicar automáticamente en el período definido

### HU-018: Asignar Productos a Promoción
**Como** gerente de marketing
**Quiero** seleccionar qué productos participan en la promoción
**Para** controlar exactamente qué se incluye en cada oferta

**Criterios de Aceptación:**
- Debo poder seleccionar productos manufacturados específicos
- Los productos seleccionados deben mostrar el precio promocional
- Debo poder agregar o quitar productos de promociones activas
- Los cambios deben ser inmediatos

### HU-019: Consultar Promociones Activas
**Como** mesero o cliente
**Quiero** ver las promociones vigentes
**Para** conocer las ofertas disponibles

**Criterios de Aceptación:**
- Debo ver solo promociones vigentes por fecha y hora actual
- Debo ver detalles de descuento y precio promocional
- Debo ver qué productos están incluidos
- Debo ver horarios de aplicación de cada promoción

### HU-020: Analizar Promociones por Tipo
**Como** gerente de marketing
**Quiero** analizar las promociones agrupadas por tipo
**Para** evaluar qué tipos de promociones son más efectivas

**Criterios de Aceptación:**
- Debo ver promociones agrupadas por tipo (HAPPYHOUR, PROMOCION1)
- Debo ver cantidad de promociones por tipo
- Debo ver nombres de promociones en cada grupo
- Los datos deben actualizarse automáticamente

**Resultado Esperado:**
```
PROMOCIONES POR TIPO:
• PROMOCION1: 2 promociones
  - Descuento especial de otoño
• HAPPYHOUR: 2 promociones
  - 2x1 en bebidas seleccionadas
```

---

## 👥 Gestión de Clientes

### HU-021: Registrar Nuevo Cliente
**Como** recepcionista o mesero
**Quiero** registrar un nuevo cliente en el sistema
**Para** poder gestionar sus pedidos y datos de contacto

**Criterios de Aceptación:**
- Debo poder ingresar nombre y apellido
- Debo poder ingresar teléfono y email (único)
- Debo poder registrar fecha de nacimiento
- Debo poder asignar un usuario del sistema existente
- Debo poder asignar una imagen de perfil existente
- El email debe ser único en el sistema

**Ejemplo de Implementación:**
```java
Cliente nuevoCliente = Cliente.builder()
    .nombre("David")
    .apellido("López")
    .telefono("2616649039")
    .email("david.lopez@email.com")
    .fechaNacimiento(LocalDate.of(1990, 5, 15))
    .usuario(usuarioExistente)
    .imagen(imagenExistente)
    .build();
```

**Resultado Esperado:**
```
Cliente guardado con ID: 1
Cliente: David López - david.lopez@email.com
```

### HU-022: Asignar Domicilios a Cliente
**Como** recepcionista
**Quiero** asignar direcciones de entrega a un cliente
**Para** poder procesar pedidos con delivery

**Criterios de Aceptación:**
- Debo poder seleccionar domicilios existentes del sistema
- Un cliente puede tener múltiples domicilios
- Debo poder reutilizar domicilios entre clientes
- Las direcciones deben mostrarse al procesar pedidos

**Ejemplo de Implementación:**
```java
cliente.addDomicilio(domicilioExistente); // Relación bidireccional
```

### HU-023: Consultar Información de Cliente
**Como** mesero o gerente
**Quiero** consultar la información completa de un cliente
**Para** brindar un servicio personalizado

**Criterios de Aceptación:**
- Debo ver datos personales completos
- Debo ver historial de domicilios asociados
- Debo ver información del usuario del sistema
- Debo poder acceder a esta información por ID o email

### HU-024: Actualizar Datos de Cliente
**Como** recepcionista
**Quiero** actualizar los datos de un cliente existente
**Para** mantener la información de contacto actualizada

**Criterios de Aceptación:**
- Debo poder modificar teléfono y email
- Debo poder cambiar fecha de nacimiento si fue mal ingresada
- Los cambios deben persistir inmediatamente
- El email modificado debe seguir siendo único

### HU-025: Buscar Clientes por Criterios
**Como** gerente de atención al cliente
**Quiero** buscar clientes por diferentes criterios
**Para** encontrar rápidamente información específica

**Criterios de Aceptación:**
- Debo poder buscar por nombre exacto
- Debo poder buscar por email
- Debo poder buscar por teléfono
- Los resultados deben mostrar información básica de contacto

---

## 🛒 Gestión de Pedidos

### HU-026: Crear Nuevo Pedido
**Como** mesero
**Quiero** crear un nuevo pedido para un cliente
**Para** procesar su orden de compra

**Criterios de Aceptación:**
- Debo poder seleccionar un cliente existente
- Debo poder seleccionar la sucursal que procesa el pedido
- Debo poder elegir domicilio de entrega (para delivery)
- Debo poder establecer tipo de envío (DELIVERY/TAKEAWAY)
- Debo poder definir forma de pago (EFECTIVO/MERCADOPAGO)
- El pedido debe iniciarse en estado PENDIENTE
- Debe registrarse fecha y hora automáticamente

**Ejemplo de Implementación:**
```java
Pedido nuevoPedido = Pedido.builder()
    .cliente(clienteExistente)
    .sucursal(sucursalActual)
    .domicilio(domicilioEntrega)
    .fechaPedido(LocalDate.now())
    .estado(Estado.PENDIENTE)
    .tipoEnvio(TipoEnvio.DELIVERY)
    .formaPago(FormaPago.MERCADOPAGO)
    .build();
```

### HU-027: Agregar Productos al Pedido
**Como** mesero
**Quiero** agregar productos con cantidades al pedido
**Para** completar la orden del cliente

**Criterios de Aceptación:**
- Debo poder seleccionar productos manufacturados disponibles
- Debo poder especificar cantidad de cada producto
- El sistema debe calcular subtotal automáticamente
- Debo poder agregar múltiples productos al mismo pedido
- Cada línea de pedido debe tener nombre, cantidad y subtotal

**Ejemplo de Implementación:**
```java
DetallePedido detalle = DetallePedido.builder()
    .articulo(pizzaEspecial)
    .cantidad(2)
    .subTotal(1700.0) // precio * cantidad
    .build();

pedido.addDetalle(detalle); // Relación bidireccional
```

### HU-028: Calcular Total del Pedido
**Como** sistema
**Quiero** calcular automáticamente el total del pedido
**Para** mostrar el monto final al cliente

**Criterios de Aceptación:**
- El total debe ser la suma de todos los subtotales
- Debe aplicar promociones vigentes automáticamente
- Debe calcular tiempo estimado total de preparación
- Debe mostrar desglose de productos y precios

### HU-029: Procesar Pago del Pedido
**Como** cajero
**Quiero** procesar el pago del pedido
**Para** completar la transacción

**Criterios de Aceptación:**
- Debo poder confirmar forma de pago seleccionada
- Debo poder generar factura asociada
- El pedido debe cambiar a estado PREPARACION
- Debe registrarse hora estimada de finalización
- Debe enviarse orden a cocina

### HU-030: Consultar Estado del Pedido
**Como** cliente o mesero
**Quiero** consultar el estado actual del pedido
**Para** conocer el progreso de la preparación

**Criterios de Aceptación:**
- Debo ver estado actual (PENDIENTE, PREPARACION, LISTO, ENTREGADO)
- Debo ver tiempo estimado de finalización
- Debo ver productos incluidos y cantidades
- Debo ver información de entrega (domicilio, tipo)

### HU-031: Actualizar Estado del Pedido
**Como** cocinero o delivery
**Quiero** actualizar el estado del pedido
**Para** informar el progreso al cliente

**Criterios de Aceptación:**
- Debo poder cambiar estado secuencialmente
- Cocinero: PENDIENTE → PREPARACION → LISTO
- Delivery: LISTO → ENTREGADO
- Cada cambio debe registrar timestamp
- Cliente debe ser notificado del cambio

---

## 🌍 Gestión Geográfica

### HU-032: Registrar Nueva Ubicación Geográfica
**Como** administrador del sistema
**Quiero** registrar la estructura geográfica completa
**Para** poder asignar direcciones precisas a sucursales y clientes

**Criterios de Aceptación:**
- Debo poder registrar países con nombre único
- Debo poder registrar provincias asociadas a países
- Debo poder registrar localidades asociadas a provincias
- Debo poder crear domicilios con dirección completa
- La jerarquía geográfica debe mantenerse consistente

**Ejemplo de Implementación:**
```java
// Estructura jerárquica completa
Pais argentina = Pais.builder().nombre("Argentina").build();
Provincia mendoza = Provincia.builder()
    .nombre("Mendoza")
    .pais(argentina)
    .build();
Localidad maipu = Localidad.builder()
    .nombre("Maipú")
    .provincia(mendoza)
    .build();
Domicilio direccion = Domicilio.builder()
    .nombre("San Martín")
    .numero(1000)
    .cp(5501)
    .localidad(maipu)
    .build();
```

### HU-033: Consultar Estructura Geográfica
**Como** usuario del sistema
**Quiero** navegar por la estructura geográfica
**Para** seleccionar ubicaciones precisas

**Criterios de Aceptación:**
- Debo poder ver países disponibles
- Al seleccionar país, debo ver sus provincias
- Al seleccionar provincia, debo ver sus localidades
- Al seleccionar localidad, debo ver domicilios disponibles
- Debo poder ver la ruta completa (País > Provincia > Localidad)

### HU-034: Reutilizar Domicilios
**Como** administrador
**Quiero** reutilizar domicilios entre diferentes entidades
**Para** optimizar el almacenamiento y mantener consistencia

**Criterios de Aceptación:**
- Un domicilio puede ser usado por múltiples clientes
- Un domicilio puede ser sede de una sucursal
- Los cambios en el domicilio deben reflejarse en todas las referencias
- Debo poder ver qué entidades usan cada domicilio

### HU-035: Analizar Distribución Geográfica
**Como** gerente corporativo
**Quiero** analizar la distribución geográfica de sucursales
**Para** evaluar cobertura y planificar expansión

**Criterios de Aceptación:**
- Debo ver listado de sucursales por localidad
- Debo ver la información geográfica completa de cada sucursal
- Debo poder identificar áreas sin cobertura
- Los datos deben actualizarse automáticamente

**Resultado Esperado:**
```
ANALISIS GEOGRAFICO:
• Sucursal Godoy Cruz ubicada en Localidad: Godoy Cruz - Mendoza
• Casa Matriz Centro ubicada en Localidad: Maipú - Mendoza
```

---

## 📊 Reportes y Análisis

### HU-036: Generar Reporte de Ventas por Producto
**Como** gerente de ventas
**Quiero** ver el análisis de productos más vendidos
**Para** optimizar el menú y la oferta

**Criterios de Aceptación:**
- Debo ver listado de todos los productos con precios
- Debo ver productos agrupados por categoría
- Debo poder identificar productos premium vs económicos
- El reporte debe actualizarse con cada cambio de precios

**Resultado Esperado:**
```
ANALISIS DE PRODUCTOS:
• Combo completo pizza mediana + bebida - $950.0
• Pizza Especial con ingredientes premium - $850.0
```

### HU-037: Analizar Efectividad de Promociones
**Como** gerente de marketing
**Quiero** analizar el impacto de las promociones
**Para** optimizar las estrategias promocionales

**Criterios de Aceptación:**
- Debo ver promociones agrupadas por tipo
- Debo ver cantidad de promociones activas por categoría
- Debo ver descuentos totales otorgados
- Debo poder comparar efectividad entre tipos

### HU-038: Reporte de Consistencia del Sistema
**Como** administrador técnico
**Quiero** verificar la consistencia de datos
**Para** asegurar la integridad del sistema

**Criterios de Aceptación:**
- Debo ver conteo total de cada tipo de entidad
- Debo verificar que las relaciones estén correctas
- Debo poder detectar inconsistencias en los datos
- El reporte debe ejecutarse bajo demanda

**Resultado Esperado:**
```
PRUEBA: Consistencia de Repositorios
• Total empresas: 1
• Total sucursales: 2
• Total usuarios: 2
• Total clientes: 2
• Total artículos: 5
• Total categorías: 3
• Total promociones: 2
```

### HU-039: Dashboard Ejecutivo
**Como** gerente general
**Quiero** ver un resumen ejecutivo del estado del sistema
**Para** tomar decisiones informadas

**Criterios de Aceptación:**
- Debo ver métricas clave de cada área de negocio
- Debo ver estado actual de inventario
- Debo ver performance de sucursales
- Debo ver tendencias de clientes y pedidos
- La información debe ser actualizada y precisa

---

## 🔧 Administración del Sistema

### HU-040: Realizar Operaciones CRUD Completas
**Como** administrador del sistema
**Quiero** poder crear, leer, actualizar y eliminar cualquier entidad
**Para** mantener el sistema actualizado y corregir errores

**Criterios de Aceptación:**
- Debo poder crear nuevas entidades de cualquier tipo
- Debo poder buscar entidades por ID y por campos específicos
- Debo poder actualizar datos de entidades existentes
- Debo poder eliminar entidades (eliminación lógica)
- Todas las operaciones deben ser transaccionales y seguras

**Ejemplo de Flujo CRUD:**
```java
// CREATE
Usuario nuevo = Usuario.builder()
    .nombre("Usuario Prueba")
    .auth0Id("999")
    .username("UsuarioPrueba")
    .build();
Usuario guardado = usuarioRepo.save(nuevo);

// READ
Optional<Usuario> encontrado = usuarioRepo.findById(guardado.getId());

// UPDATE
guardado.setNombre("Usuario Actualizado");
Optional<Usuario> actualizado = usuarioRepo.update(guardado);

// DELETE
Optional<Usuario> eliminado = usuarioRepo.deleteById(guardado.getId());
```

### HU-041: Mantener Relaciones Bidireccionales
**Como** sistema
**Quiero** mantener automáticamente las relaciones bidireccionales
**Para** asegurar la consistencia de datos

**Criterios de Aceptación:**
- Al agregar una entidad a una colección, la relación inversa debe establecerse automáticamente
- Al remover una entidad, ambos lados de la relación deben actualizarse
- Las operaciones deben ser atómicas
- No debe ser posible crear estados inconsistentes

**Ejemplo de Verificación:**
```java
// Agregar sucursal a empresa
empresa.addSucursal(nuevaSucursal);
assert nuevaSucursal.getEmpresa() == empresa; // Verificación automática

// Remover sucursal
empresa.removeSucursal(nuevaSucursal);
assert nuevaSucursal.getEmpresa() == null; // Verificación automática
```

### HU-042: Gestionar Transacciones del Sistema
**Como** sistema
**Quiero** manejar automáticamente las transacciones de base de datos
**Para** garantizar consistencia y recuperación ante errores

**Criterios de Aceptación:**
- Todas las operaciones deben ejecutarse dentro de transacciones
- En caso de error, debe ejecutarse rollback automático
- El sistema debe registrar operaciones exitosas
- Debe informar claramente errores y sus causas
- Las transacciones deben seguir propiedades ACID

### HU-043: Validar Integridad de Datos
**Como** sistema
**Quiero** validar la integridad de datos en cada operación
**Para** prevenir corrupción y estados inválidos

**Criterios de Aceptación:**
- Debo validar que referencias a entidades existan
- Debo prevenir eliminación de entidades referenciadas
- Debo validar unicidad de campos marcados como únicos
- Debo validar formatos de datos (emails, teléfonos)
- Debo proporcionar mensajes de error claros

### HU-044: Ejecutar Demostraciones del Sistema
**Como** usuario de demostración
**Quiero** ejecutar una demostración completa del sistema
**Para** ver todas las funcionalidades en acción

**Criterios de Aceptación:**
- El sistema debe inicializarse con datos de ejemplo
- Debe ejecutar automáticamente operaciones CRUD
- Debe mostrar consultas y análisis de datos
- Debe verificar funcionamiento de relaciones bidireccionales
- Debe proporcionar salida clara y formateada
- La demostración debe completarse sin errores

**Flujo de Demostración:**
```
1. Inicialización → Crear estructura completa de datos
2. Visualización → Mostrar información de empresa y sucursales
3. CRUD → Demostrar operaciones en usuarios y artículos
4. Análisis → Ejecutar consultas complejas y reportes
5. Pruebas → Verificar funcionalidades avanzadas
6. Finalización → Limpiar recursos y cerrar sistema
```

---

## 📝 Criterios de Aceptación Generales

### Rendimiento
- Las operaciones CRUD deben ejecutarse en menos de 100ms
- La inicialización completa debe tomar menos de 5 segundos
- Las consultas complejas deben responder en menos de 200ms

### Usabilidad
- Todos los mensajes deben ser claros y en español
- Los errores deben incluir información específica del problema
- La salida debe estar formateada de manera legible

### Confiabilidad
- El sistema debe recuperarse automáticamente de errores transaccionales
- Los datos deben persistir entre reinicios de la aplicación
- No debe ser posible crear estados de datos inconsistentes

### Mantenibilidad
- El código debe seguir patrones de diseño consistentes
- Las entidades deben tener métodos getInfo() descriptivos
- Las relaciones bidireccionales deben mantenerse automáticamente

---

## 🎯 Notas de Implementación

### Patrones Utilizados
- **Repository Pattern**: Para abstracción de acceso a datos
- **Builder Pattern**: Para construcción fluida de entidades complejas
- **Template Method**: En clase Base para comportamiento común
- **Composition**: Para relaciones entre entidades

### Tecnologías
- **JPA/Hibernate**: Para persistencia y ORM
- **H2 Database**: Base de datos embebida con persistencia en archivo
- **Lombok**: Para reducción de código boilerplate
- **Java Streams**: Para consultas complejas y análisis de datos

### Consideraciones Especiales
- Todas las entidades extienden de Base con campos comunes
- Se utiliza eliminación lógica (flag eliminado) en lugar de física
- Las relaciones bidireccionales se mantienen automáticamente
- El sistema es transaccional con rollback automático en caso de errores

Este documento de historias de usuario proporciona una guía completa para entender, usar y extender el Sistema de Gestión de Restaurante, cubriendo todos los aspectos funcionales desde la perspectiva del usuario final.