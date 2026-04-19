# 🐾 Sistema de Adopciones de Mascotas

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Educational-yellow.svg)](#licencia)

Una aplicación web **Spring Boot + JSP** para gestionar de forma integral el proceso de adopción de mascotas. Permite registrar mascotas disponibles, solicitar adopciones, validar solicitantes y realizar seguimiento de adopciones exitosas.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías](#tecnologías)
- [Configuración](#configuración)
- [Licencia](#licencia)

## ✨ Características

- ✅ **Gestión de Mascotas**: Registro, edición y eliminación de mascotas disponibles
- ✅ **Formulario de Solicitud**: Proceso simplificado para solicitar adopciones
- ✅ **Validación de Solicitantes**: Verificación de información de adoptantes
- ✅ **Seguimiento de Adopciones**: Historial y estado de cada adopción
- ✅ **Dashboard Administrativo**: Panel de control para administradores
- ✅ **Búsqueda y Filtros**: Funcionalidad para encontrar mascotas

## 📋 Requisitos

- **Java**: 22 o superior
- **Maven**: 3.6 o superior
- **Base de Datos**: MySQL 8.0+ o H2 (en memoria)
- **Docker**: Opcional

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/ErickCaiced2/GR01_1BT3_622_26A.git
cd GR01_1BT3_622_26A
```

### 2. Configurar Base de Datos

#### Opción A: Docker Compose (Recomendado)

```bash
docker-compose up -d
```

#### Opción B: MySQL Local

```sql
CREATE DATABASE adopciones_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'adopciones_user'@'localhost' IDENTIFIED BY 'adopciones_pass';
GRANT ALL PRIVILEGES ON adopciones_db.* TO 'adopciones_user'@'localhost';
FLUSH PRIVILEGES;
```

#### Opción C: H2 (Sin Instalación)

La aplicación usa H2 por defecto para desarrollo.

### 3. Compilar

```bash
mvn clean install
```

### 4. Ejecutar

```bash
# Con Maven
mvn spring-boot:run

# O con JAR
java -jar target/GR01_1BT3_622_26A-0.0.1-SNAPSHOT.jar
```

**URL**: http://localhost:8080

## 📁 Estructura del Proyecto

```
src/
├── main/java/com/example/gr01_1bt3_622_26a/
│   ├── controller/        # Controladores MVC
│   │   ├── AdopcionController.java
│   │   ├── MascotaController.java
│   │   ├── SolicitanteController.java
│   │   └── SolicitudController.java
│   ├── entity/            # Entidades JPA
│   │   ├── Adopcion.java
│   │   ├── Mascota.java
│   │   ├── Solicitante.java
│   │   ├── Solicitud.java
│   │   └── Foto.java
│   ├── repository/        # Repositorios
│   │   ├── AdopcionRepository.java
│   │   ├── MascotaRepository.java
│   │   ├── SolicitanteRepository.java
│   │   ├── SolicitudRepository.java
│   │   └── FotoRepository.java
│   └── service/           # Servicios
│       ├── AdopcionService.java
│       ├── MascotaService.java
│       ├── SolicitanteService.java
│       └── SolicitudService.java
├── main/resources/
│   ├── application.properties
│   ├── schema-mysql.sql
│   └── init-database.sql
└── webapp/WEB-INF/jsp/    # Vistas JSP
    ├── mascotas/
    ├── solicitudes/
    ├── adopciones/
    └── admin/
```

## 🛠️ Tecnologías

- **Spring Boot 4.0.5** - Framework principal
- **Spring Data JPA** - Acceso a datos
- **Spring Web MVC** - Controladores
- **MySQL 8.0** - Base de datos
- **H2 Database** - Desarrollo
- **JSP + JSTL** - Vistas
- **Project Lombok** - Utilidades
- **Maven** - Build tool

## ⚙️ Configuración

Edita `src/main/resources/application.properties`:

```properties
# Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/adopciones_db
spring.datasource.username=adopciones_user
spring.datasource.password=adopciones_pass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Servidor
server.port=8080

# Logging
logging.level.root=INFO
logging.level.com.example.gr01_1bt3_622_26a=DEBUG
```

## 📡 API Endpoints

### Mascotas
- `GET /mascotas` - Listar todas
- `GET /mascotas/{id}` - Obtener una
- `POST /mascotas` - Crear
- `PUT /mascotas/{id}` - Actualizar
- `DELETE /mascotas/{id}` - Eliminar

### Solicitudes
- `GET /solicitudes` - Listar todas
- `POST /solicitudes` - Crear
- `GET /solicitudes/{id}` - Detalle

### Adopciones
- `GET /adopciones` - Listar todas
- `POST /adopciones` - Crear

## 🤝 Contribuir

1. Fork el repositorio
2. Crea rama: `git checkout -b feature/miFeature`
3. Commit: `git commit -m 'Agrega miFeature'`
4. Push: `git push origin feature/miFeature`
5. Pull Request

## 📝 Licencia

Proyecto educativo GR01_1BT3_622_26A

---

**Autor**: Erick Caicedo  
**GitHub**: [@ErickCaiced2](https://github.com/ErickCaiced2)  
**Proyecto**: [GR01_1BT3_622_26A](https://github.com/ErickCaiced2/GR01_1BT3_622_26A)

**Última actualización**: Abril 2026
