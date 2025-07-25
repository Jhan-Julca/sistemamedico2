# 💊 Sistema de Gestión de Farmacia - Backend

Sistema backend integral de gestión para farmacias modernas desarrollado con Spring Boot, que incluye integración con RENIEC para validación automática de clientes.

## 🚀 Características Principales

- **Framework**: Spring Boot 3.3.4 (Java 17)
- **Base de Datos**: MySQL 8.0+
- **Arquitectura**: Service Interface + ServiceImpl
- **API Externa**: Integración con RENIEC para validación de clientes
- **Gestión de Cache**: Sistema de cache inteligente para consultas RENIEC
- **API REST**: Endpoints completos para todas las operaciones

## 🏗️ Arquitectura del Sistema

### Patrón Service-ServiceImpl
```
Controller → Service Interface → ServiceImpl → Repository → Database
                                      ↓
                               External APIs (RENIEC)
```

### Estructura del Proyecto
```
src/main/java/com/example/sistemafarmacia/
├── SistemafarmaciaApplication.java
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── ClienteController.java
│   ├── ProductoController.java
│   ├── VentaController.java
│   └── VentaDetalleController.java
├── model/
│   ├── Cliente.java
│   ├── Producto.java
│   ├── Venta.java
│   ├── VentaDetalle.java
│   ├── VentaDTO.java
│   └── dto/
│       ├── ReniecResponseDTO.java
│       ├── ClienteResponseDTO.java
│       └── ClienteManualDTO.java
├── repository/
│   ├── ClientesRepository.java
│   ├── ProductoRepository.java
│   ├── VentaRepository.java
│   └── VentaDetalleRepository.java
├── service/
│   ├── ClienteService.java
│   ├── ProductoService.java
│   ├── VentaService.java
│   ├── VentaDetalleService.java
│   ├── ReniecService.java
│   └── ClienteCacheService.java
└── serviceImpl/
    ├── ClienteServiceImpl.java
    ├── ProductoServiceImpl.java
    ├── VentaServiceImpl.java
    ├── VentaDetalleServiceImpl.java
    ├── ReniecServiceImpl.java
    └── ClienteCacheServiceImpl.java
```

## 🛠️ Configuración e Instalación

### Prerequisitos
- **Java 17** o superior
- **Maven 3.6+**
- **MySQL 8.0+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### 1. Configuración de Base de Datos

```sql
-- Crear la base de datos
CREATE DATABASE farmacia_db;

-- Ejecutar el script completo desde:
-- database/farmacia_db_setup.sql
```

### 2. Configuración del Proyecto

Actualizar las credenciales de base de datos en `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/farmacia_db
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
```

### 3. Compilar y Ejecutar

```bash
# Limpiar y compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run

# El servidor estará disponible en: http://localhost:8010
```

## 📡 API Endpoints

### 🧑‍💼 Cliente Controller (`/api/cliente`)

#### Integración RENIEC
- **GET** `/api/cliente/dni/{dni}` - Obtiene cliente por DNI (BD → Cache → RENIEC → Manual)
- **POST** `/api/cliente/manual` - Registra cliente manualmente
- **GET** `/api/cliente/buscar?termino={termino}` - Búsqueda por nombre
- **GET** `/api/cliente/cache/stats` - Estadísticas del cache
- **DELETE** `/api/cliente/cache` - Limpiar cache

#### CRUD Tradicional
- **GET** `/api/cliente` - Obtener todos los clientes
- **GET** `/api/cliente/{id}` - Obtener cliente por ID
- **POST** `/api/cliente` - Crear cliente
- **PUT** `/api/cliente/{id}` - Actualizar cliente
- **DELETE** `/api/cliente/{id}` - Eliminar cliente

### 💊 Producto Controller (`/api/producto`)
- **GET** `/api/producto` - Obtener todos los productos
- **GET** `/api/producto/{id}` - Obtener producto por ID
- **GET** `/api/producto/buscar/nombre?nombre={nombre}` - Buscar por nombre
- **GET** `/api/producto/buscar/categoria?categoria={categoria}` - Buscar por categoría
- **POST** `/api/producto` - Crear producto
- **PUT** `/api/producto/{id}` - Actualizar producto
- **DELETE** `/api/producto/{id}` - Eliminar producto

### 🛒 Venta Controller (`/api/venta`)
- **GET** `/api/venta` - Obtener todas las ventas
- **GET** `/api/venta/{id}` - Obtener venta por ID
- **POST** `/api/venta` - Registrar nueva venta (con detalles)
- **PUT** `/api/venta/{id}` - Actualizar venta
- **DELETE** `/api/venta/{id}` - Eliminar venta

