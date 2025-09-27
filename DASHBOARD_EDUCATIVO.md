# 📚 Dashboard Educativo - Sistema de Gestión de Restaurante

## 🎓 Enfoque Educativo

Este dashboard está diseñado para **aprendizaje** usando tecnologías fundamentales de la web, sin frameworks complejos. El objetivo es entender los conceptos básicos de desarrollo frontend y consumo de APIs REST.

---

## 🛠️ Stack Tecnológico Educativo

### **Tecnologías Base**
- ✅ **HTML5** - Estructura semántica y moderna
- ✅ **CSS3** - Estilos puros para entender los fundamentos
- ✅ **Tailwind CSS** - Framework de utilidades para acelerar el desarrollo
- ✅ **TypeScript** - Tipado estático para mejor desarrollo
- ✅ **Vanilla JavaScript** - Sin frameworks, JavaScript puro

### **Herramientas de Desarrollo**
- ✅ **Vite** - Build tool moderno y rápido
- ✅ **TypeScript Compiler** - Compilación de TS a JS
- ✅ **PostCSS** - Procesamiento de CSS con Tailwind
- ✅ **ESLint** - Linting de código TypeScript

### **Bibliotecas Mínimas**
- ✅ **Axios** - Para peticiones HTTP (o Fetch API nativo)
- ✅ **Chart.js** - Para gráficos del dashboard (opcional)
- ✅ **Day.js** - Para manejo de fechas (alternativa ligera a moment.js)

---

## 📋 Estructura del Proyecto Educativo

```
dashboard-restaurante/
├── index.html                 # Página principal
├── package.json               # Dependencias del proyecto
├── vite.config.ts             # Configuración de Vite
├── tailwind.config.js         # Configuración de Tailwind
├── tsconfig.json              # Configuración de TypeScript
│
├── src/
│   ├── main.ts                # Punto de entrada principal
│   ├── style.css              # Estilos globales + Tailwind
│   │
│   ├── types/                 # Definiciones de tipos TypeScript
│   │   ├── api.ts             # Tipos de la API
│   │   ├── empresa.ts         # Tipos de empresa
│   │   ├── sucursal.ts        # Tipos de sucursal
│   │   ├── promocion.ts       # Tipos de promoción
│   │   └── index.ts           # Exportaciones
│   │
│   ├── services/              # Servicios para consumir API
│   │   ├── api-client.ts      # Cliente HTTP configurado
│   │   ├── empresas.service.ts
│   │   ├── sucursales.service.ts
│   │   ├── promociones.service.ts
│   │   └── index.ts
│   │
│   ├── components/            # Componentes reutilizables
│   │   ├── layout/
│   │   │   ├── header.ts
│   │   │   ├── sidebar.ts
│   │   │   └── footer.ts
│   │   ├── forms/
│   │   │   ├── empresa-form.ts
│   │   │   ├── sucursal-form.ts
│   │   │   └── promocion-form.ts
│   │   └── tables/
│   │       ├── data-table.ts
│   │       └── pagination.ts
│   │
│   ├── pages/                 # Páginas del dashboard
│   │   ├── dashboard.ts       # Dashboard principal
│   │   ├── empresas/
│   │   │   ├── empresas-list.ts
│   │   │   ├── empresa-create.ts
│   │   │   └── empresa-detail.ts
│   │   ├── sucursales/
│   │   │   ├── sucursales-list.ts
│   │   │   └── sucursal-detail.ts
│   │   └── promociones/
│   │       ├── promociones-list.ts
│   │       ├── promocion-create.ts
│   │       └── promocion-detail.ts
│   │
│   ├── utils/                 # Utilidades
│   │   ├── dom-helpers.ts     # Helpers para manipulación DOM
│   │   ├── date-helpers.ts    # Helpers para fechas
│   │   ├── validation.ts      # Validaciones de formularios
│   │   └── constants.ts       # Constantes del proyecto
│   │
│   └── router/                # Router simple
│       ├── router.ts          # Sistema de routing básico
│       └── routes.ts          # Definición de rutas
│
└── public/                    # Archivos estáticos
    ├── favicon.ico
    └── images/
```

---

## 🎯 Funcionalidad Principal: Promociones por Empresa/Sucursal

### **Flujo de Trabajo Requerido:**

