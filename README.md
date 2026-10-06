```markdown
# RutaMacht 🚗

## Descripción

RutaMacht es una aplicación desarrollada en Java con Spring Boot para facilitar el transporte compartido entre personas que tienen destinos iguales o rutas similares.

El sistema permite gestionar conductores, pasajeros, personas y vehículos, buscando conectar usuarios que puedan compartir un recorrido.

El proyecto cuenta con un backend desarrollado en Spring Boot y una interfaz frontend desarrollada con Angular.

## Tecnologías utilizadas

- Java
- Spring Boot
- Maven
- Angular
- TypeScript
- HTML
- CSS
- Git y GitHub
- IntelliJ IDEA

## Estructura del proyecto

```text
RutaMacht/
│
├── .idea/
├── .mvn/
│
├── src/
│   ├── angular-app/
│   │   └── Aplicación frontend desarrollada con Angular
│   │
│   ├── main/
│   │   ├── java/
│   │   │   └── com.RutaMacht.RutaMacht/
│   │   │       ├── client/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── model/
│   │   │       │   ├── Conductor.java
│   │   │       │   ├── IActualizable.java
│   │   │       │   ├── Pasajero.java
│   │   │       │   ├── Persona.java
│   │   │       │   └── Vehiculo.java
│   │   │       ├── services/
│   │   │       └── view/
│   │   │
│   │   └── resources/
│   │
│   └── test/
│
├── target/
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
└── pom.xml
```

## Arquitectura

El proyecto está organizado por capas para separar las responsabilidades de cada componente:

### Model

Contiene las clases principales del sistema:

- `Persona`
- `Conductor`
- `Pasajero`
- `Vehiculo`
- `IActualizable`

### Controller

Contiene los controladores encargados de recibir y procesar las solicitudes HTTP de la aplicación.

### Services

Contiene la lógica de negocio de la aplicación.

### DTO

Contiene los objetos utilizados para transportar información entre las diferentes partes de la aplicación.

### Client

Contiene los componentes encargados de realizar comunicación con otros servicios.

### Config

Contiene las configuraciones necesarias para el funcionamiento de la aplicación.

### View

Contiene elementos relacionados con la presentación de la información.

### Angular App

Contiene la interfaz gráfica desarrollada con Angular, utilizada para interactuar con el backend.

## Requisitos

Para ejecutar el proyecto se necesita tener instalado:

- Java JDK
- Maven
- Node.js
- Angular CLI
- Git

## Ejecución del backend

Clonar el repositorio:

```bash
git clone https://github.com/Laura-sofia-fi/RutaMacht.git
```

Ingresar a la carpeta del proyecto:

```bash
cd RutaMacht
```

Ejecutar el proyecto utilizando Maven:

```bash
./mvnw spring-boot:run
```

En Windows también se puede utilizar:

```bash
mvnw.cmd spring-boot:run
```

## Ejecución del frontend

Ingresar a la carpeta de Angular:

```bash
cd src/angular-app
```

Instalar las dependencias:

```bash
npm install
```

Ejecutar la aplicación:

```bash
ng serve
```

Después de iniciar Angular, se puede acceder a la aplicación desde:

```text
http://localhost:4200
```

## Funcionamiento

RutaMacht busca facilitar el transporte compartido entre usuarios.

El sistema contempla principalmente:

- Gestión de conductores.
- Gestión de pasajeros.
- Gestión de vehículos.
- Comunicación entre los diferentes componentes del sistema.
- Consulta y gestión de información mediante servicios REST.
- Interfaz web desarrollada con Angular.

## API

El backend expone diferentes endpoints REST para gestionar los recursos de la aplicación.

Entre los principales recursos se encuentran:

- Conductores
- Pasajeros
- Vehículos

Los endpoints pueden ser probados utilizando herramientas como **Postman**.

## Control de versiones

El proyecto utiliza Git para el control de versiones y GitHub como repositorio remoto.

Repositorio:

`https://github.com/Laura-sofia-fi/RutaMacht.git`

## Autores

Proyecto desarrollado como parte del curso de Desarrollo de Software.

**Laura Carvajal** 
**Equipo RutaMacht**

## Estado del proyecto

🚧 Proyecto en desarrollo.
```
