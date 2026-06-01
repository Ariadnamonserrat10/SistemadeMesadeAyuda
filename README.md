# Sistema de Mesa de Ayuda

Backend REST construido con Spring Boot 3, Java 21 y Maven para la gestion academica de una mesa de ayuda.

## Tecnologias

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Bean Validation
- Lombok
- OpenAPI Swagger
- JUnit 5
- Mockito

## Arquitectura

El proyecto sigue una separacion por capas:

- controller: expone la API REST.
- service: contiene reglas de negocio.
- repository: acceso a datos con Spring Data JPA.
- dto: contratos de entrada y salida.
- entity: modelo persistente.
- mapper: conversion entre entidades y DTOs.
- exception: manejo centralizado de errores.
- config: OpenAPI y carga inicial de datos.

## Ejecucion local

### Compilar

./mvnw clean install

### Ejecutar

./mvnw spring-boot:run

### Probar

./mvnw test

## URLs utiles

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Consola H2: http://localhost:8080/h2-console

Credenciales H2:

- JDBC URL: jdbc:h2:mem:mesadeayuda
- User: sa
- Password: vacio

## Datos iniciales

Al arrancar la aplicacion se cargan automaticamente:

- 10 usuarios
- 50 tickets
- 15 tickets resueltos
- 15 tickets en proceso
- 20 tickets pendientes
- 15 soluciones para tickets resueltos

## Endpoints

### Usuarios

- GET /api/usuarios
- GET /api/usuarios/{id}
- POST /api/usuarios
- PUT /api/usuarios/{id}
- DELETE /api/usuarios/{id}

### Tickets

- GET /api/tickets
- GET /api/tickets/detallado
- GET /api/tickets/{id}
- POST /api/tickets
- PUT /api/tickets/{id}
- DELETE /api/tickets/{id}
- PUT /api/tickets/{id}/en-proceso
- PUT /api/tickets/{id}/resolver

### Dashboard

- GET /api/dashboard

## Ejemplos de requests y responses

### Crear usuario

Request:

```json
{
  "nombre": "Laura Gomez",
  "correo": "laura.gomez@helpdesk.local",
  "departamento": "Soporte",
  "activo": true
}
```

Response 201:

```json
{
  "id": 11,
  "nombre": "Laura Gomez",
  "correo": "laura.gomez@helpdesk.local",
  "departamento": "Soporte",
  "activo": true
}
```

### Crear ticket

Request:

```json
{
  "titulo": "No puedo acceder al portal",
  "descripcion": "El sistema indica credenciales invalidas",
  "prioridad": "ALTA",
  "usuarioAsignadoId": 1
}
```

Response 201:

```json
{
  "id": 51,
  "titulo": "No puedo acceder al portal",
  "descripcion": "El sistema indica credenciales invalidas",
  "prioridad": "ALTA",
  "estado": "PENDIENTE",
  "fechaCreacion": "2026-05-31T15:00:00",
  "usuarioAsignadoId": 1
}
```

### Cambiar ticket a en proceso

Request:

```json
{
  "nuevoResponsableId": 3
}
```

Response 200:

```json
{
  "id": 5,
  "titulo": "No puedo iniciar sesion #5",
  "descripcion": "Detalle del incidente 5 generado automaticamente para ambiente inicial",
  "prioridad": "MEDIA",
  "estado": "EN_PROCESO",
  "fechaCreacion": "2026-05-21T10:00:00",
  "usuarioAsignadoId": 3
}
```

### Resolver ticket

Request:

```json
{
  "descripcionSolucion": "Se reinstalo el controlador",
  "tiempoInvertidoHoras": 2
}
```

Response 200:

```json
{
  "id": 16,
  "descripcionSolucion": "Se reinstalo el controlador",
  "fechaSolucion": "2026-05-31T15:05:00",
  "tiempoInvertidoHoras": 2,
  "ticketId": 31
}
```

### Listado resumido de tickets

Response 200:

```json
[
  {
    "id": 1,
    "titulo": "No puedo iniciar sesion #1",
    "usuarioAsignadoId": 2
  }
]
```

### Listado detallado de tickets

Response 200:

```json
[
  {
    "id": 1,
    "titulo": "No puedo iniciar sesion #1",
    "estado": "RESUELTO",
    "usuarioAsignado": {
      "id": 2,
      "nombre": "Usuario 2"
    }
  }
]
```

### Dashboard

Response 200:

```json
{
  "totalTickets": 50,
  "pendientes": 20,
  "enProceso": 15,
  "resueltos": 15,
  "criticos": 12
}
```

### Error de validacion

Response 400:

```json
{
  "timestamp": "2026-05-31T15:10:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La solicitud contiene errores de validacion",
  "path": "/api/tickets",
  "validationErrors": {
    "titulo": "El titulo es obligatorio"
  }
}
```

## Reglas de negocio implementadas

- Un ticket solo se crea si el usuario asignado existe y esta activo.
- Un ticket solo se resuelve si esta en estado EN_PROCESO.
- Al resolver un ticket se crea una solucion y el ticket cambia a RESUELTO.
- Un usuario no se puede eliminar si tiene tickets asignados.
- El correo del usuario debe ser unico.

## Cobertura y pruebas

Se incluyeron pruebas unitarias para:

- UsuarioService
- TicketService
- DashboardService

La construccion ejecuta JaCoCo en fase verify con un minimo de cobertura del 80%.

## Validacion sugerida manual

1. Ejecutar ./mvnw clean install
2. Levantar la app con ./mvnw spring-boot:run
3. Abrir Swagger UI
4. Ejecutar operaciones CRUD y cambios de estado desde Swagger o curl