#### **1. Gestión de Empresas → Sucursales → Promociones**
```
Empresa (TechFood Solutions)
├── Sucursal Centro
│   ├── Promoción: "Happy Hour Centro"
│   ├── Promoción: "2x1 Pizzas Martes"
│   └── Promoción: "Descuento Estudiantes"
└── Sucursal Norte
    ├── Promoción: "Combo Familiar Norte"
    ├── Promoción: "Lunes Sin IVA"
    └── Promoción: "Promo Delivery Gratis"
```

#### **2. URLs del Backend para Promociones:**
```typescript
// Crear promoción para una sucursal específica
POST http://localhost:8080/api/v1/promociones
Body: {
  "nombre": "Happy Hour Centro",
  "denominacion": "2x1 en bebidas de 17:00 a 20:00",
  "fechaDesde": "2025-01-01",
  "fechaHasta": "2025-12-31",
  "horaDesde": "17:00",
  "horaHasta": "20:00",
  "precioDescuento": 50.0,
  "precioPromocional": 150.0,
  "tipoPromocion": "HAPPYHOUR",
  "sucursalId": 1,  // ⭐ CLAVE: Asignar a sucursal específica
  "articuloIds": [1, 2, 3]
}

// Obtener promociones de una empresa (a través de sus sucursales)
GET http://localhost:8080/api/v1/promociones/buscar-por-sucursal?sucursalId={sucursalId}

// Obtener todas las promociones vigentes
GET http://localhost:8080/api/v1/promociones/vigentes
```

### **3. Interfaz de Usuario para Promociones:**

#### **Página: Gestión de Promociones**
```html
<!-- promociones-list.html -->
<div class="promociones-container">
  <!-- Filtros por Empresa/Sucursal -->
  <div class="filters-section bg-white p-6 rounded-lg shadow mb-6">
    <h3 class="text-lg font-semibold mb-4">Filtrar Promociones</h3>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <!-- Selector de Empresa -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Empresa</label>
        <select id="empresa-select" class="w-full border-gray-300 rounded-md">
          <option value="">Todas las empresas</option>
          <!-- Opciones dinámicas -->
        </select>
      </div>

      <!-- Selector de Sucursal -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Sucursal</label>
        <select id="sucursal-select" class="w-full border-gray-300 rounded-md">
          <option value="">Todas las sucursales</option>
          <!-- Opciones dinámicas basadas en empresa seleccionada -->
        </select>
      </div>

      <!-- Filtro de Estado -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Estado</label>
        <select id="estado-select" class="w-full border-gray-300 rounded-md">
          <option value="">Todas</option>
          <option value="vigente">Vigentes</option>
          <option value="vencidas">Vencidas</option>
          <option value="futuras">Futuras</option>
        </select>
      </div>
    </div>
  </div>

  <!-- Botón Crear Nueva Promoción -->
  <div class="flex justify-between items-center mb-6">
    <h2 class="text-2xl font-bold text-gray-900">Promociones</h2>
    <button id="crear-promocion-btn"
            class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors">
      + Nueva Promoción
    </button>
  </div>

  <!-- Tabla de Promociones -->
  <div class="bg-white rounded-lg shadow overflow-hidden">
    <table class="min-w-full divide-y divide-gray-200">
      <thead class="bg-gray-50">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Promoción
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Empresa / Sucursal
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Vigencia
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Descuento
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Estado
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Acciones
          </th>
        </tr>
      </thead>
      <tbody id="promociones-table-body" class="bg-white divide-y divide-gray-200">
        <!-- Contenido dinámico -->
      </tbody>
    </table>
  </div>
</div>
```

