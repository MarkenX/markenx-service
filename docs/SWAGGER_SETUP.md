# Swagger/OpenAPI Configuration

## Overview

La aplicación MarkenX Service ahora incluye Swagger/OpenAPI para documentación interactiva de la API y pruebas de endpoints.

## Acceso a Swagger UI

### URL Local (Docker)
Cuando la aplicación está corriendo en Docker con docker-compose.dev.yml:

```
http://localhost:8082/swagger-ui.html
```

O alternativamente:
```
http://localhost:8082/swagger-ui/index.html
```

**Nota:** El puerto 8082 es el mapeado en docker-compose.dev.yml (8082:8080)

### API Docs JSON/YAML
Los documentos OpenAPI están disponibles en:

```
http://localhost:8082/v3/api-docs        (JSON)
http://localhost:8082/v3/api-docs.yaml   (YAML)
```

## Autenticación

La mayoría de los endpoints requieren autenticación JWT de Keycloak.

### Pasos para autenticarse en Swagger:

1. **Obtener un token JWT de Keycloak:**
   ```bash
   curl -X POST 'http://localhost:7080/realms/markenx/protocol/openid-connect/token' \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     -d 'username=admin@udla.edu.ec' \
     -d 'password=admin123' \
     -d 'grant_type=password' \
     -d 'client_id=markenx-client'
   ```

2. **Copiar el `access_token` de la respuesta**

3. **En Swagger UI:**
   - Hacer clic en el botón **"Authorize"** (candado en la esquina superior derecha)
   - En el campo `bearerAuth`, ingresar: `Bearer {tu-token-aquí}`
   - Hacer clic en **"Authorize"**
   - Hacer clic en **"Close"**

4. **Ahora puedes probar los endpoints autenticados**

## Estructura de la API

### Tags (Grupos de Endpoints)

- **Admin**: Operaciones administrativas (requiere rol ADMIN)
  - Gestión de estudiantes (CRUD)
  - Importación masiva de estudiantes desde CSV
  - Depuración de autenticación

- **Students**: Operaciones de estudiantes (requiere rol STUDENT o ADMIN)
  - Perfil del estudiante actual
  - Consulta de estudiantes
  - Tareas asignadas

- **Courses**: Gestión de cursos
  - CRUD completo de cursos
  - Listado paginado

- **Academic Terms**: Gestión de periodos académicos
  - CRUD completo de términos académicos
  - Listado paginado

- **Assignments**: Catálogo de estados de asignaciones
  - Obtener estados disponibles (público)

## Características de Swagger

### Paginación
Muchos endpoints soportan paginación con parámetros:
- `page`: Número de página (0-based)
- `size`: Cantidad de elementos por página
- `sort`: Campo y dirección de ordenamiento (ej: `firstName,asc`)

### Validaciones
Swagger muestra automáticamente:
- Campos requeridos
- Formatos esperados
- Restricciones de validación

### Modelos/Schemas
En la sección "Schemas" al final de Swagger UI puedes ver:
- Estructuras de DTOs de request
- Estructuras de DTOs de response
- Modelos de dominio

## Seguridad

Los endpoints están protegidos según roles:

| Endpoint | Rol Requerido |
|----------|---------------|
| `/api/markenx/admin/**` | ADMIN |
| `/api/markenx/students/**` | STUDENT, ADMIN |
| `/api/markenx/courses/**` | STUDENT, ADMIN (escritura solo ADMIN) |
| `/api/markenx/academic-terms/**` | STUDENT, ADMIN (escritura solo ADMIN) |
| `/api/markenx/assignments/status` | Público |

## Configuración

### application.properties
```properties
# OpenAPI/Swagger Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
```

### Dependencias (pom.xml)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

## Pruebas con Swagger

### Ejemplo: Crear un estudiante (Admin)

1. Autenticarse con token de admin
2. Ir a **Admin** → **POST /api/markenx/admin/students**
3. Hacer clic en "Try it out"
4. Modificar el JSON de ejemplo:
   ```json
   {
     "firstName": "Juan",
     "lastName": "Pérez",
     "email": "juan.perez@udla.edu.ec",
     "password": "temporal123"
   }
   ```
5. Hacer clic en "Execute"
6. Ver la respuesta (201 Created)

### Ejemplo: Obtener perfil actual (Student)

1. Autenticarse con token de estudiante
2. Ir a **Students** → **GET /api/markenx/students/me**
3. Hacer clic en "Try it out"
4. Hacer clic en "Execute"
5. Ver tu perfil en la respuesta

## Personalización

Para personalizar la documentación de OpenAPI, editar:
```
src/main/java/com/udla/markenx/shared/infrastructure/config/OpenApiConfiguration.java
```

## Troubleshooting

### No veo Swagger UI
- Verificar que la aplicación esté corriendo
- Verificar la URL: `http://localhost:8080/swagger-ui.html`
- Revisar los logs para errores de inicialización

### Error 401 Unauthorized
- Verificar que el token JWT sea válido
- Verificar que el token no haya expirado
- Asegurarse de incluir "Bearer " antes del token

### Error 403 Forbidden
- Verificar que tu usuario tenga el rol necesario
- Admin endpoints requieren rol ADMIN
- Algunos endpoints requieren rol STUDENT

## Referencias

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)
