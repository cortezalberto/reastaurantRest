# Frontend Dashboard - Sistema de Gestión de Restaurante

## Descripción General

Dashboard administrativo para gestionar las funcionalidades internas del Sistema de Gestión de Restaurante TechFood Solutions. Permite administrar empresas, sucursales, artículos, clientes y todas las entidades relacionadas del sistema.

**URL Base del Backend:** `http://localhost:8080`

## 🎯 Objetivos del Dashboard

- Administrar empresas y sus sucursales con domicilios asociados
- Gestionar catálogo de artículos (insumos y manufacturados)
- Configurar unidades de medida e imágenes
- Administrar clientes y sus datos
- Visualizar relaciones entre entidades
- Proporcionar interfaz intuitiva para operaciones CRUD

## 📋 Módulos Principales - URLs Específicas del Backend

### 1. 🏢 Gestión de Empresas y Sucursales

#### 1.1 Módulo Empresas
**Funcionalidades:**
- ✅ **Crear Empresa**: Formulario con nombre, razón social, CUIL
- ✅ **Listar Empresas**: Tabla con búsqueda y filtros
- ✅ **Editar Empresa**: Modificar datos existentes
- ✅ **Eliminar Empresa**: Soft delete con confirmación
- ✅ **Ver Detalles**: Vista completa con sucursales asociadas
- ✅ **Búsqueda Avanzada**: Por nombre y razón social

**Campos del Formulario:**
```json
{
  "nombre": "string (requerido)",
  "razonSocial": "string (requerido)",
  "cuil": "number (11 dígitos, Long)"
}
```

**🔗 API Endpoints del Backend:**
```typescript
// CRUD Básico
GET    http://localhost:8080/api/v1/empresas                    // Listar todas las empresas activas
POST   http://localhost:8080/api/v1/empresas                    // Crear nueva empresa
GET    http://localhost:8080/api/v1/empresas/{id}              // Obtener empresa por ID
PUT    http://localhost:8080/api/v1/empresas/{id}              // Actualizar empresa
DELETE http://localhost:8080/api/v1/empresas/{id}              // Eliminar empresa (soft delete)

// Búsquedas Específicas
GET    http://localhost:8080/api/v1/empresas/buscar-por-nombre?nombre={nombre}           // Buscar por nombre exacto
GET    http://localhost:8080/api/v1/empresas/buscar-por-razon-social?razonSocial={text} // Buscar por razón social
```

**📝 Ejemplos de Request/Response:**
```typescript
// POST /api/v1/empresas (Crear)
{
  "nombre": "TechFood Solutions",
  "razonSocial": "TechFood Solutions S.A.",
  "cuil": 20356206360
}

// Response 201 Created
{
  "id": 1,
  "nombre": "TechFood Solutions",
  "razonSocial": "TechFood Solutions S.A.",
  "cuil": 20356206360,
  "eliminado": false,
  "sucursales": []
}
```

#### 1.2 Módulo Sucursales
**Funcionalidades:**
- ✅ **Crear Sucursal**: Asociada a una empresa y domicilio
- ✅ **Listar Sucursales**: Por empresa o todas
- ✅ **Editar Sucursal**: Modificar horarios y domicilio
- ✅ **Gestionar Horarios**: Apertura y cierre con validación automática
- ✅ **Asignar Domicilio**: Selección de domicilios existentes
- ✅ **Estado en Tiempo Real**: Verificación si están abiertas ahora

**Campos del Formulario:**
```json
{
  "nombre": "string (requerido)",
  "empresaId": "number (requerido)",
  "domicilioId": "number (requerido)",
  "horarioApertura": "time (HH:mm)",
  "horarioCierre": "time (HH:mm)"
}
```

**🔗 API Endpoints del Backend:**
```typescript
// CRUD Básico - Solo lectura (las sucursales se crean desde empresas)
GET    http://localhost:8080/api/v1/sucursales                           // Listar todas las sucursales activas
GET    http://localhost:8080/api/v1/sucursales/{id}                     // Obtener sucursal por ID con domicilio completo

// Búsquedas Específicas
GET    http://localhost:8080/api/v1/sucursales/buscar-por-nombre?nombre={nombre}         // Buscar por nombre
GET    http://localhost:8080/api/v1/sucursales/buscar-por-empresa?empresaId={empresaId}  // Sucursales de una empresa
GET    http://localhost:8080/api/v1/sucursales/abiertas                                  // Solo sucursales abiertas ahora
```

