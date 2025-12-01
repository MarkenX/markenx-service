# API Endpoints - MarkenX Service

## Resumen de Endpoints CRUD Implementados

Este documento lista todos los endpoints REST expuestos para las entidades del dominio `classroom`.

---

## 1. Academic Terms (Períodos Académicos)

**Base URL:** `/api/markenx/academic-terms`

### Endpoints

| Método | Endpoint | Descripción | Roles | Request Body | Response |
|--------|----------|-------------|-------|--------------|----------|
| POST | `/` | Crear período académico | ADMIN | `CreateAcademicPeriodRequestDTO` | `AcademicPeriodResponseDTO` (201) |
| GET | `/` | Listar todos los períodos (paginado) | ADMIN, STUDENT | - | `Page<AcademicPeriodResponseDTO>` (200) |
| GET | `/{id}` | Obtener período por ID | ADMIN, STUDENT | - | `AcademicPeriodResponseDTO` (200) |
| PUT | `/{id}` | Actualizar período | ADMIN | `UpdateAcademicTermRequestDTO` | `AcademicPeriodResponseDTO` (200) |
| DELETE | `/{id}` | Eliminar período (soft delete) | ADMIN | - | 204 No Content |

### Modelos

**CreateAcademicPeriodRequestDTO:**
```json
{
  "startDate": "2024-01-15",
  "endDate": "2024-06-30",
  "year": 2024
}
```

**UpdateAcademicTermRequestDTO:**
```json
{
  "startDate": "2024-01-15",
  "endDate": "2024-06-30",
  "year": 2024
}
```

**AcademicPeriodResponseDTO:**
```json
{
  "code": "AT-2024-01",
  "startOfTerm": "2024-01-15",
  "endOfTerm": "2024-06-30",
  "academicYear": 2024,
  "label": "1er Semestre - 2024"
}
```

---

## 2. Courses (Cursos)

**Base URL:** `/api/markenx/courses`

### Endpoints

| Método | Endpoint | Descripción | Roles | Request Body | Response |
|--------|----------|-------------|-------|--------------|----------|
| POST | `/` | Crear curso | ADMIN | `CreateCourseRequestDTO` | `CourseResponseDTO` (201) |
| GET | `/` | Listar todos los cursos (paginado) | ADMIN, STUDENT | - | `Page<CourseResponseDTO>` (200) |
| GET | `/{id}` | Obtener curso por ID | ADMIN, STUDENT | - | `CourseResponseDTO` (200) |
| PUT | `/{id}` | Actualizar curso | ADMIN | `UpdateCourseRequestDTO` | `CourseResponseDTO` (200) |
| DELETE | `/{id}` | Eliminar curso (soft delete) | ADMIN | - | 204 No Content |

### Modelos

**CreateCourseRequestDTO:**
```json
{
  "academicPeriodId": 1,
  "label": "Desarrollo de Software"
}
```

**UpdateCourseRequestDTO:**
```json
{
  "name": "Desarrollo de Software Avanzado"
}
```

**CourseResponseDTO:**
```json
{
  "code": "CRS-2024-0001",
  "name": "Desarrollo de Software"
}
```

---

## 3. Students (Estudiantes)

**Base URL:** `/api/markenx/students`

### Endpoints

| Método | Endpoint | Descripción | Roles | Request Body | Response |
|--------|----------|-------------|-------|--------------|----------|
| POST | `/` | Crear estudiante | ADMIN | `CreateStudentRequestDTO` | `StudentResponseDTO` (201) |
| GET | `/` | Listar todos los estudiantes (paginado) | ADMIN, STUDENT | - | `Page<StudentResponseDTO>` (200) |
| GET | `/me` | Obtener perfil del estudiante actual | ADMIN, STUDENT | - | `StudentResponseDTO` (200) |
| GET | `/{id}` | Obtener estudiante por ID | ADMIN, STUDENT | - | `StudentResponseDTO` (200) |
| PUT | `/{id}` | Actualizar estudiante | ADMIN | `UpdateStudentRequestDTO` | `StudentResponseDTO` (200) |
| DELETE | `/{id}` | Eliminar estudiante (soft delete) | ADMIN | - | 204 No Content |

### Modelos

**CreateStudentRequestDTO:**
```json
{
  "courseId": 1,
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@udla.edu.ec"
}
```

