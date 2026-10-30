Aquí tienes una propuesta completa y estructurada para actualizar el archivo **`README.md`** de tu repositorio:

```markdown
# RutaMacht - Sistema de Gestión de Flota y Viajes[cite: 2]

**RutaMacht** es una plataforma web *Full-Stack* diseñada para la administración integral de conductores, vehículos de flota y pasajeros[cite: 2]. La arquitectura del proyecto es desacoplada, contando con un backend robusto en Java Spring Boot que expone servicios REST y un panel de administración cliente desarrollado en Angular 17[cite: 2].

---

## 🚀 Tecnologías Utilizadas

### Backend
* **Lenguaje:** Java 17+[cite: 2]
* **Framework:** Spring Boot[cite: 2]
* **Gestor de Dependencias:** Maven Wrapper (`mvnw`)[cite: 2]
* **Persistencia de Datos:** Spring Data JPA[cite: 2]
* **Clientes HTTP Internos:** `RestClient` para consumo de microservicios externos[cite: 2]

### Frontend
* **Framework:** Angular 17[cite: 2]
* **Entorno de Compilación:** Vite / esbuild[cite: 2]
* **Lenguaje:** TypeScript[cite: 2]
* **Interfaz de Usuario:** HTML5, CSS3 y componentes modulares[cite: 2]

---

## 📁 Estructura del Proyecto

```text
APIRutaMacht/
├── API REST_ Rendpoints y m todos HTTP/   # Manual y especificación técnica de la API REST (PDF)[cite: 2]
├── src/
│   ├── main/
│   │   ├── java/com/RutaMacht/RutaMacht/
│   │   │   ├── client/       # Clientes REST externos (PasajeroClient)[cite: 2]
│   │   │   ├── config/       # Configuración de CORS y RestClient[cite: 2]
│   │   │   ├── controller/   # Controladores REST (Conductores, Vehículos, Pasajeros, Status)[cite: 2]
│   │   │   ├── dto/          # Objetos de transferencia de datos (PasajeroDTO, viajeRequestDTO)[cite: 2]
│   │   │   ├── model/        # Entidades de dominio (Persona, Conductor, Vehiculo)[cite: 2]
│   │   │   ├── repository/   # Interfaces de persistencia JPA[cite: 2]
│   │   │   └── services/     # Lógica de negocio (ConductorServicie, VehiculoServicio)[cite: 2]
│   │   └── resources/        # Configuración de propiedades (application.properties)[cite: 2]
│   └── angular-app/          # Aplicación Frontend en Angular (rutamacht-panel)[cite: 2]
│       └── src/app/
│           ├── components/   # Módulos de interfaz (conductores, pasajeros, vehículos)[cite: 2]
│           ├── models/       # Modelos e interfaces en TypeScript[cite: 2]
│           └── services/     # Servicios para comunicación HTTP y notificaciones Toast[cite: 2]
├── pom.xml                   # Configuración del proyecto Maven[cite: 2]
└── mvnw / mvnw.cmd           # Ejecutables portátiles de Maven[cite: 2]

```

---

## ⚙️ Requisitos e Instalación

### Prerrequisitos

* Java Development Kit (JDK) 17 o superior.


* Node.js v18+ y npm instalados.


* Angular CLI instalado de forma global (`npm install -g @angular/cli`).

---

## 🛠️ Instrucciones de Ejecución

### 1. Iniciar el Backend (Spring Boot)

1. Posiciónate en la raíz del proyecto:


```bash
cd APIRutaMacht

```


2. Ejecuta el servidor mediante el ejecutable de Maven:


* En Linux/macOS:
```bash
./mvnw spring-boot:run

```


* En Windows:
```cmd
mvnw.cmd spring-boot:run

```




3. La API estará disponible en el puerto especificado en `application.properties`.



### 2. Iniciar el Frontend (Angular)

1. Navega al directorio de la aplicación Angular:


```bash
cd src/angular-app

```


2. Instala las dependencias:


```bash
npm install

```


3. Inicia el servidor de desarrollo:


```bash
ng serve

```


4. Accede a la aplicación desde tu navegador en `http://localhost:4200`.



---

## 📖 Documentación de la API

La especificación completa de las rutas, métodos HTTP (GET, POST, PUT, DELETE) y contratos de datos se encuentra detallada en la guía [API REST_ Rendpoints y m todos HTTP/endpoints y m todos HTTP.pdf](https://www.google.com/search?q=API%2520REST_%2520Rendpoints%2520y%2520m%2520todos%2520HTTP/endpoints%2520y%2520m%2520todos%2520HTTP.pdf).

```

```