**📝 Ejemplo de Response:**
```typescript
// GET /api/v1/sucursales/{id}
{
  "id": 1,
  "nombre": "Casa Matriz Centro",
  "horarioApertura": "11:00",
  "horarioCierre": "23:00",
  "eliminado": false,
  "empresa": "TechFood Solutions",
  "domicilio": {
    "id": 1,
    "nombre": "San Martín",
    "numero": 1000,
    "cp": 5501,
    "localidad": "Maipú",
    "provincia": "Mendoza",
    "pais": "Argentina"
  },
  "cantidadCategorias": 3,
  "cantidadPromociones": 2,
  "abierta": true  // Calculado en tiempo real
}
```

### 2. Gestión Geográfica y Domicilios

#### 2.1 Módulo Domicilios
**Funcionalidades:**
- ✅ **Crear Domicilio**: Con jerarquía país → provincia → localidad
- ✅ **Listar Domicilios**: Con filtros por localidad
- ✅ **Editar Domicilio**: Modificar dirección y código postal
- ✅ **Asociar a Entidades**: Empresas, sucursales, clientes

**Jerarquía Geográfica:**
```
País (Argentina)
└── Provincia (Mendoza)
    └── Localidad (Maipú, Godoy Cruz)
        └── Domicilio (Calle + Número + CP)
```

**Campos del Formulario:**
```
- País (preseleccionado: Argentina)
- Provincia (selección)
- Localidad (selección dependiente)
- Calle/Avenida (requerido)
- Número (requerido)
- Código Postal (requerido)
- Piso/Departamento (opcional)
```

### 3. Gestión de Artículos y Catálogo

#### 3.1 Módulo Artículos Insumos
**Funcionalidades:**
- ✅ **Crear Insumo**: Ingredientes y materias primas
- ✅ **Gestionar Stock**: Stock actual, máximo, mínimo
- ✅ **Configurar Precios**: Compra y venta
- ✅ **Asociar Imágenes**: Múltiples imágenes por artículo
- ✅ **Unidad de Medida**: Kg, L, unidades, etc.

**Campos del Formulario:**
```
- Nombre del Insumo (requerido)
- Denominación (descripción corta)
- Precio de Compra (decimal)
- Precio de Venta (decimal)
- Stock Actual (entero)
- Stock Máximo (entero)
- Stock Mínimo (entero)
- Unidad de Medida (selección)
- Es para Elaborar (checkbox)
- Imágenes (múltiples archivos)
```

#### 3.2 Módulo Artículos Manufacturados
**Funcionalidades:**
- ✅ **Crear Producto**: Productos finales elaborados
- ✅ **Definir Receta**: Asociar insumos necesarios
- ✅ **Tiempo de Preparación**: Estimación en minutos
- ✅ **Instrucciones**: Pasos de preparación
- ✅ **Costos**: Cálculo automático basado en insumos

**Campos del Formulario:**
```
- Nombre del Producto (requerido)
- Denominación (descripción corta)
- Descripción Completa (texto largo)
- Tiempo Estimado (minutos)
- Instrucciones de Preparación (texto)
- Precio de Venta (decimal)
- Unidad de Medida (selección)
- Imágenes (múltiples archivos)
```

**Subformulario - Receta (Detalles):**
```
- Insumo (selección de artículos insumo)
- Cantidad Necesaria (decimal)
- Unidad (hereda del insumo)
- Costo por Unidad (automático)
- Subtotal (automático: cantidad × costo)
```

#### 3.3 Módulo Unidades de Medida
**Funcionalidades:**
- ✅ **Crear Unidad**: Kg, L, unidades, porciones, etc.
- ✅ **Listar Unidades**: Con denominación y símbolo
- ✅ **Editar Unidad**: Modificar nombre y denominación
- ✅ **Asociar a Artículos**: Selección en formularios

**Campos del Formulario:**
```
- Nombre (requerido): "Kilogramo"
- Denominación (requerido): "Kg"
- Tipo (opcional): peso, volumen, cantidad
```

#### 3.4 Módulo Imágenes
**Funcionalidades:**
- ✅ **Subir Imágenes**: Drag & drop o selección
- ✅ **Previsualización**: Thumbnails y vista previa
- ✅ **Gestionar Galería**: Organización por entidad
- ✅ **Asociar Múltiples**: Una imagen para varias entidades

**Características:**
```
- Formatos soportados: JPG, PNG, WEBP
- Tamaño máximo: 5MB por imagen
- Resolución recomendada: 1200x800px
- Compresión automática
- Nombres únicos para evitar conflictos
```

### 4. Gestión de Clientes