#### **Formulario: Crear/Editar Promoción**
```html
<!-- promocion-form.html -->
<div class="promocion-form-container">
  <div class="bg-white p-8 rounded-lg shadow-lg max-w-2xl mx-auto">
    <h2 class="text-2xl font-bold text-gray-900 mb-6">Crear Nueva Promoción</h2>

    <form id="promocion-form" class="space-y-6">
      <!-- Información Básica -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Nombre de la Promoción *</label>
          <input type="text" id="nombre" required
                 class="w-full border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500">
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Tipo de Promoción *</label>
          <select id="tipoPromocion" required class="w-full border-gray-300 rounded-md">
            <option value="">Seleccionar tipo</option>
            <option value="PROMOCION1">Promoción Estándar</option>
            <option value="HAPPYHOUR">Happy Hour</option>
            <option value="DESCUENTO">Descuento Especial</option>
          </select>
        </div>
      </div>

      <!-- Asignación a Empresa/Sucursal -->
      <div class="bg-gray-50 p-4 rounded-lg">
        <h3 class="text-lg font-medium text-gray-900 mb-4">Asignación</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Empresa *</label>
            <select id="empresaId" required class="w-full border-gray-300 rounded-md">
              <option value="">Seleccionar empresa</option>
              <!-- Opciones dinámicas -->
            </select>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Sucursal *</label>
            <select id="sucursalId" required class="w-full border-gray-300 rounded-md">
              <option value="">Primero selecciona una empresa</option>
              <!-- Opciones dinámicas basadas en empresa -->
            </select>
          </div>
        </div>
      </div>

      <!-- Vigencia -->
      <div class="bg-blue-50 p-4 rounded-lg">
        <h3 class="text-lg font-medium text-gray-900 mb-4">Vigencia</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Fecha Desde *</label>
            <input type="date" id="fechaDesde" required
                   class="w-full border-gray-300 rounded-md">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Fecha Hasta *</label>
            <input type="date" id="fechaHasta" required
                   class="w-full border-gray-300 rounded-md">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Hora Desde</label>
            <input type="time" id="horaDesde"
                   class="w-full border-gray-300 rounded-md">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Hora Hasta</label>
            <input type="time" id="horaHasta"
                   class="w-full border-gray-300 rounded-md">
          </div>
        </div>
      </div>

      <!-- Precios -->
      <div class="bg-green-50 p-4 rounded-lg">
        <h3 class="text-lg font-medium text-gray-900 mb-4">Precios</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Precio de Descuento *</label>
            <input type="number" id="precioDescuento" step="0.01" required
                   class="w-full border-gray-300 rounded-md">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Precio Promocional *</label>
            <input type="number" id="precioPromocional" step="0.01" required
                   class="w-full border-gray-300 rounded-md">
          </div>
        </div>
      </div>

      <!-- Descripción -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">Descripción</label>
        <textarea id="denominacion" rows="3"
                  class="w-full border-gray-300 rounded-md focus:ring-blue-500 focus:border-blue-500"></textarea>
      </div>

      <!-- Botones -->
      <div class="flex justify-end space-x-4">
        <button type="button" id="cancelar-btn"
                class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50">
          Cancelar
        </button>
        <button type="submit"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">
          Crear Promoción
        </button>
      </div>
    </form>
  </div>
</div>
```

---

## 💻 Código TypeScript Educativo

### **1. Configuración del Proyecto**

#### **package.json**
```json
{
  "name": "dashboard-restaurante-educativo",
  "version": "1.0.0",
  "description": "Dashboard educativo para gestión de restaurante",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview",
    "lint": "eslint src --ext .ts,.js"
  },
  "devDependencies": {
    "vite": "^5.0.0",
    "typescript": "^5.0.0",
    "@types/node": "^20.0.0",
    "tailwindcss": "^3.4.0",
    "postcss": "^8.4.0",
    "autoprefixer": "^10.4.0",
    "eslint": "^8.0.0",
    "@typescript-eslint/eslint-plugin": "^6.0.0",
    "@typescript-eslint/parser": "^6.0.0"
  },
  "dependencies": {
    "axios": "^1.6.0",
    "dayjs": "^1.11.0"
  }
}
```

#### **vite.config.ts**
```typescript
import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: true
  }
});
```

#### **tailwind.config.js**
```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,html}"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8'
        }
      }
    },
  },
  plugins: [],
}
```

### **2. Tipos TypeScript**

