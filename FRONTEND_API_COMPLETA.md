# 🚀 API Completa del Backend - URLs Específicas para Frontend

## 🏗️ URL Base del Sistema
```
BASE_URL = http://localhost:8080
API_BASE = http://localhost:8080/api/v1
```

---

## 📋 ÍNDICE DE MÓDULOS

1. [🏢 Gestión de Empresas](#1--gestión-de-empresas)
2. [🏪 Gestión de Sucursales](#2--gestión-de-sucursales)
3. [📦 Gestión de Artículos](#3--gestión-de-artículos)
4. [👥 Gestión de Clientes](#4--gestión-de-clientes)
5. [🏷️ Gestión de Categorías](#5--gestión-de-categorías)
6. [💰 Gestión de Promociones](#6--gestión-de-promociones)
7. [📊 Gestión de Pedidos](#7--gestión-de-pedidos)
8. [🧾 Gestión de Facturas](#8--gestión-de-facturas)
9. [🏠 Entidades Geográficas](#9--entidades-geográficas)
10. [⚙️ Configuración del Sistema](#10--configuración-del-sistema)

---

## 1. 🏢 Gestión de Empresas

### 📡 **Endpoints Disponibles:**

#### **CRUD Básico**
```typescript
// Listar todas las empresas activas
GET http://localhost:8080/api/v1/empresas
// Response: EmpresaDto[]

// Crear nueva empresa
POST http://localhost:8080/api/v1/empresas
// Body: CreateEmpresaRequest
// Response: EmpresaDto (201 Created)

// Obtener empresa por ID
GET http://localhost:8080/api/v1/empresas/{id}
// Response: EmpresaDto

// Actualizar empresa
PUT http://localhost:8080/api/v1/empresas/{id}
// Body: CreateEmpresaRequest
// Response: EmpresaDto

// Eliminar empresa (soft delete)
DELETE http://localhost:8080/api/v1/empresas/{id}
// Response: 204 No Content
```

#### **Búsquedas Específicas**
```typescript
// Buscar empresa por nombre exacto
GET http://localhost:8080/api/v1/empresas/buscar-por-nombre?nombre={nombre}
// Response: EmpresaDto

// Buscar empresas por razón social (contiene texto)
GET http://localhost:8080/api/v1/empresas/buscar-por-razon-social?razonSocial={text}
// Response: EmpresaDto[]
```

### 📝 **Estructuras de Datos:**

#### **CreateEmpresaRequest**
```typescript
interface CreateEmpresaRequest {
  nombre: string;           // Requerido
  razonSocial: string;      // Requerido
  cuil: number;             // 11 dígitos (Long), requerido
}
```

#### **EmpresaDto (Response)**
```typescript
interface EmpresaDto {
  id: number;
  nombre: string;
  razonSocial: string;
  cuil: number;
  eliminado: boolean;
  sucursales: SucursalDto[];  // Sucursales asociadas
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/empresas.ts
export const empresasService = {
  // Listar todas
  getAll: () => apiClient.get<EmpresaDto[]>('/empresas'),

  // Crear nueva
  create: (data: CreateEmpresaRequest) =>
    apiClient.post<EmpresaDto>('/empresas', data),

  // Obtener por ID
  getById: (id: number) =>
    apiClient.get<EmpresaDto>(`/empresas/${id}`),

  // Actualizar
  update: (id: number, data: CreateEmpresaRequest) =>
    apiClient.put<EmpresaDto>(`/empresas/${id}`, data),

  // Eliminar
  delete: (id: number) =>
    apiClient.delete(`/empresas/${id}`),

  // Búsquedas
  searchByName: (nombre: string) =>
    apiClient.get<EmpresaDto>(`/empresas/buscar-por-nombre?nombre=${nombre}`),

  searchByRazonSocial: (razonSocial: string) =>
    apiClient.get<EmpresaDto[]>(`/empresas/buscar-por-razon-social?razonSocial=${razonSocial}`)
};
```

---

## 2. 🏪 Gestión de Sucursales

### 📡 **Endpoints Disponibles:**

#### **Lectura y Consultas** (Solo lectura desde este controlador)
```typescript
// Listar todas las sucursales activas
GET http://localhost:8080/api/v1/sucursales
// Response: SucursalDto[]

// Obtener sucursal por ID con información completa
GET http://localhost:8080/api/v1/sucursales/{id}
// Response: SucursalDto (con domicilio, empresa, categorías, promociones)

// Obtener solo sucursales abiertas en este momento
GET http://localhost:8080/api/v1/sucursales/abiertas
// Response: SucursalDto[] (con campo 'abierta': true calculado en tiempo real)
```

#### **Búsquedas Específicas**
```typescript
// Buscar sucursales por nombre (contiene texto)
GET http://localhost:8080/api/v1/sucursales/buscar-por-nombre?nombre={nombre}
// Response: SucursalDto[]

// Buscar todas las sucursales de una empresa específica
GET http://localhost:8080/api/v1/sucursales/buscar-por-empresa?empresaId={empresaId}
// Response: SucursalDto[]
```

### 📝 **Estructuras de Datos:**

#### **SucursalDto (Response)**
```typescript
interface SucursalDto {
  id: number;
  nombre: string;
  horarioApertura: string;      // Formato "HH:mm"
  horarioCierre: string;        // Formato "HH:mm"
  eliminado: boolean;
  empresa: string;              // Nombre de la empresa
  domicilio: {
    id: number;
    nombre: string;             // Nombre de la calle
    numero: number;
    cp: number;                 // Código postal
    localidad: string;
    provincia: string;
    pais: string;
  };
  cantidadCategorias: number;   // Categorías asociadas
  cantidadPromociones: number;  // Promociones asociadas
  abierta: boolean;            // Calculado en tiempo real
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/sucursales.ts
export const sucursalesService = {
  // Listar todas
  getAll: () => apiClient.get<SucursalDto[]>('/sucursales'),

  // Obtener por ID (con toda la información)
  getById: (id: number) =>
    apiClient.get<SucursalDto>(`/sucursales/${id}`),

  // Solo sucursales abiertas ahora
  getAbiertas: () =>
    apiClient.get<SucursalDto[]>('/sucursales/abiertas'),

  // Búsquedas
  searchByName: (nombre: string) =>
    apiClient.get<SucursalDto[]>(`/sucursales/buscar-por-nombre?nombre=${nombre}`),

  getByEmpresa: (empresaId: number) =>
    apiClient.get<SucursalDto[]>(`/sucursales/buscar-por-empresa?empresaId=${empresaId}`)
};
```

---

## 3. 📦 Gestión de Artículos

### 📡 **Endpoints Disponibles:**

#### **CRUD Básico**
```typescript
// Listar todos los artículos activos
GET http://localhost:8080/api/v1/articulos
// Response: ArticuloDto[]

// Crear nuevo artículo (insumo o manufacturado)
POST http://localhost:8080/api/v1/articulos
// Body: CreateArticuloRequest
// Response: ArticuloDto (201 Created)

// Obtener artículo por ID
GET http://localhost:8080/api/v1/articulos/{id}
// Response: ArticuloDto

// Actualizar artículo
PUT http://localhost:8080/api/v1/articulos/{id}
// Body: CreateArticuloRequest
// Response: ArticuloDto

// Eliminar artículo (soft delete)
DELETE http://localhost:8080/api/v1/articulos/{id}
// Response: 204 No Content
```

#### **Búsquedas Específicas**
```typescript
// Buscar artículos por nombre (contiene texto)
GET http://localhost:8080/api/v1/articulos/buscar-por-nombre?nombre={nombre}
// Response: ArticuloDto[]

// Buscar artículos por tipo (INSUMO o MANUFACTURADO)
GET http://localhost:8080/api/v1/articulos/buscar-por-tipo?tipo={tipo}
// Parámetros: tipo = "INSUMO" | "MANUFACTURADO"
// Response: ArticuloDto[]
```

### 📝 **Estructuras de Datos:**

#### **CreateArticuloRequest** (Base)
```typescript
interface CreateArticuloRequest {
  nombre: string;                    // Requerido
  denominacion: string;              // Descripción corta
  precioVenta: number;               // Precio decimal
  unidadMedidaId: number;           // ID de unidad de medida
  imagenes?: number[];               // IDs de imágenes asociadas

  // Campos específicos para INSUMO
  precioCompra?: number;             // Solo para insumos
  stockActual?: number;              // Solo para insumos
  stockMaximo?: number;              // Solo para insumos
  esParaElaborar?: boolean;          // Solo para insumos

  // Campos específicos para MANUFACTURADO
  descripcion?: string;              // Solo para manufacturados
  tiempoEstimadoMinutos?: number;    // Solo para manufacturados
  preparacion?: string;              // Instrucciones de preparación
  detalles?: ArticuloManufacturadoDetalle[];  // Receta
}

interface ArticuloManufacturadoDetalle {
  articuloInsumoId: number;          // ID del insumo
  cantidad: number;                  // Cantidad necesaria
}
```

#### **ArticuloDto (Response)**
```typescript
interface ArticuloDto {
  id: number;
  nombre: string;
  denominacion: string;
  precioVenta: number;
  eliminado: boolean;
  unidadMedida: string;              // Nombre de la unidad
  imagenes: ImagenDto[];
  tipo: "INSUMO" | "MANUFACTURADO";

  // Campos específicos de INSUMO
  precioCompra?: number;
  stockActual?: number;
  stockMaximo?: number;
  esParaElaborar?: boolean;

  // Campos específicos de MANUFACTURADO
  descripcion?: string;
  tiempoEstimadoMinutos?: number;
  preparacion?: string;
  detalles?: {
    id: number;
    articuloInsumo: string;          // Nombre del insumo
    cantidad: number;
    costoUnitario: number;           // Calculado automáticamente
    subTotal: number;                // cantidad × costoUnitario
  }[];
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/articulos.ts
export const articulosService = {
  // CRUD básico
  getAll: () => apiClient.get<ArticuloDto[]>('/articulos'),

  create: (data: CreateArticuloRequest) =>
    apiClient.post<ArticuloDto>('/articulos', data),

  getById: (id: number) =>
    apiClient.get<ArticuloDto>(`/articulos/${id}`),

  update: (id: number, data: CreateArticuloRequest) =>
    apiClient.put<ArticuloDto>(`/articulos/${id}`, data),

  delete: (id: number) =>
    apiClient.delete(`/articulos/${id}`),

  // Búsquedas específicas
  searchByName: (nombre: string) =>
    apiClient.get<ArticuloDto[]>(`/articulos/buscar-por-nombre?nombre=${nombre}`),

  getByTipo: (tipo: 'INSUMO' | 'MANUFACTURADO') =>
    apiClient.get<ArticuloDto[]>(`/articulos/buscar-por-tipo?tipo=${tipo}`),

  // Métodos de conveniencia
  getInsumos: () => articulosService.getByTipo('INSUMO'),
  getManufacturados: () => articulosService.getByTipo('MANUFACTURADO'),
};
```

---

## 4. 👥 Gestión de Clientes

### 📡 **Endpoints Disponibles:**

#### **CRUD Básico**
```typescript
// Listar todos los clientes activos
GET http://localhost:8080/api/v1/clientes
// Response: ClienteDto[]

// Crear nuevo cliente
POST http://localhost:8080/api/v1/clientes
// Body: CreateClienteRequest
// Response: ClienteDto (201 Created)

// Obtener cliente por ID
GET http://localhost:8080/api/v1/clientes/{id}
// Response: ClienteDto

// Actualizar cliente
PUT http://localhost:8080/api/v1/clientes/{id}
// Body: CreateClienteRequest
// Response: ClienteDto

// Eliminar cliente (soft delete)
DELETE http://localhost:8080/api/v1/clientes/{id}
// Response: 204 No Content
```

#### **Búsquedas Específicas**
```typescript
// Buscar cliente por email exacto
GET http://localhost:8080/api/v1/clientes/buscar-por-email?email={email}
// Response: ClienteDto

// Buscar clientes por nombre (contiene texto)
GET http://localhost:8080/api/v1/clientes/buscar-por-nombre?nombre={nombre}
// Response: ClienteDto[]

// Buscar clientes por apellido (contiene texto)
GET http://localhost:8080/api/v1/clientes/buscar-por-apellido?apellido={apellido}
// Response: ClienteDto[]

// Buscar cliente por teléfono exacto
GET http://localhost:8080/api/v1/clientes/buscar-por-telefono?telefono={telefono}
// Response: ClienteDto

// Obtener cliente con historial de pedidos
GET http://localhost:8080/api/v1/clientes/{id}/con-pedidos
// Response: ClienteDto (con array de pedidos incluido)
```

### 📝 **Estructuras de Datos:**

#### **CreateClienteRequest**
```typescript
interface CreateClienteRequest {
  nombre: string;                    // Requerido
  apellido: string;                  // Requerido
  email: string;                     // Requerido, único
  telefono: string;                  // Formato: 261-XXXXXXX
  fechaNacimiento: string;           // Formato ISO: YYYY-MM-DD
  usuarioId?: number;                // ID del usuario asociado (opcional)
  imagenId?: number;                 // ID de imagen de perfil (opcional)
  domicilioIds?: number[];           // IDs de domicilios asociados
}
```

#### **ClienteDto (Response)**
```typescript
interface ClienteDto {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  fechaNacimiento: string;           // ISO format
  eliminado: boolean;
  usuario?: {
    id: number;
    nombre: string;
    username: string;
  };
  imagen?: ImagenDto;
  domicilios: DomicilioDto[];
  pedidos?: PedidoDto[];             // Solo en /con-pedidos
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/clientes.ts
export const clientesService = {
  // CRUD básico
  getAll: () => apiClient.get<ClienteDto[]>('/clientes'),

  create: (data: CreateClienteRequest) =>
    apiClient.post<ClienteDto>('/clientes', data),

  getById: (id: number) =>
    apiClient.get<ClienteDto>(`/clientes/${id}`),

  update: (id: number, data: CreateClienteRequest) =>
    apiClient.put<ClienteDto>(`/clientes/${id}`, data),

  delete: (id: number) =>
    apiClient.delete(`/clientes/${id}`),

  // Búsquedas específicas
  searchByEmail: (email: string) =>
    apiClient.get<ClienteDto>(`/clientes/buscar-por-email?email=${email}`),

  searchByName: (nombre: string) =>
    apiClient.get<ClienteDto[]>(`/clientes/buscar-por-nombre?nombre=${nombre}`),

  searchByApellido: (apellido: string) =>
    apiClient.get<ClienteDto[]>(`/clientes/buscar-por-apellido?apellido=${apellido}`),

  searchByTelefono: (telefono: string) =>
    apiClient.get<ClienteDto>(`/clientes/buscar-por-telefono?telefono=${telefono}`),

  // Cliente con historial
  getWithPedidos: (id: number) =>
    apiClient.get<ClienteDto>(`/clientes/${id}/con-pedidos`)
};
```

---

## 5. 🏷️ Gestión de Categorías

### 📡 **Endpoints Disponibles:**

#### **CRUD Básico**
```typescript
// Listar todas las categorías activas
GET http://localhost:8080/api/v1/categorias
// Response: CategoriaDto[]

// Crear nueva categoría
POST http://localhost:8080/api/v1/categorias
// Body: CreateCategoriaRequest
// Response: CategoriaDto (201 Created)

// Obtener categoría por ID
GET http://localhost:8080/api/v1/categorias/{id}
// Response: CategoriaDto

// Actualizar categoría
PUT http://localhost:8080/api/v1/categorias/{id}
// Body: CreateCategoriaRequest
// Response: CategoriaDto

// Eliminar categoría (soft delete)
DELETE http://localhost:8080/api/v1/categorias/{id}
// Response: 204 No Content
```

#### **Búsquedas Específicas**
```typescript
// Buscar categorías por nombre (contiene texto)
GET http://localhost:8080/api/v1/categorias/buscar-por-nombre?nombre={nombre}
// Response: CategoriaDto[]

// Buscar categorías por sucursal
GET http://localhost:8080/api/v1/categorias/buscar-por-sucursal?sucursalId={sucursalId}
// Response: CategoriaDto[]

// Obtener categorías principales (sin padre)
GET http://localhost:8080/api/v1/categorias/principales
// Response: CategoriaDto[]
```

### 📝 **Estructuras de Datos:**

#### **CreateCategoriaRequest**
```typescript
interface CreateCategoriaRequest {
  nombre: string;                    // Requerido
  denominacion: string;              // Descripción
  sucursalId: number;                // ID de sucursal (requerido)
  categoriaPadreId?: number;         // ID de categoría padre (opcional)
  articuloIds?: number[];            // IDs de artículos asociados
}
```

#### **CategoriaDto (Response)**
```typescript
interface CategoriaDto {
  id: number;
  nombre: string;
  denominacion: string;
  eliminado: boolean;
  sucursal: string;                  // Nombre de la sucursal
  categoriaPadre?: {
    id: number;
    nombre: string;
  };
  subcategorias: {
    id: number;
    nombre: string;
  }[];
  articulos: {
    id: number;
    nombre: string;
    tipo: "INSUMO" | "MANUFACTURADO";
  }[];
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/categorias.ts
export const categoriasService = {
  // CRUD básico
  getAll: () => apiClient.get<CategoriaDto[]>('/categorias'),

  create: (data: CreateCategoriaRequest) =>
    apiClient.post<CategoriaDto>('/categorias', data),

  getById: (id: number) =>
    apiClient.get<CategoriaDto>(`/categorias/${id}`),

  update: (id: number, data: CreateCategoriaRequest) =>
    apiClient.put<CategoriaDto>(`/categorias/${id}`, data),

  delete: (id: number) =>
    apiClient.delete(`/categorias/${id}`),

  // Búsquedas específicas
  searchByName: (nombre: string) =>
    apiClient.get<CategoriaDto[]>(`/categorias/buscar-por-nombre?nombre=${nombre}`),

  getBySucursal: (sucursalId: number) =>
    apiClient.get<CategoriaDto[]>(`/categorias/buscar-por-sucursal?sucursalId=${sucursalId}`),

  getPrincipales: () =>
    apiClient.get<CategoriaDto[]>('/categorias/principales')
};
```

---

## 6. 💰 Gestión de Promociones

### 📡 **Endpoints Disponibles:**

#### **CRUD Básico**
```typescript
// Listar todas las promociones activas
GET http://localhost:8080/api/v1/promociones
// Response: PromocionDto[]

// Crear nueva promoción
POST http://localhost:8080/api/v1/promociones
// Body: CreatePromocionRequest
// Response: PromocionDto (201 Created)

// Obtener promoción por ID
GET http://localhost:8080/api/v1/promociones/{id}
// Response: PromocionDto

// Actualizar promoción
PUT http://localhost:8080/api/v1/promociones/{id}
// Body: CreatePromocionRequest
// Response: PromocionDto

// Eliminar promoción (soft delete)
DELETE http://localhost:8080/api/v1/promociones/{id}
// Response: 204 No Content
```

#### **Búsquedas Específicas**
```typescript
// Buscar promociones por nombre (contiene texto)
GET http://localhost:8080/api/v1/promociones/buscar-por-nombre?nombre={nombre}
// Response: PromocionDto[]

// Buscar promociones por sucursal
GET http://localhost:8080/api/v1/promociones/buscar-por-sucursal?sucursalId={sucursalId}
// Response: PromocionDto[]

// Buscar promociones por tipo
GET http://localhost:8080/api/v1/promociones/buscar-por-tipo?tipo={tipo}
// Parámetros: tipo = "PROMOCION1" | "HAPPYHOUR" | etc.
// Response: PromocionDto[]

// Obtener promociones vigentes (activas ahora)
GET http://localhost:8080/api/v1/promociones/vigentes
// Response: PromocionDto[] (filtradas por fecha y hora actual)
```

### 📝 **Estructuras de Datos:**

#### **CreatePromocionRequest**
```typescript
export type TipoPromocion = 'PROMOCION1' | 'HAPPYHOUR' | 'DESCUENTO';

interface CreatePromocionRequest {
  nombre: string;                    // Requerido
  denominacion: string;              // Descripción
  fechaDesde: string;                // ISO format: YYYY-MM-DD
  fechaHasta: string;                // ISO format: YYYY-MM-DD
  horaDesde: string;                 // Formato: HH:mm
  horaHasta: string;                 // Formato: HH:mm
  precioDescuento: number;           // Monto del descuento
  precioPromocional: number;         // Precio final promocional
  tipoPromocion: TipoPromocion;      // Tipo seguro con enum
  sucursalId: number;                // ID de sucursal (requerido)
  articuloIds?: number[];            // IDs de artículos incluidos
  imagenIds?: number[];              // IDs de imágenes promocionales
}
```

#### **PromocionDto (Response)**
```typescript
interface ArticuloSimpleDto {
  id: number;
  nombre: string;
  precioVenta: number;
}

interface ImagenDto {
  id: number;
  nombre: string;
  denominacion: string;
}

interface PromocionDto {
  id: number;
  nombre: string;
  denominacion: string;
  fechaDesde: string;                // ISO format
  fechaHasta: string;                // ISO format
  horaDesde: string;                 // HH:mm
  horaHasta: string;                 // HH:mm
  precioDescuento: number;
  precioPromocional: number;
  tipoPromocion: TipoPromocion;
  eliminado: boolean;
  sucursal: string;                  // Nombre de la sucursal
  empresa?: string;                  // ⭐ AGREGADO: Nombre de la empresa
  articulos: ArticuloSimpleDto[];    // Usando interface separada
  imagenes: ImagenDto[];
  vigente: boolean;                  // Calculado en tiempo real
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/promociones.ts
export const promocionesService = {
  // CRUD básico
  getAll: () => apiClient.get<PromocionDto[]>('/promociones'),

  create: (data: CreatePromocionRequest) =>
    apiClient.post<PromocionDto>('/promociones', data),

  getById: (id: number) =>
    apiClient.get<PromocionDto>(`/promociones/${id}`),

  update: (id: number, data: CreatePromocionRequest) =>
    apiClient.put<PromocionDto>(`/promociones/${id}`, data),

  delete: (id: number) =>
    apiClient.delete(`/promociones/${id}`),

  // Búsquedas específicas
  searchByName: (nombre: string) =>
    apiClient.get<PromocionDto[]>(`/promociones/buscar-por-nombre?nombre=${nombre}`),

  getBySucursal: (sucursalId: number) =>
    apiClient.get<PromocionDto[]>(`/promociones/buscar-por-sucursal?sucursalId=${sucursalId}`),

  getByTipo: (tipo: string) =>
    apiClient.get<PromocionDto[]>(`/promociones/buscar-por-tipo?tipo=${tipo}`),

  getVigentes: () =>
    apiClient.get<PromocionDto[]>('/promociones/vigentes')
};
```

---

## 7. 📊 Gestión de Pedidos

### 📡 **Endpoints Disponibles:**

#### **Solo Lectura** (Los pedidos se crean desde el sistema de ventas)
```typescript
// Listar todos los pedidos
GET http://localhost:8080/api/v1/pedidos
// Response: PedidoDto[]

// Obtener pedido por ID
GET http://localhost:8080/api/v1/pedidos/{id}
// Response: PedidoDto

// Buscar pedidos por cliente
GET http://localhost:8080/api/v1/pedidos/buscar-por-cliente?clienteId={clienteId}
// Response: PedidoDto[]

// Buscar pedidos por estado
GET http://localhost:8080/api/v1/pedidos/buscar-por-estado?estado={estado}
// Parámetros: estado = "PENDIENTE" | "PREPARACION" | "ENTREGADO" | "CANCELADO"
// Response: PedidoDto[]
```

### 📝 **Estructuras de Datos:**

#### **PedidoDto (Response)**
```typescript
interface PedidoDto {
  id: number;
  nombre: string;                    // Nombre del pedido
  fechaPedido: string;               // ISO format
  horaEstimadaFinalizacion: string;  // HH:mm
  total: number;
  totalCosto: number;
  estado: "PENDIENTE" | "PREPARACION" | "ENTREGADO" | "CANCELADO";
  tipoDeEnvio: "DELIVERY" | "TAKEAWAY";
  formaPago: "EFECTIVO" | "MERCADOPAGO";
  eliminado: boolean;
  cliente: {
    id: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono: string;
  };
  sucursal: {
    id: number;
    nombre: string;
  };
  domicilio?: {                      // Solo para DELIVERY
    id: number;
    nombre: string;
    numero: number;
    cp: number;
    localidad: string;
  };
  detallePedidos: {
    id: number;
    nombre: string;
    cantidad: number;
    subTotal: number;
    articulo: {
      id: number;
      nombre: string;
      tipo: "INSUMO" | "MANUFACTURADO";
    };
  }[];
  factura?: {                        // Solo si tiene factura asociada
    id: number;
    nombre: string;
    fechaFacturacion: string;
    totalVenta: number;
  };
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/pedidos.ts
export const pedidosService = {
  // Solo lectura
  getAll: () => apiClient.get<PedidoDto[]>('/pedidos'),

  getById: (id: number) =>
    apiClient.get<PedidoDto>(`/pedidos/${id}`),

  // Búsquedas específicas
  getByCliente: (clienteId: number) =>
    apiClient.get<PedidoDto[]>(`/pedidos/buscar-por-cliente?clienteId=${clienteId}`),

  getByEstado: (estado: string) =>
    apiClient.get<PedidoDto[]>(`/pedidos/buscar-por-estado?estado=${estado}`),

  // Métodos de conveniencia
  getPendientes: () => pedidosService.getByEstado('PENDIENTE'),
  getEnPreparacion: () => pedidosService.getByEstado('PREPARACION'),
  getEntregados: () => pedidosService.getByEstado('ENTREGADO'),
  getCancelados: () => pedidosService.getByEstado('CANCELADO')
};
```

---

## 8. 🧾 Gestión de Facturas

### 📡 **Endpoints Disponibles:**

#### **Solo Lectura** (Las facturas se generan automáticamente)
```typescript
// Listar todas las facturas
GET http://localhost:8080/api/v1/facturas
// Response: FacturaDto[]

// Obtener factura por ID
GET http://localhost:8080/api/v1/facturas/{id}
// Response: FacturaDto

// Obtener estadísticas de facturación
GET http://localhost:8080/api/v1/facturas/estadisticas
// Response: EstadisticasFacturacionDto
```

### 📝 **Estructuras de Datos:**

#### **FacturaDto (Response)**
```typescript
interface FacturaDto {
  id: number;
  nombre: string;
  fechaFacturacion: string;          // ISO format
  totalVenta: number;
  formaPago: "EFECTIVO" | "MERCADOPAGO";
  eliminado: boolean;

  // Datos de MercadoPago (solo si formaPago = "MERCADOPAGO")
  mpPaymentId?: number;
  mpMerchantOrderId?: string;
  mpPreferenceId?: string;
  mpPaymentType?: string;            // "credit_card" | "debit_card" | "ticket"

  // Pedido asociado
  pedido?: {
    id: number;
    nombre: string;
    fechaPedido: string;
    cliente: string;                 // Nombre completo del cliente
  };
}

interface EstadisticasFacturacionDto {
  totalFacturas: number;
  totalVentas: number;
  ventasEfectivo: number;
  ventasMercadoPago: number;
  facturasPorMes: {
    mes: string;                     // YYYY-MM
    cantidad: number;
    total: number;
  }[];
  formasPagoDistribucion: {
    formaPago: string;
    cantidad: number;
    porcentaje: number;
  }[];
}
```

### 🎯 **Ejemplo de Uso en Frontend:**
```typescript
// services/facturas.ts
export const facturasService = {
  // Solo lectura
  getAll: () => apiClient.get<FacturaDto[]>('/facturas'),

  getById: (id: number) =>
    apiClient.get<FacturaDto>(`/facturas/${id}`),

  // Estadísticas
  getEstadisticas: () =>
    apiClient.get<EstadisticasFacturacionDto>('/facturas/estadisticas')
};
```

---

## 9. 🏠 Entidades Geográficas

### 📡 **Endpoints Disponibles:**

**⚠️ Nota**: Estas entidades actualmente no tienen controladores REST expuestos, pero se pueden acceder a través de los datos incluidos en otras respuestas (como Sucursales y Clientes).

#### **Estructura de Datos Implícita**
```typescript
interface PaisDto {
  id: number;
  nombre: string;                    // "Argentina"
}

interface ProvinciaDto {
  id: number;
  nombre: string;                    // "Mendoza"
  pais: string;
}

interface LocalidadDto {
  id: number;
  nombre: string;                    // "Maipú", "Godoy Cruz"
  provincia: string;
}

interface DomicilioDto {
  id: number;
  nombre: string;                    // Nombre de la calle
  numero: number;
  cp: number;                        // Código postal
  localidad: string;
  provincia: string;
  pais: string;
}
```

### 🎯 **Acceso a Datos Geográficos:**
```typescript
// Los datos geográficos se obtienen a través de otros endpoints
// Ejemplo: al obtener sucursales o clientes

// Obtener domicilios a través de sucursales
const sucursales = await sucursalesService.getAll();
const domicilios = sucursales.map(s => s.domicilio);

// Obtener domicilios a través de clientes
const clientes = await clientesService.getAll();
const todosDomicilios = clientes.flatMap(c => c.domicilios);
```

---

## 10. ⚙️ Configuración del Sistema

### 📡 **Endpoints de Salud y Monitoreo:**

```typescript
// Health check personalizado
GET http://localhost:8080/api/v1/health
// Response: HealthDto

// Spring Boot Actuator health
GET http://localhost:8080/actuator/health
// Response: Actuator health format

// Documentación de la API
GET http://localhost:8080/swagger-ui.html
// Swagger UI interface

// API docs JSON
GET http://localhost:8080/api-docs
// OpenAPI JSON specification

// Consola H2 (solo desarrollo)
GET http://localhost:8080/h2-console
// H2 database console
```

### 📝 **Estructuras de Datos:**

#### **HealthDto (Response)**
```typescript
interface HealthDto {
  status: "UP" | "DOWN";
  timestamp: string;                 // ISO format
  version: string;
  environment: string;
  database: {
    status: "UP" | "DOWN";
    url: string;
  };
  components: {
    name: string;
    status: "UP" | "DOWN";
  }[];
}
```

---

## 🛠️ Configuración Global del Cliente API

### **Setup de Axios con TypeScript**
```typescript
// api/client.ts
import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para requests
apiClient.interceptors.request.use(
  (config) => {
    // Agregar token de autenticación si existe
    const token = localStorage.getItem('auth_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Interceptor para responses
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Manejar logout automático
      localStorage.removeItem('auth_token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

### **Types Globales**
```typescript
// types/api.ts
export interface ApiResponse<T> {
  data: T;
  status: number;
  message?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ErrorResponse {
  error: string;
  message: string;
  status: number;
  timestamp: string;
}
```

---

## 🚨 Códigos de Estado HTTP

### **Códigos de Éxito**
- `200 OK` - Operación exitosa
- `201 Created` - Recurso creado exitosamente
- `204 No Content` - Operación exitosa sin contenido (DELETE)

### **Códigos de Error del Cliente**
- `400 Bad Request` - Datos de entrada inválidos
- `404 Not Found` - Recurso no encontrado
- `409 Conflict` - Conflicto (ej: email duplicado)

### **Códigos de Error del Servidor**
- `500 Internal Server Error` - Error interno del servidor

---

## 📋 Checklist de Integración Frontend

### **Antes de Empezar**
- [ ] ✅ Backend corriendo en `http://localhost:8080`
- [ ] ✅ Swagger UI accesible en `/swagger-ui.html`
- [ ] ✅ H2 Console accesible en `/h2-console` (desarrollo)
- [ ] ✅ Datos de prueba cargados automáticamente

### **Durante el Desarrollo**
- [ ] Implementar manejo de errores global
- [ ] Configurar interceptors de Axios
- [ ] Implementar types TypeScript para todas las respuestas
- [ ] Agregar loading states y error states
- [ ] Probar todos los endpoints con datos reales

### **Testing**
- [ ] Probar CRUD completo para cada entidad
- [ ] Verificar búsquedas con datos válidos e inválidos
- [ ] Probar límites de datos (caracteres, números)
- [ ] Verificar relaciones entre entidades
- [ ] Probar escenarios de error

---

**✨ Esta documentación está sincronizada con el backend actual y se actualizará conforme evolucione la API.**