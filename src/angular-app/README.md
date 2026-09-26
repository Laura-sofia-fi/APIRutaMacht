# RutaMacht · Panel (Angular)

Panel de administración para la API de RutaMacht: CRUD de conductores y
vehículos, más indicador de estado del servicio.

## Cómo correrlo

```bash
npm install
npm start
```

Se abre en `http://localhost:4200`.

## Configurar la URL de la API

La URL base de la API está en `src/app/api-base.ts`:

```ts
export const API_BASE = 'http://localhost:8080';
```

Cámbiala si tu backend de Spring Boot corre en otro host o puerto.

## Estructura

- `src/app/models` — interfaces `Conductor` y `Vehiculo`.
- `src/app/services` — `ConductorService`, `VehiculoService`, `StatusService`
  (llamadas HTTP) y `ToastService` (notificaciones).
- `src/app/components/conductores` — listado + modal crear/editar.
- `src/app/components/vehiculos` — listado + modal crear/editar.
- `src/app/app.component.*` — layout con pestañas y estado de la API.
- `src/styles.css` — paleta café/verde y tipografía Times New Roman 12px.