#### **src/types/promocion.ts**
```typescript
export interface PromocionCreateRequest {
  nombre: string;
  denominacion: string;
  fechaDesde: string;      // YYYY-MM-DD
  fechaHasta: string;      // YYYY-MM-DD
  horaDesde: string;       // HH:mm
  horaHasta: string;       // HH:mm
  precioDescuento: number;
  precioPromocional: number;
  tipoPromocion: TipoPromocion;
  sucursalId: number;      // ⭐ CLAVE para asociar a sucursal
  articuloIds?: number[];
  imagenIds?: number[];
}

export interface PromocionDto {
  id: number;
  nombre: string;
  denominacion: string;
  fechaDesde: string;
  fechaHasta: string;
  horaDesde: string;
  horaHasta: string;
  precioDescuento: number;
  precioPromocional: number;
  tipoPromocion: TipoPromocion;
  eliminado: boolean;
  sucursal: string;        // Nombre de la sucursal
  empresa?: string;        // Agregado para mostrar empresa
  articulos: ArticuloSimpleDto[];
  imagenes: ImagenDto[];
  vigente: boolean;        // Calculado en tiempo real
}

export type TipoPromocion = 'PROMOCION1' | 'HAPPYHOUR' | 'DESCUENTO';

export interface ArticuloSimpleDto {
  id: number;
  nombre: string;
  precioVenta: number;
}

export interface ImagenDto {
  id: number;
  nombre: string;
  denominacion: string;
}
```

### **3. Servicio de Promociones**

#### **src/services/promociones.service.ts**
```typescript
import { apiClient } from './api-client';
import { PromocionDto, PromocionCreateRequest } from '../types/promocion';

export class PromocionesService {
  private readonly baseUrl = '/promociones';

  // CRUD Básico
  async getAll(): Promise<PromocionDto[]> {
    const response = await apiClient.get<PromocionDto[]>(this.baseUrl);
    return response.data;
  }

  async getById(id: number): Promise<PromocionDto> {
    const response = await apiClient.get<PromocionDto>(`${this.baseUrl}/${id}`);
    return response.data;
  }

  async create(promocion: PromocionCreateRequest): Promise<PromocionDto> {
    const response = await apiClient.post<PromocionDto>(this.baseUrl, promocion);
    return response.data;
  }

  async update(id: number, promocion: PromocionCreateRequest): Promise<PromocionDto> {
    const response = await apiClient.put<PromocionDto>(`${this.baseUrl}/${id}`, promocion);
    return response.data;
  }

  async delete(id: number): Promise<void> {
    await apiClient.delete(`${this.baseUrl}/${id}`);
  }

  // Búsquedas Específicas
  async searchByName(nombre: string): Promise<PromocionDto[]> {
    const response = await apiClient.get<PromocionDto[]>(
      `${this.baseUrl}/buscar-por-nombre?nombre=${nombre}`
    );
    return response.data;
  }

  // ⭐ FUNCIONALIDAD CLAVE: Promociones por sucursal
  async getBySucursal(sucursalId: number): Promise<PromocionDto[]> {
    const response = await apiClient.get<PromocionDto[]>(
      `${this.baseUrl}/buscar-por-sucursal?sucursalId=${sucursalId}`
    );
    return response.data;
  }

  async getByTipo(tipo: string): Promise<PromocionDto[]> {
    const response = await apiClient.get<PromocionDto[]>(
      `${this.baseUrl}/buscar-por-tipo?tipo=${tipo}`
    );
    return response.data;
  }

  async getVigentes(): Promise<PromocionDto[]> {
    const response = await apiClient.get<PromocionDto[]>(`${this.baseUrl}/vigentes`);
    return response.data;
  }

  // ⭐ MÉTODO ADICIONAL: Obtener promociones por empresa
  async getByEmpresa(empresaId: number): Promise<PromocionDto[]> {
    // Primero obtenemos las sucursales de la empresa
    const sucursalesResponse = await apiClient.get(
      `/sucursales/buscar-por-empresa?empresaId=${empresaId}`
    );
    const sucursales = sucursalesResponse.data;

    // Luego obtenemos las promociones de cada sucursal
    const promociones: PromocionDto[] = [];
    for (const sucursal of sucursales) {
      const promocionesSucursal = await this.getBySucursal(sucursal.id);
      promociones.push(...promocionesSucursal);
    }

    return promociones;
  }
}

export const promocionesService = new PromocionesService();
```

### **4. Componente de Formulario de Promoción**

