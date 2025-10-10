#  WMS Backend E-Commerce

Este es el servicio de gestión de inventario para el E-Commerce, desarrollado en Spring Boot con Java. Proporciona endpoints
REST para la gestión del sistema.

## Autores

**WMS** – (Backend developer)

- **W**ilson Martín Cabrera Juárez
- **M**elany Belen Ambrocio Nelson
- **S**ebastian Rocop Quemé

## Tecnologías usadas

- Java 17 (o superior)
- Spring Boot 3.5.3
- Gradle 8.14.3
- MySQL (MariaDB)
- Springdoc OpenAPI (Swagger)
- JUnit 5 + Mockito para tests
- Docker

## Configuración

Para que se inicie el proyecto, es necesario crear el archivo `.env` que contendrá todas las variables de entornos (volátiles) 
que se usarán durante la ejecución del proyecto.

Vea el fichero [.env.sample](./.env.sample), el cual contiene un ejemplo de las variables utilizadas por WMS-Backend, asegúrese de exportarlo 
en la sesión de la API.

### Ejecutar la aplicación

Para compilar y ejecutar WMS-Backend, desde la línea de comandos, se usa la siguiente instrucción:

```shell
$ ./gradlew bootRun
```

### Docker

Construir esta API mediante docker.

1. Limpiar
```shell
$ ./gradlew clean
```

2. Construir
```shell
$ ./gradlew bootJar
```

3. Dockerizar
```shell
$ docker compose up -d --build
```

### Prerrequisitos

- Java 17
- Gradle 8.14.3
- Base de datos MySQL
- IDE utilizado: IntelliJ IDEA ultimate o Apache NetBeans

## Documentación API Swagger

apiurl + /swagger-ui/index.html
