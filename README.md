# API ListMe

> Backend REST de la aplicación ListMe — Spring Boot 3 · Java 21 · PostgreSQL

API RESTful que gestiona toda la lógica de negocio de ListMe: autenticación, listas, ítems, atributos personalizados, imágenes, sistema de amistades e invitaciones a listas compartidas.

---

## Tecnologías

| Capa | Tecnología |
|------|-----------|
| Framework | Spring Boot 3.4 |
| Lenguaje | Java 21 |
| Persistencia | Spring Data JPA / Hibernate 6 |
| Base de datos | PostgreSQL 16 |
| Seguridad | Spring Security · JWT (jjwt 0.12) |
| Almacenamiento de imágenes | Firebase Storage (Admin SDK 9.2) |
| Documentación API | SpringDoc OpenAPI (Swagger UI) |
| Build | Gradle 8 |
| Despliegue | Docker Compose |

---

## Requisitos previos

- **Java 21+**
- **Docker** y **Docker Compose**
- Proyecto en **Firebase** con Storage habilitado y clave de servicio descargada

---

## Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/Angelery33/API_Listme.git
cd API_Listme
```

### 2. Credenciales de Firebase Admin SDK

El backend necesita el archivo `firebase-adminsdk.json` para subir imágenes a Firebase Storage. Sin él la aplicación no arranca.

1. Ir a [Firebase Console](https://console.firebase.google.com) → Configuración del proyecto → **Cuentas de servicio**.
2. Pulsar **Generar nueva clave privada** y descargar el JSON.
3. Guardarlo en `src/main/resources/firebase-adminsdk.json`.

> ⚠️ Este archivo está en `.gitignore`. No debe subirse nunca a control de versiones.

### 3. Variables de entorno

```bash
# Linux/macOS
cp .env.example .env
# Windows
copy .env.example .env
```

Editar `.env` y rellenar los valores necesarios:

| Variable | Descripción |
|----------|-------------|
| `LISTME_DB_USERNAME` | Usuario de PostgreSQL |
| `LISTME_DB_PASSWORD` | Contraseña de PostgreSQL |
| `LISTME_JWT_SECRET` | Cadena hex de mínimo 64 caracteres (`openssl rand -hex 32`) |
| `LISTME_CORS_ORIGINS` | Orígenes permitidos separados por coma |
| `LISTME_BOOTSTRAP_USERNAME` | Usuario admin inicial (opcional) |
| `LISTME_BOOTSTRAP_PASSWORD` | Contraseña admin inicial (opcional) |
| `LISTME_TMDB_KEY` | Clave TMDb (opcional, para importación de películas/series) |
| `LISTME_BOOKS_KEY` | Clave Google Books (opcional, para importación de libros) |

---

## Arrancar con Docker Compose

```bash
# Construir la imagen de la API
docker build -t listme:latest .

# Levantar PostgreSQL y el backend
docker-compose up -d
```

Esto levanta dos contenedores:

- **listme** — la API en el puerto `8089`
- **listme-db** — PostgreSQL en el puerto `5433`

La API queda disponible en `http://localhost:8089/api/v1`.

La documentación interactiva (**Swagger UI**) está accesible en:

```
http://localhost:8089/swagger-ui/index.html
```

---

## Estructura del proyecto

```
src/main/java/com/angelcantero/listme/
├── config/         # Seguridad (JWT, CORS), Firebase, Swagger
├── controller/     # Endpoints REST
├── dto/            # Objetos de transferencia de datos
├── exception/      # Manejo global de errores (@RestControllerAdvice)
├── model/          # Entidades JPA
├── repository/     # Interfaces Spring Data JPA
├── service/        # Lógica de negocio
└── util/           # Utilidades
```

---

## Endpoints principales

| Recurso | Prefijo |
|---------|---------|
| Autenticación | `/api/v1/auth` |
| Listas | `/api/v1/libraries` |
| Géneros de lista | `/api/v1/library-genres` |
| Ítems | `/api/v1/items` |
| Imágenes de ítems | `/api/v1/images` |
| Tipos de atributo | `/api/v1/attribute-types` |
| Valores de atributo | `/api/v1/attribute-items` |
| Amistades | `/api/v1/friends` |
| Invitaciones | `/api/v1/invitations` |
| Búsqueda externa (proxy) | `/api/v1/search` |
| Proxy de imágenes externas | `/api/v1/proxy` |

La especificación completa está disponible en Swagger UI una vez arrancado el servidor.

---

## Cliente Flutter

La aplicación cliente que consume esta API está disponible en:
[ListMe Flutter](https://github.com/Angelery33/ListMe_Flutter)

---

## Licencia

Este proyecto es de uso académico y personal. Todos los derechos reservados © 2025 Angel Cantero.