#### **src/components/forms/promocion-form.ts**
```typescript
import { EmpresaDto } from '../../types/empresa';
import { SucursalDto } from '../../types/sucursal';
import { PromocionCreateRequest, TipoPromocion } from '../../types/promocion';
import { empresasService } from '../../services/empresas.service';
import { sucursalesService } from '../../services/sucursales.service';
import { promocionesService } from '../../services/promociones.service';

export class PromocionForm {
  private empresas: EmpresaDto[] = [];
  private sucursales: SucursalDto[] = [];
  private form: HTMLFormElement;

  constructor(private container: HTMLElement) {
    this.form = container.querySelector('#promocion-form') as HTMLFormElement;
    this.init();
  }

  private async init(): Promise<void> {
    await this.loadEmpresas();
    this.setupEventListeners();
  }

  private async loadEmpresas(): Promise<void> {
    try {
      this.empresas = await empresasService.getAll();
      this.populateEmpresasSelect();
    } catch (error) {
      console.error('Error cargando empresas:', error);
      this.showError('Error al cargar las empresas');
    }
  }

  private populateEmpresasSelect(): void {
    const empresaSelect = this.form.querySelector('#empresaId') as HTMLSelectElement;
    empresaSelect.innerHTML = '<option value="">Seleccionar empresa</option>';

    this.empresas.forEach(empresa => {
      const option = document.createElement('option');
      option.value = empresa.id.toString();
      option.textContent = empresa.nombre;
      empresaSelect.appendChild(option);
    });
  }

  private async loadSucursalesByEmpresa(empresaId: number): Promise<void> {
    try {
      this.sucursales = await sucursalesService.getByEmpresa(empresaId);
      this.populateSucursalesSelect();
    } catch (error) {
      console.error('Error cargando sucursales:', error);
      this.showError('Error al cargar las sucursales');
    }
  }

  private populateSucursalesSelect(): void {
    const sucursalSelect = this.form.querySelector('#sucursalId') as HTMLSelectElement;
    sucursalSelect.innerHTML = '<option value="">Seleccionar sucursal</option>';

    this.sucursales.forEach(sucursal => {
      const option = document.createElement('option');
      option.value = sucursal.id.toString();
      option.textContent = `${sucursal.nombre} (${sucursal.empresa})`;
      sucursalSelect.appendChild(option);
    });
  }

  private setupEventListeners(): void {
    // Cambio de empresa → cargar sucursales
    const empresaSelect = this.form.querySelector('#empresaId') as HTMLSelectElement;
    empresaSelect.addEventListener('change', async (e) => {
      const empresaId = parseInt((e.target as HTMLSelectElement).value);
      if (empresaId) {
        await this.loadSucursalesByEmpresa(empresaId);
      } else {
        const sucursalSelect = this.form.querySelector('#sucursalId') as HTMLSelectElement;
        sucursalSelect.innerHTML = '<option value="">Primero selecciona una empresa</option>';
      }
    });

    // Submit del formulario
    this.form.addEventListener('submit', async (e) => {
      e.preventDefault();
      await this.handleSubmit();
    });

    // Botón cancelar
    const cancelarBtn = this.form.querySelector('#cancelar-btn') as HTMLButtonElement;
    cancelarBtn.addEventListener('click', () => {
      this.resetForm();
    });
  }

  private async handleSubmit(): Promise<void> {
    try {
      const formData = new FormData(this.form);
      const promocionData: PromocionCreateRequest = {
        nombre: formData.get('nombre') as string,
        denominacion: formData.get('denominacion') as string,
        fechaDesde: formData.get('fechaDesde') as string,
        fechaHasta: formData.get('fechaHasta') as string,
        horaDesde: formData.get('horaDesde') as string,
        horaHasta: formData.get('horaHasta') as string,
        precioDescuento: parseFloat(formData.get('precioDescuento') as string),
        precioPromocional: parseFloat(formData.get('precioPromocional') as string),
        tipoPromocion: formData.get('tipoPromocion') as TipoPromocion,
        sucursalId: parseInt(formData.get('sucursalId') as string)
      };

      if (!this.validatePromocion(promocionData)) {
        return;
      }

      const nuevaPromocion = await promocionesService.create(promocionData);
      this.showSuccess(`Promoción "${nuevaPromocion.nombre}" creada exitosamente`);
      this.resetForm();

      // Opcional: redirigir a la lista de promociones
      window.location.hash = '#/promociones';

    } catch (error) {
      console.error('Error creando promoción:', error);
      this.showError('Error al crear la promoción');
    }
  }

  private validatePromocion(promocion: PromocionCreateRequest): boolean {
    // Validaciones básicas
    if (!promocion.nombre || !promocion.sucursalId) {
      this.showError('Nombre y sucursal son requeridos');
      return false;
    }

    if (new Date(promocion.fechaDesde) > new Date(promocion.fechaHasta)) {
      this.showError('La fecha de inicio no puede ser mayor a la fecha de fin');
      return false;
    }

    if (promocion.precioPromocional <= 0) {
      this.showError('El precio promocional debe ser mayor a 0');
      return false;
    }

    return true;
  }

  private resetForm(): void {
    this.form.reset();
    const sucursalSelect = this.form.querySelector('#sucursalId') as HTMLSelectElement;
    sucursalSelect.innerHTML = '<option value="">Primero selecciona una empresa</option>';
  }

  private showSuccess(message: string): void {
    // Implementar notification success
    alert(`✅ ${message}`);
  }

  private showError(message: string): void {
    // Implementar notification error
    alert(`❌ ${message}`);
  }
}
```

