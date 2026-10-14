# API Apartamentos

API REST para rentar departamentos y habitaciones. Maneja clientes, propiedades, imagenes de las propiedades, unidades rentables (por ejemplo cada habitacion de un departamento), disponibilidades, reservas, pagos y resenas.

Reglas principales:

- Una unidad no se puede reservar si ya tiene una reserva `CONFIRMADA`.
- La fecha de salida de una reserva tiene que ser despues de la de entrada.
- El importe de un pago tiene que ser mayor a 0 y no puede pasar del total de la reserva.
- La calificacion de una resena va de 1 a 5.
- No se puede eliminar un registro que tenga otros registros relacionados (por ejemplo un cliente con reservas o una propiedad con unidades).

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Spring Web y Spring Data JPA)
- MySQL 8
- Maven (el proyecto trae `mvnw`, no hace falta instalarlo)

## Instalacion y ejecucion

1. Tener instalado Java 21 y MySQL 8 corriendo en el puerto 3306.
2. Clonar el repositorio y entrar a la carpeta del proyecto.
3. Configurar las variables de entorno de la base de datos (ver la siguiente seccion).
4. Ejecutar:

```
mvnw.cmd spring-boot:run
```

Tambien se puede correr desde el IDE ejecutando la clase `ApartamentosApplication`.

La API queda en `http://localhost:8082/api`. La base de datos `rentas` y sus tablas se crean solas al arrancar.

## Variables de entorno

| Variable | Descripcion | Valor por defecto |
|---|---|---|
| `DB_URL` | URL de conexion a MySQL | `jdbc:mysql://localhost:3306/rentas?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contrasena de MySQL | vacia |
| `PORT` | Puerto de la API | `8082` |

Ejemplo en la terminal antes de ejecutar:

```
set DB_PASSWORD=tu_contrasena
```

## Endpoints

Todos los recursos tienen las mismas operaciones:

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/api/{recurso}` | Lista todos |
| GET | `/api/{recurso}/{id}` | Busca uno por id |
| POST | `/api/{recurso}` | Crea un registro |
| PUT | `/api/{recurso}/{id}` | Edita un registro |
| DELETE | `/api/{recurso}/{id}` | Elimina un registro |

| Recurso | Que guarda |
|---|---|
| `clientes` | Huespedes y propietarios |
| `propiedades` | Departamentos, casas, etc. Cada una tiene un propietario |
| `imagenes` | Fotos de una propiedad |
| `unidades-rentables` | Lo que se renta dentro de una propiedad (habitacion, departamento completo) |
| `disponibilidades` | Rangos de fechas en que una unidad esta disponible |
| `reservas` | Reserva de una unidad por un cliente |
| `pagos` | Pagos de una reserva |
| `resenas` | Calificacion y comentario de una reserva |

Endpoint extra:

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/api/propiedades/tipo/{tipo}` | Busca propiedades por tipo, por ejemplo `APARTAMENTO` |

## Ejemplos

### Registrar un cliente

`POST http://localhost:8082/api/clientes`

```json
{
  "nombres": "Luis",
  "apellidos": "Mendez Ortiz",
  "email": "luis.mendez@mail.com",
  "telefono": "2229876543",
  "tipo": "PROPIETARIO"
}
```

Respuesta `201 Created`

```json
{
  "apellidos": "Mendez Ortiz",
  "email": "luis.mendez@mail.com",
  "idCliente": 1,
  "nombres": "Luis",
  "telefono": "2229876543",
  "tipo": "PROPIETARIO"
}
```

### Registrar una propiedad

`POST http://localhost:8082/api/propiedades`

```json
{
  "titulo": "Departamento en el centro",
  "descripcion": "Dos recamaras cerca del zocalo",
  "tipo": "APARTAMENTO",
  "direccion": "Calle 5 de Mayo 210",
  "ciudad": "Puebla",
  "capacidad": 4,
  "precioNoche": 850,
  "propietario": { "idCliente": 1 }
}
```

Respuesta `201 Created`

```json
{
  "capacidad": 4,
  "ciudad": "Puebla",
  "descripcion": "Dos recamaras cerca del zocalo",
  "direccion": "Calle 5 de Mayo 210",
  "estado": "DISPONIBLE",
  "idPropiedad": 1,
  "precioNoche": 850.0,
  "propietario": {
    "apellidos": "Mendez Ortiz",
    "email": "luis.mendez@mail.com",
    "idCliente": 1,
    "nombres": "Luis",
    "telefono": "2229876543",
    "tipo": "PROPIETARIO"
  },
  "tipo": "APARTAMENTO",
  "titulo": "Departamento en el centro"
}
```