#### 4.1 Módulo Clientes
**Funcionalidades:**
- ✅ **Crear Cliente**: Datos personales completos
- ✅ **Listar Clientes**: Con búsqueda por nombre/email
- ✅ **Editar Cliente**: Modificar información personal
- ✅ **Gestionar Domicilios**: Múltiples domicilios por cliente
- ✅ **Asociar Usuario**: Vinculación con sistema de usuarios

**Campos del Formulario:**
```
- Nombre (requerido)
- Apellido (requerido)
- Email (requerido, único)
- Teléfono (formato: 261-XXXXXXX)
- Fecha de Nacimiento (date picker)
- Usuario Asociado (selección)
- Imagen de Perfil (opcional)
- Domicilios (selección múltiple)
```

**API Endpoints:**
- `GET /api/v1/clientes` - Listar todos
- `POST /api/v1/clientes` - Crear nuevo
- `GET /api/v1/clientes/{id}` - Obtener por ID
- `PUT /api/v1/clientes/{id}` - Actualizar
- `DELETE /api/v1/clientes/{id}` - Eliminar

### 5. Gestión de Categorías y Promociones

#### 5.1 Módulo Categorías
**Funcionalidades:**
- ✅ **Crear Categoría**: Jerarquía de categorías
- ✅ **Gestionar Jerarquía**: Categorías padre e hijas
- ✅ **Asociar Artículos**: Productos por categoría
- ✅ **Organizar Menú**: Estructura para frontend público

**Campos del Formulario:**
```
- Nombre de la Categoría (requerido)
- Denominación (descripción)
- Categoría Padre (selección, opcional)
- Artículos Asociados (selección múltiple)
- Orden de Visualización (entero)
- Activa (checkbox)
```

#### 5.2 Módulo Promociones
**Funcionalidades:**
- ✅ **Crear Promoción**: Ofertas y descuentos
- ✅ **Configurar Vigencia**: Fechas y horarios
- ✅ **Asociar Artículos**: Productos en promoción
- ✅ **Calcular Descuentos**: Precio promocional automático

**Campos del Formulario:**
```
- Nombre de la Promoción (requerido)
- Denominación (descripción corta)
- Tipo de Promoción (PROMOCION1, HAPPYHOUR, etc.)
- Fecha Desde (date picker)
- Fecha Hasta (date picker)
- Hora Desde (time picker)
- Hora Hasta (time picker)
- Precio de Descuento (decimal)
- Precio Promocional (decimal)
- Artículos Incluidos (selección múltiple)
- Imagen Promocional (archivo)
```

## 🎨 Diseño de Interfaz

### Layout Principal
```
┌─────────────────────────────────────────────────────────┐
│ Header: Logo + Usuario + Notificaciones                │
├─────────────────────────────────────────────────────────┤
│ Sidebar: Menú de Navegación                            │
├─────────────────────────────────────────────────────────┤
│ Main Content: Dashboard / Formularios / Tablas         │
├─────────────────────────────────────────────────────────┤
│ Footer: Estado del Sistema + Versión                   │
└─────────────────────────────────────────────────────────┘
```

### Estructura de Navegación
```
📊 Dashboard Principal
├── 🏢 Gestión de Empresas
│   ├── Empresas
│   └── Sucursales
├── 🏠 Gestión Geográfica
│   ├── Países/Provincias/Localidades
│   └── Domicilios
├── 📦 Gestión de Artículos
│   ├── Artículos Insumo
│   ├── Artículos Manufacturados
│   ├── Unidades de Medida
│   └── Galería de Imágenes
├── 👥 Gestión de Clientes
│   └── Clientes
├── 🏷️ Catálogo
│   ├── Categorías
│   └── Promociones
└── ⚙️ Configuración
    ├── Usuarios
    └── Sistema
```

### Componentes de UI Requeridos

#### 1. Tablas de Datos
- **Paginación**: 10, 25, 50, 100 registros por página
- **Búsqueda**: Campo de búsqueda global
- **Filtros**: Por estado, fecha, categoría, etc.
- **Ordenamiento**: Por cualquier columna
- **Acciones**: Ver, Editar, Eliminar por fila
- **Selección Múltiple**: Para acciones en lote

#### 2. Formularios
- **Validación en Tiempo Real**: Mensajes de error inmediatos
- **Campos Dependientes**: Provincia → Localidad
- **Selección Múltiple**: Domicilios, artículos, imágenes
- **Drag & Drop**: Para imágenes
- **Autocompletado**: Para búsquedas rápidas
- **Guardado Automático**: En formularios largos