### **5. Página de Lista de Promociones**

#### **src/pages/promociones/promociones-list.ts**
```typescript
import { PromocionDto } from '../../types/promocion';
import { EmpresaDto } from '../../types/empresa';
import { SucursalDto } from '../../types/sucursal';
import { promocionesService } from '../../services/promociones.service';
import { empresasService } from '../../services/empresas.service';
import { sucursalesService } from '../../services/sucursales.service';

export class PromocionesListPage {
  private promociones: PromocionDto[] = [];
  private empresas: EmpresaDto[] = [];
  private sucursales: SucursalDto[] = [];
  private filtros = {
    empresaId: null as number | null,
    sucursalId: null as number | null,
    estado: 'todas' as 'todas' | 'vigente' | 'vencidas' | 'futuras'
  };

  constructor(private container: HTMLElement) {
    this.init();
  }

  private async init(): Promise<void> {
    await this.loadData();
    this.setupEventListeners();
    this.renderPromociones();
  }

  private async loadData(): Promise<void> {
    try {
      // Cargar datos en paralelo
      const [promociones, empresas] = await Promise.all([
        promocionesService.getAll(),
        empresasService.getAll()
      ]);

      this.promociones = promociones;
      this.empresas = empresas;

      this.populateEmpresasFilter();
    } catch (error) {
      console.error('Error cargando datos:', error);
    }
  }

  private populateEmpresasFilter(): void {
    const empresaSelect = this.container.querySelector('#empresa-select') as HTMLSelectElement;
    empresaSelect.innerHTML = '<option value="">Todas las empresas</option>';

    this.empresas.forEach(empresa => {
      const option = document.createElement('option');
      option.value = empresa.id.toString();
      option.textContent = empresa.nombre;
      empresaSelect.appendChild(option);
    });
  }

  private async loadSucursalesByEmpresa(empresaId: number): Promise<void> {
    try {
      this.sucursales = await sucursalesService.getByEmpresa(empresaId);
      this.populateSucursalesFilter();
    } catch (error) {
      console.error('Error cargando sucursales:', error);
    }
  }

  private populateSucursalesFilter(): void {
    const sucursalSelect = this.container.querySelector('#sucursal-select') as HTMLSelectElement;
    sucursalSelect.innerHTML = '<option value="">Todas las sucursales</option>';

    this.sucursales.forEach(sucursal => {
      const option = document.createElement('option');
      option.value = sucursal.id.toString();
      option.textContent = sucursal.nombre;
      sucursalSelect.appendChild(option);
    });
  }

  private setupEventListeners(): void {
    // Filtro por empresa
    const empresaSelect = this.container.querySelector('#empresa-select') as HTMLSelectElement;
    empresaSelect.addEventListener('change', async (e) => {
      const empresaId = parseInt((e.target as HTMLSelectElement).value);
      this.filtros.empresaId = empresaId || null;

      if (empresaId) {
        await this.loadSucursalesByEmpresa(empresaId);
      } else {
        const sucursalSelect = this.container.querySelector('#sucursal-select') as HTMLSelectElement;
        sucursalSelect.innerHTML = '<option value="">Todas las sucursales</option>';
        this.filtros.sucursalId = null;
      }

      await this.aplicarFiltros();
    });

    // Filtro por sucursal
    const sucursalSelect = this.container.querySelector('#sucursal-select') as HTMLSelectElement;
    sucursalSelect.addEventListener('change', async (e) => {
      const sucursalId = parseInt((e.target as HTMLSelectElement).value);
      this.filtros.sucursalId = sucursalId || null;
      await this.aplicarFiltros();
    });

    // Filtro por estado
    const estadoSelect = this.container.querySelector('#estado-select') as HTMLSelectElement;
    estadoSelect.addEventListener('change', async (e) => {
      this.filtros.estado = (e.target as HTMLSelectElement).value as any;
      await this.aplicarFiltros();
    });

    // Botón crear promoción
    const crearBtn = this.container.querySelector('#crear-promocion-btn') as HTMLButtonElement;
    crearBtn.addEventListener('click', () => {
      window.location.hash = '#/promociones/crear';
    });
  }

  private async aplicarFiltros(): Promise<void> {
    try {
      let promocionesFiltradas: PromocionDto[];

      // Filtrar por sucursal específica
      if (this.filtros.sucursalId) {
        promocionesFiltradas = await promocionesService.getBySucursal(this.filtros.sucursalId);
      }
      // Filtrar por empresa (todas sus sucursales)
      else if (this.filtros.empresaId) {
        promocionesFiltradas = await promocionesService.getByEmpresa(this.filtros.empresaId);
      }
      // Sin filtro de empresa/sucursal
      else {
        promocionesFiltradas = await promocionesService.getAll();
      }

      // Filtrar por estado
      if (this.filtros.estado !== 'todas') {
        promocionesFiltradas = this.filtrarPorEstado(promocionesFiltradas, this.filtros.estado);
      }

      this.promociones = promocionesFiltradas;
      this.renderPromociones();

    } catch (error) {
      console.error('Error aplicando filtros:', error);
    }
  }

  private filtrarPorEstado(promociones: PromocionDto[], estado: string): PromocionDto[] {
    const ahora = new Date();

    return promociones.filter(promocion => {
      const fechaDesde = new Date(promocion.fechaDesde);
      const fechaHasta = new Date(promocion.fechaHasta);

      switch (estado) {
        case 'vigente':
          return ahora >= fechaDesde && ahora <= fechaHasta;
        case 'vencidas':
          return ahora > fechaHasta;
        case 'futuras':
          return ahora < fechaDesde;
        default:
          return true;
      }
    });
  }

  private renderPromociones(): void {
    const tableBody = this.container.querySelector('#promociones-table-body') as HTMLTableSectionElement;

    if (this.promociones.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="6" class="px-6 py-4 text-center text-gray-500">
            No hay promociones para mostrar
          </td>
        </tr>
      `;
      return;
    }

    tableBody.innerHTML = this.promociones.map(promocion => this.renderPromocionRow(promocion)).join('');
  }

  private renderPromocionRow(promocion: PromocionDto): string {
    const estadoBadge = this.getEstadoBadge(promocion);
    const empresa = this.empresas.find(e => e.sucursales?.some(s => s.nombre === promocion.sucursal));

    return `
      <tr class="hover:bg-gray-50">
        <td class="px-6 py-4 whitespace-nowrap">
          <div>
            <div class="text-sm font-medium text-gray-900">${promocion.nombre}</div>
            <div class="text-sm text-gray-500">${promocion.denominacion}</div>
          </div>
        </td>
        <td class="px-6 py-4 whitespace-nowrap">
          <div class="text-sm text-gray-900">${empresa?.nombre || 'N/A'}</div>
          <div class="text-sm text-gray-500">${promocion.sucursal}</div>
        </td>
        <td class="px-6 py-4 whitespace-nowrap">
          <div class="text-sm text-gray-900">${promocion.fechaDesde} - ${promocion.fechaHasta}</div>
          <div class="text-sm text-gray-500">${promocion.horaDesde} - ${promocion.horaHasta}</div>
        </td>
        <td class="px-6 py-4 whitespace-nowrap">
          <div class="text-sm text-gray-900">$${promocion.precioDescuento}</div>
          <div class="text-sm text-gray-500">Final: $${promocion.precioPromocional}</div>
        </td>
        <td class="px-6 py-4 whitespace-nowrap">
          ${estadoBadge}
        </td>
        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
          <button onclick="editarPromocion(${promocion.id})"
                  class="text-blue-600 hover:text-blue-900 mr-3">Editar</button>
          <button onclick="eliminarPromocion(${promocion.id})"
                  class="text-red-600 hover:text-red-900">Eliminar</button>
        </td>
      </tr>
    `;
  }

  private getEstadoBadge(promocion: PromocionDto): string {
    if (promocion.vigente) {
      return '<span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-green-100 text-green-800">Vigente</span>';
    }

    const ahora = new Date();
    const fechaDesde = new Date(promocion.fechaDesde);
    const fechaHasta = new Date(promocion.fechaHasta);

    if (ahora < fechaDesde) {
      return '<span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">Futura</span>';
    } else if (ahora > fechaHasta) {
      return '<span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-red-100 text-red-800">Vencida</span>';
    }

    return '<span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-gray-100 text-gray-800">Inactiva</span>';
  }
}