**UpdateStudentRequestDTO:**
```json
{
  "firstName": "Juan Carlos",
  "lastName": "Pérez González",
  "email": "juan.perez@udla.edu.ec"
}
```

**StudentResponseDTO:**
```json
{
  "code": "STD-000001",
  "firstName": "Juan",
  "lastName": "Pérez",
  "academicEmail": "juan.perez@udla.edu.ec"
}
```

---

## 4. Tasks (Tareas)

**Base URL:** `/api/markenx/tasks`

### Endpoints

| Método | Endpoint | Descripción | Roles | Request Body | Response |
|--------|----------|-------------|-------|--------------|----------|
| POST | `/` | Crear tarea | ADMIN | `CreateTaskRequestDTO` | `TaskResponseDTO` (201) |
| GET | `/` | Listar todas las tareas (paginado) | ADMIN, STUDENT | - | `Page<TaskResponseDTO>` (200) |
| GET | `/{id}` | Obtener tarea por ID | ADMIN, STUDENT | - | `TaskResponseDTO` (200) |
| PUT | `/{id}` | Actualizar tarea | ADMIN | `UpdateTaskRequestDTO` | `TaskResponseDTO` (200) |
| DELETE | `/{id}` | Eliminar tarea (soft delete) | ADMIN | - | 204 No Content |

### Modelos

**CreateTaskRequestDTO:**
```json
{
  "courseId": 1,
  "title": "Tarea de Programación",
  "summary": "Implementar un algoritmo de ordenamiento",
  "dueDate": "2024-03-15",
  "maxAttempts": 3,
  "minScoreToPass": 7.0
}
```

**UpdateTaskRequestDTO:**
```json
{
  "title": "Tarea de Programación Avanzada",
  "summary": "Implementar algoritmos de ordenamiento y búsqueda",
  "dueDate": "2024-03-20",
  "maxAttempts": 5,
  "minScoreToPass": 6.5
}
```

**TaskResponseDTO:**
```json
{
  "code": "TSK-2024-0001",
  "title": "Tarea de Programación",
  "summary": "Implementar un algoritmo de ordenamiento",
  "dueDate": "2024-03-15",
  "maxAttempts": 3,
  "minScoreToPass": 7.0
}
```

---

## Notas Importantes

### Autenticación y Autorización

- Todos los endpoints requieren autenticación mediante JWT token
- Los endpoints están protegidos por roles:
  - **ADMIN**: Acceso completo (CRUD)
  - **STUDENT**: Solo lectura (GET)

### Soft Delete

La eliminación de entidades es **lógica** (soft delete):
- Al eliminar, el estado de la entidad cambia a `DISABLED`
- Las entidades con estado `DISABLED` NO se muestran a estudiantes
- Los administradores pueden ver todas las entidades, incluidas las deshabilitadas

### Paginación

Los endpoints de listado (`GET /`) soportan paginación con parámetros query:
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)
- `sort`: Ordenamiento (ejemplo: `sort=name,asc`)

**Ejemplo:**
```
GET /api/markenx/courses?page=0&size=20&sort=name,asc
```

### Respuestas de Error

Formato estándar de errores:
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Curso no encontrado con ID: 123",
  "path": "/api/markenx/courses/123"
}
```

### Códigos de Estado HTTP

- **200 OK**: Operación exitosa (GET, PUT)
- **201 Created**: Recurso creado exitosamente (POST)
- **204 No Content**: Eliminación exitosa (DELETE)
- **400 Bad Request**: Error de validación
- **401 Unauthorized**: No autenticado
- **403 Forbidden**: No autorizado
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Error del servidor

---

## Ejemplos de Uso con cURL

### Crear Período Académico
```bash
curl -X POST http://localhost:8080/api/markenx/academic-terms \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "startDate": "2024-01-15",
    "endDate": "2024-06-30",
    "year": 2024
  }'
```

### Listar Cursos (Paginado)
```bash
curl -X GET "http://localhost:8080/api/markenx/courses?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Obtener Estudiante por ID
```bash
curl -X GET http://localhost:8080/api/markenx/students/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Actualizar Tarea
```bash
curl -X PUT http://localhost:8080/api/markenx/tasks/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tarea Actualizada",
    "summary": "Nueva descripción",
    "dueDate": "2024-03-20",
    "maxAttempts": 5,
    "minScoreToPass": 7.5
  }'
```

### Eliminar Curso (Soft Delete)
```bash
curl -X DELETE http://localhost:8080/api/markenx/courses/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