#### 3. Componentes Especializados
- **Selector de Fecha/Hora**: Para promociones y horarios
- **Editor de Recetas**: Para artículos manufacturados
- **Galería de Imágenes**: Con previsualización
- **Calculadora de Costos**: Para artículos manufacturados
- **Mapa de Domicilios**: Visualización geográfica (opcional)

## 🔧 Especificaciones Técnicas

### Stack Tecnológico Recomendado

#### Frontend Framework
- **React 18** con TypeScript
- **Next.js 14** para SSR/SSG
- **Tailwind CSS** para styling
- **Headless UI** para componentes base

#### Gestión de Estado
- **Zustand** o **Redux Toolkit** para estado global
- **React Query/TanStack Query** para estado del servidor
- **React Hook Form** para formularios

#### UI Components
- **Radix UI** para componentes primitivos
- **Lucide React** para iconografía
- **React Dropzone** para subida de archivos
- **React Select** para selecciones complejas

#### Utilidades
- **Axios** para peticiones HTTP
- **Date-fns** para manejo de fechas
- **Zod** para validación de esquemas
- **React Hot Toast** para notificaciones

### Estructura del Proyecto Frontend
```
src/
├── components/           # Componentes reutilizables
│   ├── ui/              # Componentes base (Button, Input, etc.)
│   ├── forms/           # Formularios específicos
│   ├── tables/          # Tablas de datos
│   └── layout/          # Componentes de layout
├── pages/               # Páginas de Next.js
│   ├── empresas/        # Gestión de empresas
│   ├── articulos/       # Gestión de artículos
│   ├── clientes/        # Gestión de clientes
│   └── dashboard/       # Dashboard principal
├── hooks/               # Custom hooks
├── services/            # Servicios API
├── types/               # Tipos TypeScript
├── utils/               # Utilidades
└── stores/              # Stores de estado
```

### Configuración de API

#### Base URL y Autenticación
```typescript
const API_BASE_URL = 'http://localhost:8080/api/v1'

// Configuración de Axios
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
})
```

#### Servicios por Módulo
```typescript
// services/empresas.ts
export const empresasService = {
  getAll: () => apiClient.get('/empresas'),
  getById: (id: number) => apiClient.get(`/empresas/${id}`),
  create: (data: CreateEmpresaRequest) => apiClient.post('/empresas', data),
  update: (id: number, data: UpdateEmpresaRequest) => apiClient.put(`/empresas/${id}`, data),
  delete: (id: number) => apiClient.delete(`/empresas/${id}`)
}

// services/articulos.ts
export const articulosService = {
  getAll: () => apiClient.get('/articulos'),
  getByTipo: (tipo: 'INSUMO' | 'MANUFACTURADO') => apiClient.get(`/articulos/buscar-por-tipo?tipo=${tipo}`),
  create: (data: CreateArticuloRequest) => apiClient.post('/articulos', data),
  // ... más métodos
}
```

## 📱 Funcionalidades Adicionales

### Dashboard Principal
- **Métricas Clave**: Total empresas, sucursales, artículos, clientes
- **Gráficos**: Distribución por categorías, stock bajo, ventas
- **Alertas**: Stock bajo, promociones vencidas, tareas pendientes
- **Accesos Rápidos**: Crear nueva empresa, agregar producto, etc.

### Gestión de Archivos
- **Subida Múltiple**: Drag & drop para múltiples imágenes
- **Previsualización**: Thumbnails antes de subir
- **Compresión**: Automática para optimizar tamaño
- **Organización**: Por carpetas (empresas, productos, clientes)

### Exportación e Importación
- **Exportar Datos**: CSV, Excel, PDF para tablas
- **Importar Masivo**: CSV para crear múltiples registros
- **Plantillas**: Descarga de plantillas para importación
- **Validación**: Pre-validación de datos importados

### Búsqueda y Filtros Avanzados
- **Búsqueda Global**: Across todas las entidades
- **Filtros Combinados**: Múltiples criterios simultáneos
- **Guardado de Filtros**: Filtros favoritos del usuario
- **Búsqueda por Relaciones**: Encontrar artículos por proveedor, etc.

## 🔄 Flujos de Trabajo

### Flujo: Crear Nueva Empresa
1. **Navegación**: Dashboard → Empresas → "Nueva Empresa"
2. **Formulario**: Completar datos de la empresa
3. **Validación**: Verificar CUIL único y formato
4. **Confirmación**: Mostrar resumen antes de guardar
5. **Éxito**: Redirección a lista con mensaje de éxito
6. **Siguiente Paso**: Opción de crear sucursal inmediatamente