// Funciones globales para los botones de acción
(window as any).editarPromocion = (id: number) => {
  window.location.hash = `#/promociones/editar/${id}`;
};

(window as any).eliminarPromocion = async (id: number) => {
  if (confirm('¿Estás seguro de que deseas eliminar esta promoción?')) {
    try {
      await promocionesService.delete(id);
      window.location.reload(); // Recargar la página para mostrar cambios
    } catch (error) {
      alert('Error al eliminar la promoción');
    }
  }
};
```

---

## 📚 Conceptos Educativos Cubiertos

### **1. Fundamentos Web**
- ✅ **HTML5 Semántico** - Estructura clara y accesible
- ✅ **CSS3 + Tailwind** - Estilos utilitarios y diseño responsivo
- ✅ **TypeScript** - Tipado estático y mejor desarrollo
- ✅ **DOM Manipulation** - Interacción directa con elementos

### **2. Arquitectura de Software**
- ✅ **Separación de Responsabilidades** - Services, Components, Types
- ✅ **Patrón MVC** - Model (Types), View (HTML), Controller (TS Classes)
- ✅ **Programación Orientada a Objetos** - Classes y Encapsulación
- ✅ **Gestión de Estado** - Estado local en componentes

### **3. Comunicación con APIs**
- ✅ **HTTP Methods** - GET, POST, PUT, DELETE
- ✅ **Promises y Async/Await** - Programación asíncrona
- ✅ **Error Handling** - Manejo robusto de errores
- ✅ **Data Transformation** - Mapping de DTOs

### **4. Experiencia de Usuario**
- ✅ **Formularios Dinámicos** - Validación y campos dependientes
- ✅ **Filtros Interactivos** - Búsqueda y filtrado en tiempo real
- ✅ **Responsive Design** - Adaptable a diferentes dispositivos
- ✅ **Loading States** - Feedback visual para el usuario

---

## 🎯 Próximos Pasos

### **Orden de Implementación:**
1. **Setup del Proyecto** (Vite + TypeScript + Tailwind)
2. **Configuración de Services** (API Client + Error Handling)
3. **Módulo de Empresas** (CRUD básico)
4. **Módulo de Sucursales** (Lectura y filtros)
5. **⭐ Módulo de Promociones** (CRUD completo con relaciones)
6. **Dashboard Principal** (Métricas y resumen)
7. **Optimizaciones** (Performance y UX)

### **Funcionalidades Clave para Promociones:**
- ✅ **Crear promoción** asignada a sucursal específica
- ✅ **Filtrar promociones** por empresa y sucursal
- ✅ **Validar vigencia** en tiempo real
- ✅ **Mostrar jerarquía** Empresa → Sucursal → Promociones
- ✅ **Gestión completa** de ciclo de vida de promociones

<function_calls>
<invoke name="TodoWrite">
<parameter name="todos">[{"content": "Agregar funcionalidad de crear promociones por empresa/sucursal", "status": "completed", "activeForm": "Agregando funcionalidad de crear promociones por empresa/sucursal"}, {"content": "Actualizar stack a HTML5 + CSS + Tailwind + TypeScript", "status": "completed", "activeForm": "Actualizando stack a HTML5 + CSS + Tailwind + TypeScript"}, {"content": "Crear documentaci\u00f3n educativa del dashboard", "status": "completed", "activeForm": "Creando documentaci\u00f3n educativa del dashboard"}, {"content": "Documentar flujo de promociones por sucursal", "status": "completed", "activeForm": "Documentando flujo de promociones por sucursal"}]