### 📋 VentaDetalle Controller (`/api/ventadetalle`)
- **GET** `/api/ventadetalle` - Obtener todos los detalles
- **GET** `/api/ventadetalle/{id}` - Obtener detalle por ID
- **GET** `/api/ventadetalle/detalle/{idventa}` - Obtener detalles por venta
- **POST** `/api/ventadetalle` - Crear detalle
- **DELETE** `/api/ventadetalle/{id}` - Eliminar detalle

## 🔄 Flujos de Trabajo

### 1. Gestión de Clientes con RENIEC

```mermaid
graph TD
    A[Usuario ingresa DNI] --> B[GET /api/cliente/dni/{dni}]
    B --> C{¿Existe en BD local?}
    C -->|Sí| D[Retornar cliente]
    C -->|No| E{¿Existe en cache?}
    E -->|Sí| F[Guardar en BD y retornar]
    E -->|No| G[Consultar RENIEC]
    G --> H{¿Encontrado en RENIEC?}
    H -->|Sí| I[Crear cliente desde RENIEC]
    H -->|No| J[Solicitar registro manual]
```

### 2. Registro de Ventas

```json
// POST /api/venta
{
  "idcliente": 1,
  "fechaRegistro": "2025-07-21",
  "precioTotal": 150.50,
  "detalles": [
    {"idproducto": 1, "cantidad": 2},
    {"idproducto": 5, "cantidad": 1}
  ]
}
```

El sistema automáticamente:
1. Registra la venta
2. Guarda los detalles
3. Actualiza el inventario de productos

## 🧪 Ejemplos de Uso

### Buscar Cliente por DNI
```bash
curl -X GET "http://localhost:8010/api/cliente/dni/12345678"
```

### Registrar Cliente Manual
```bash
curl -X POST "http://localhost:8010/api/cliente/manual" \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "87654321",
    "nombres": "María Elena",
    "apellidoPaterno": "Rodríguez",
    "apellidoMaterno": "López",
    "telefono": "987654321"
  }'
```

### Buscar Productos
```bash
curl -X GET "http://localhost:8010/api/producto/buscar/nombre?nombre=paracetamol"
```

### Registrar Venta
```bash
curl -X POST "http://localhost:8010/api/venta" \
  -H "Content-Type: application/json" \
  -d '{
    "idcliente": 1,
    "fechaRegistro": "2025-07-21",
    "precioTotal": 33.50,
    "detalles": [
      {"idproducto": 1, "cantidad": 2},
      {"idproducto": 5, "cantidad": 1}
    ]
  }'
```

## 🔧 Configuración Avanzada

### RENIEC API Configuration
```properties
# application.properties
reniec.api.base-url=https://apis.net.pe/app/
reniec.api.token=apis-token-17053.mxIHmVUUuG3VuHanW4iP0PA5xNKN0JEY
reniec.api.timeout=5000
```

### CORS Configuration
```properties
spring.web.cors.allowed-origins=http://localhost:5173
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

## 📊 Base de Datos

### Entidades Principales

1. **Cliente**: Gestión de clientes con integración RENIEC
2. **Producto**: Inventario de medicamentos y productos
3. **Venta**: Registro de transacciones
4. **VentaDetalle**: Detalles de cada venta

### Campos Importantes

#### Cliente
- `dni`: Documento único (8 dígitos)
- `fuenteDatos`: "RENIEC" o "MANUAL"
- `fechaRegistro`: Timestamp de creación

#### Producto
- `cantidad`: Stock disponible
- `fechaVencimiento`: Control de caducidad
- `categoria`: Clasificación del producto

## 🧩 Funcionalidades Avanzadas

### Sistema de Cache Inteligente
- **Duración**: 30 minutos para consultas RENIEC
- **Auto-limpieza**: Eliminación automática de entradas expiradas
- **Estadísticas**: Endpoint para monitoreo del cache

### Validaciones
- **DNI**: Formato peruano (8 dígitos)
- **Inventario**: Control automático de stock
- **Transacciones**: Operaciones atómicas para ventas

### Logging
- Consultas RENIEC registradas en consola
- Estados de cache monitoreados
- Errores de conexión manejados graciosamente

## 🚀 Próximas Funcionalidades

1. **Autenticación JWT**
2. **Reportes en PDF**
3. **Notificaciones de stock bajo**
4. **Dashboard de analytics**
5. **Integración con facturación electrónica**

## 📞 Soporte

Para dudas o soporte técnico, revisar:
- Logs de aplicación en consola
- Estado de conexión a MySQL
- Configuración de API RENIEC
- Endpoints de health check

---

**Desarrollado con ❤️ usando Spring Boot**