### Flujo: Crear Artículo Manufacturado
1. **Selección**: Artículos → "Nuevo Manufacturado"
2. **Datos Básicos**: Nombre, descripción, tiempo de preparación
3. **Receta**: Agregar insumos necesarios con cantidades
4. **Cálculo Automático**: Costo total basado en insumos
5. **Imágenes**: Subir fotos del producto
6. **Previsualización**: Vista previa del artículo completo
7. **Guardado**: Confirmar y crear artículo

### Flujo: Gestionar Cliente
1. **Búsqueda**: Localizar cliente existente o crear nuevo
2. **Datos Personales**: Información básica del cliente
3. **Domicilios**: Agregar uno o múltiples domicilios
4. **Verificación**: Validar email único y formato telefónico
5. **Historial**: Ver pedidos anteriores (si existen)
6. **Actualización**: Modificar datos según necesidad

## 🚀 Implementación Recomendada

### Fase 1: Fundamentos (Semanas 1-2)
- ✅ Setup del proyecto Next.js + TypeScript
- ✅ Configuración de Tailwind CSS y componentes base
- ✅ Servicios API para CRUD básico
- ✅ Layout principal y navegación
- ✅ Módulo de empresas completo

### Fase 2: Gestión Básica (Semanas 3-4)
- ✅ Módulo de sucursales y domicilios
- ✅ Gestión de clientes
- ✅ Módulo de unidades de medida
- ✅ Sistema de subida de imágenes

### Fase 3: Catálogo de Productos (Semanas 5-6)
- ✅ Artículos insumo con gestión de stock
- ✅ Artículos manufacturados con recetas
- ✅ Calculadora de costos automática
- ✅ Galería de imágenes integrada

### Fase 4: Características Avanzadas (Semanas 7-8)
- ✅ Categorías y promociones
- ✅ Dashboard con métricas
- ✅ Búsqueda avanzada y filtros
- ✅ Exportación de datos

### Fase 5: Optimización (Semanas 9-10)
- ✅ Optimización de rendimiento
- ✅ Testing automatizado
- ✅ Documentación de usuario
- ✅ Deploy y configuración de producción

## 📊 Métricas y Monitoreo

### KPIs del Dashboard
- **Empresas Activas**: Total de empresas no eliminadas
- **Sucursales por Empresa**: Promedio y distribución
- **Stock Crítico**: Artículos con stock bajo
- **Productos sin Imagen**: Artículos sin fotografías
- **Clientes Registrados**: Total y crecimiento mensual

### Alertas del Sistema
- ⚠️ **Stock Bajo**: Cuando stock actual < stock mínimo
- ⚠️ **Promociones Vencidas**: Promociones que expiraron
- ⚠️ **Datos Incompletos**: Entidades sin información requerida
- ⚠️ **Errores de API**: Fallos de comunicación con backend

## 🔐 Consideraciones de Seguridad

### Validación de Datos
- **Frontend**: Validación inmediata con Zod
- **Backend**: Validación con Bean Validation
- **Sanitización**: Limpieza de inputs peligrosos
- **Escape**: Prevención de XSS en contenido dinámico

### Gestión de Archivos
- **Tipos Permitidos**: Solo imágenes (JPG, PNG, WEBP)
- **Tamaño Máximo**: Límite de 5MB por archivo
- **Escaneo**: Verificación de virus (opcional)
- **Almacenamiento**: Carpetas organizadas y seguras

### API Security
- **Validación**: Todos los endpoints validan entrada
- **Rate Limiting**: Limitación de peticiones por IP
- **CORS**: Configuración correcta para frontend
- **Error Handling**: No exposición de información sensible

## 📋 Lista de Verificación

### Antes del Desarrollo
- [ ] Confirmar API endpoints funcionando
- [ ] Definir esquemas TypeScript basados en DTOs
- [ ] Configurar entorno de desarrollo
- [ ] Establecer convenciones de código

### Durante el Desarrollo
- [ ] Implementar validación en cada formulario
- [ ] Probar CRUD completo para cada entidad
- [ ] Verificar relaciones entre entidades
- [ ] Optimizar consultas y rendimiento

### Antes del Deploy
- [ ] Testing en diferentes navegadores
- [ ] Validar responsive design
- [ ] Verificar manejo de errores
- [ ] Documentar APIs utilizadas
- [ ] Configurar variables de entorno

---

**Nota**: Este documento define los requerimientos completos para el dashboard administrativo. El backend ya está implementado y funcionando según CLAUDE.md, por lo que el frontend puede integrarse directamente con las APIs existentes.