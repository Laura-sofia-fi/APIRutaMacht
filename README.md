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