### Registrar una unidad rentable

`POST http://localhost:8082/api/unidades-rentables`

```json
{
  "nombre": "Recamara principal",
  "tipo": "HABITACION",
  "capacidad": 2,
  "precioNoche": 450,
  "propiedad": { "idPropiedad": 1 }
}
```

Respuesta `201 Created` con la unidad y su propiedad.

### Hacer una reserva

`POST http://localhost:8082/api/reservas`

```json
{
  "cliente": { "idCliente": 2 },
  "unidad": { "idUnidad": 1 },
  "fechaEntrada": "2026-11-01",
  "fechaSalida": "2026-11-03",
  "importeTotal": 900
}
```

Respuesta `201 Created`

```json
{
  "cliente": {
    "apellidos": "Diaz",
    "email": "carla.diaz@mail.com",
    "idCliente": 2,
    "nombres": "Carla",
    "telefono": "2225551234",
    "tipo": "CLIENTE"
  },
  "comentario": null,
  "estado": "PENDIENTE",
  "fechaEntrada": "2026-11-01",
  "fechaSalida": "2026-11-03",
  "idReserva": 1,
  "importeTotal": 900.0,
  "unidad": {
    "capacidad": 2,
    "idUnidad": 1,
    "nombre": "Recamara principal",
    "precioNoche": 450.0,
    "propiedad": {
      "capacidad": 4,
      "ciudad": "Puebla",
      "descripcion": "Dos recamaras cerca del zocalo",
      "direccion": "Calle 5 de Mayo 210",
      "estado": "DISPONIBLE",
      "idPropiedad": 1,
      "precioNoche": 850.0,
      "propietario": {
        "apellidos": "Mendez Ortiz",
        "email": "luis.mendez@mail.com",
        "idCliente": 1,
        "nombres": "Luis",
        "telefono": "2229876543",
        "tipo": "PROPIETARIO"
      },
      "tipo": "APARTAMENTO",
      "titulo": "Departamento en el centro"
    },
    "tipo": "HABITACION"
  }
}
```

### Confirmar la reserva

`PUT http://localhost:8082/api/reservas/1`

```json
{
  "fechaEntrada": "2026-11-01",
  "fechaSalida": "2026-11-03",
  "importeTotal": 900,
  "estado": "CONFIRMADA"
}
```

Respuesta `200 OK` con la reserva y `"estado": "CONFIRMADA"`.

### Reservar una unidad que ya esta confirmada

`POST http://localhost:8082/api/reservas`

```json
{
  "cliente": { "idCliente": 2 },
  "unidad": { "idUnidad": 1 },
  "fechaEntrada": "2026-11-10",
  "fechaSalida": "2026-11-12",
  "importeTotal": 900
}
```

Respuesta `400 Bad Request`

```
La unidad ya tiene una reserva confirmada
```

### Pago mayor al total de la reserva

`POST http://localhost:8082/api/pagos`

```json
{
  "reserva": { "idReserva": 1 },
  "importe": 1200,
  "metodo": "TARJETA"
}
```

Respuesta `400 Bad Request`

```
El importe debe ser mayor a 0 y no pasar del total de la reserva
```

### Registrar un pago

`POST http://localhost:8082/api/pagos`

```json
{
  "reserva": { "idReserva": 1 },
  "importe": 900,
  "metodo": "TARJETA"
}
```

Respuesta `201 Created` con `"estado": "PENDIENTE"`, la fecha del dia y la reserva.

### Registrar una resena

`POST http://localhost:8082/api/resenas`

```json
{
  "reserva": { "idReserva": 1 },
  "calificacion": 5,
  "comentario": "Muy limpio y bien ubicado"
}
```

Respuesta `201 Created` con la resena y su reserva. Si la calificacion no esta entre 1 y 5 responde `400 Bad Request` con `La calificacion va de 1 a 5`.

### Eliminar un cliente con reservas

`DELETE http://localhost:8082/api/clientes/2`

Respuesta `400 Bad Request`

```
No se puede borrar, el cliente tiene reservas o propiedades
```

### Buscar una propiedad que no existe

`GET http://localhost:8082/api/propiedades/99`

Respuesta `404 Not Found`

```
No existe la propiedad
```
