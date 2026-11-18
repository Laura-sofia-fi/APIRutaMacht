# RutaMacht – Sistema de Gestión de Flota y Viajes

**RutaMacht** es una plataforma *full-stack* para administrar conductores, vehículos, viajes, reservas y pasajeros.
El backend es una API REST en **Spring Boot** con persistencia en **MongoDB**, documentada con **Swagger UI**, y el
panel de administración es una aplicación **Angular**. Los pasajeros se consumen desde un microservicio independiente
mediante `RestClient`.

## Tecnologías

**Backend:** Java 21, Spring Boot 4, Spring Data MongoDB, Bean Validation, springdoc-openapi (Swagger UI), Lombok,
`RestClient` (comunicación entre microservicios), Maven Wrapper.

**Frontend:** Angular 22, TypeScript, HTML5/CSS3.

## Estructura

```text
APIRutaMacht/
├── src/main/java/com/RutaMacht/RutaMacht/
│   ├── client/       # Cliente REST hacia el microservicio de pasajeros
│   ├── config/       # CORS, RestClient y OpenAPI (Swagger)
│   ├── controller/   # Controladores REST
│   ├── dto/          # Objetos de transferencia de datos
│   ├── model/        # Documentos de MongoDB
│   ├── repository/   # Repositorios Spring Data MongoDB
│   └── services/     # Lógica de negocio
├── src/main/resources/application.properties
├── src/angular-app/  # Panel de administración (Angular)
├── pom.xml
└── mvnw / mvnw.cmd
```

## Requisitos

- JDK 21
- MongoDB en ejecución (por defecto `mongodb://localhost:27017/rutamacht`)
- Node.js 20+ y npm
- (Opcional) Microservicio de pasajeros en `http://localhost:8082`

## Configuración

Variables de entorno opcionales:

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `RUTAMACHT_MONGODB_URI` | URI de conexión a MongoDB | `mongodb://localhost:27017/rutamacht` |
| `API_PASAJEROS_URL` | URL del microservicio de pasajeros | `http://localhost:8082` |
| `GOOGLE_CLIENT_ID` | Client ID de Google (login para reservas) | *(vacío)* |

## Ejecución

### Backend (puerto 8081)

```bash
# Linux / macOS
./mvnw spring-boot:run
# Windows
mvnw.cmd spring-boot:run
```

### Frontend (puerto 4200)

```bash
cd src/angular-app
npm install
npm start
```

Abrir `http://localhost:4200`.

## Documentación de la API (Swagger)

Con el backend en ejecución:

- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **Especificación OpenAPI (JSON):** http://localhost:8081/v3/api-docs

## Endpoints principales

| Recurso | Método | Ruta | Descripción |
|---|---|---|---|
| Conductores | POST | `/api/conductor` | Crear |
| | GET | `/api/conductor` · `/api/conductor/{id}` | Listar / consultar |
| | PUT | `/api/conductor/{id}` | Actualizar |
| | DELETE | `/api/conductor/{id}` | Eliminar |
| Vehículos | POST | `/api/vehiculo` | Crear |
| | GET | `/api/vehiculo` · `/api/vehiculo/{id}` · `/api/vehiculo/placa/{placa}` | Listar / consultar |
| | PUT | `/api/vehiculo/{id}` | Actualizar |
| | DELETE | `/api/vehiculo/{id}` | Eliminar |
| Viajes | POST | `/api/viajes` | Publicar |
| | GET | `/api/viajes` · `/api/viajes/todos` · `/api/viajes/{id}` | Buscar programados / listar todos / consultar |
| | PUT | `/api/viajes/{id}` | Actualizar |
| | PATCH | `/api/viajes/{id}/cancelar` | Cancelar |
| | DELETE | `/api/viajes/{id}` | Eliminar |
| Pasajeros (microservicio) | POST / GET / PUT / DELETE | `/api/pasajero[/{id}]` | CRUD reenviado al microservicio 8082 |
| Reservas (Google) | POST | `/api/viajes/{viajeId}/reservas` | Reservar cupo |
| | GET | `/api/reservas/mis` | Mis reservas |
| | DELETE | `/api/reservas/{reservaId}` | Cancelar reserva |
| Utilidades | GET | `/api/status` · `/api/conexion/pasajero` | Estado de la API / del microservicio |
