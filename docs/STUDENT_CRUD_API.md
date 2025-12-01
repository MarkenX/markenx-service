# Student CRUD Operations - Quick Reference

## Overview

The Student API provides complete CRUD operations for managing students in the MarkenX system. All student operations automatically integrate with Keycloak for authentication.

## API Endpoints

### Base URL
```
/api/markenx/students
```

All endpoints require authentication via Bearer token.

---

## 1. Create Student (Individual)

Creates a single student and registers them in Keycloak.

**Endpoint:**
```http
POST /api/markenx/students
```

**Authentication:** Required - ADMIN role only

**Request Body:**
```json
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@udla.edu.ec",
  "password": "SecurePass123!",
  "courseId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Validations:**
- `firstName`: Required, 2-50 characters
- `lastName`: Required, 2-50 characters
- `email`: Required, valid email format, must be `@udla.edu.ec` domain
- `password`: Required, minimum 8 characters
- `courseId`: Required, must be a valid UUID of an existing course

**Response (201 Created):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "code": "STD-000123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@udla.edu.ec",
  "status": "ENABLED",
  "enrolledCourseId": null,
  "createdAt": "2025-11-18T10:30:00Z",
  "updatedAt": "2025-11-18T10:30:00Z"
}
```

**Error Responses:**

**400 Bad Request** - Validation errors:
```json
{
  "message": "Validation failed",
  "errors": {
    "email": "El correo debe pertenecer al dominio @udla.edu.ec",
    "password": "La contraseña debe tener al menos 8 caracteres"
  }
}
```

**409 Conflict** - Duplicate:
```json
{
  "message": "El email ya existe en la base de datos"
}
```

**curl Example:**
```bash
curl -X POST http://localhost:8082/api/markenx/students \
  -H "Authorization: Bearer {admin-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Juan",
    "lastName": "Pérez",
    "email": "juan.perez@udla.edu.ec",
    "password": "SecurePass123!"
  }'
```

---

## 2. Delete Student (Soft Delete)

Disables a student by setting their status to DISABLED in both the database and Keycloak. The student record remains but is not visible to non-admins and cannot authenticate.

**Important:** 
- Database: Status set to DISABLED (soft delete)
- Keycloak: User is disabled (preserves authentication history, prevents login)

**Endpoint:**
```http
DELETE /api/markenx/students/{id}
```

**Authentication:** Required - ADMIN role only

**Path Parameters:**
- `id` (UUID): Student unique identifier

**Response (204 No Content):**
No body returned on success.

**Error Responses:**

**404 Not Found:**
```json
{
  "message": "Estudiante not found with id: 123e4567-e89b-12d3-a456-426614174000"
}
```

**curl Example:**
```bash
curl -X DELETE http://localhost:8082/api/markenx/students/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer {admin-token}"
```

---

## 3. Bulk Import Students (CSV)

Imports multiple students from a CSV file. All-or-nothing transaction: either all students are imported or none.

**Endpoint:**
```http
POST /api/markenx/students/bulk-import
```

**Authentication:** Required - ADMIN role only

**Request:**
- Content-Type: `multipart/form-data`
- Form field: `file` (CSV file)

**CSV Format:**
```csv
firstName,lastName,email,enrollmentCode,courseId
Juan,Pérez,juan.perez@udla.edu.ec,202301234,123e4567-e89b-12d3-a456-426614174000
María,García,maria.garcia@udla.edu.ec,202301235,123e4567-e89b-12d3-a456-426614174000
Pedro,Rodríguez,pedro.rodriguez@udla.edu.ec,202301236,123e4567-e89b-12d3-a456-426614174000
```

**CSV Requirements:**
- Header row is required
- Columns: `firstName`, `lastName`, `email`, `enrollmentCode`, `courseId`
- All emails must be `@udla.edu.ec` domain
- `courseId` must be a valid UUID of an existing course
- No duplicate emails (checked against DB and Keycloak)

**Default Password:** All imported students receive password: `ChangeMe123!`
- Must be changed on first login

**Response (201 Created):**
```json
{
  "message": "Todos los estudiantes fueron importados exitosamente",
  "totalRecords": 3,
  "successfulImports": 3
}
```

**Error Response (400 Bad Request):**
```json
{
  "message": "La importación falló: 2 estudiante(s) con errores",
  "totalRecords": 5,
  "errors": {
    "3": "El usuario ya existe en Keycloak",
    "5": "El correo debe pertenecer al dominio @udla.edu.ec"
  }
}
```

**curl Example:**
```bash
curl -X POST http://localhost:8082/api/markenx/students/bulk-import \
  -H "Authorization: Bearer {admin-token}" \
  -F "file=@students.csv"
```

---

## 4. Download CSV Template

Downloads a sample CSV template for bulk import with example data.

**Endpoint:**
```http
GET /api/markenx/students/bulk-import/template
```

**Authentication:** Required - ADMIN role only

**Response (200 OK):**
- Content-Type: `text/csv`
- Content-Disposition: `attachment; filename=student-import-template.csv`

**Template Contents:**
```csv
firstName,lastName,email,enrollmentCode,courseId
Juan,Pérez,juan.perez@udla.edu.ec,202301234,123e4567-e89b-12d3-a456-426614174000
María,García,maria.garcia@udla.edu.ec,202301235,123e4567-e89b-12d3-a456-426614174000
```

**curl Example:**
```bash
curl -X GET http://localhost:8082/api/markenx/students/bulk-import/template \
  -H "Authorization: Bearer {admin-token}" \
  -o student-template.csv
```

**Browser:** Simply navigate to the URL with valid admin token in Authorization header.

---

## 5. Get All Students

Retrieves a paginated list of students.
- **Admin users:** See all students including disabled ones
- **Student users:** See only enabled students

**Endpoint:**
```http
GET /api/markenx/students
```

**Authentication:** Required - STUDENT or ADMIN role

**Query Parameters:**
- `page` (int, default: 0): Page number (0-indexed)
- `size` (int, default: 10): Page size
- `sort` (string, repeatable): Sort criteria
  - Format: `property,direction`
  - Example: `lastName,asc`, `firstName,asc`
  - Valid properties: `id`, `status`, `createdAt`, `updatedAt`, `firstName`, `lastName`, `email`, `studentCode`

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "code": "STD-000123",
      "firstName": "Juan",
      "lastName": "Pérez",
      "email": "juan.perez@udla.edu.ec",
      "status": "ENABLED",
      "enrolledCourseId": null,
      "createdAt": "2025-11-18T10:30:00Z",
      "updatedAt": "2025-11-18T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "orders": [
        {
          "property": "lastName",
          "direction": "ASC"
        }
      ]
    }
  },
  "totalElements": 25,
  "totalPages": 3,
  "last": false,
  "first": true,
  "numberOfElements": 10
}
```

**curl Example:**
```bash
curl -X GET "http://localhost:8082/api/markenx/students?page=0&size=10&sort=lastName,asc" \
  -H "Authorization: Bearer {token}"
```

---

## 6. Get Student by ID

Retrieves a specific student by their UUID.
- **Admin users:** Can see disabled students
- **Student users:** Can only see enabled students

**Endpoint:**
```http
GET /api/markenx/students/{id}
```

**Authentication:** Required - STUDENT or ADMIN role

**Path Parameters:**
- `id` (UUID): Student unique identifier

**Response (200 OK):**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "code": "STD-000123",
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@udla.edu.ec",
  "status": "ENABLED",
  "enrolledCourseId": "456e7890-e89b-12d3-a456-426614174111",
  "createdAt": "2025-11-18T10:30:00Z",
  "updatedAt": "2025-11-18T10:30:00Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "message": "Estudiante not found with id: 123e4567-e89b-12d3-a456-426614174000"
}
```

**curl Example:**
```bash
curl -X GET http://localhost:8082/api/markenx/students/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer {token}"
```

---

## Integration with Keycloak

### Create Operations
When a student is created (individually or via bulk import):
1. User is created in Keycloak with:
   - Username: student's email
   - Password: provided (individual) or `ChangeMe123!` (bulk)
   - Role: `STUDENT`
   - Temporary password flag: enabled (must change on first login)
2. Student is saved in database

### Delete Operations
When a student is deleted:
- Database: Status set to `DISABLED` (soft delete)
- Keycloak: User is **DISABLED** (preserves authentication history, prevents login)
- Access: Non-admins cannot see disabled students, user cannot authenticate

### Duplicate Prevention
Before creating:
- Check if email exists in database
- Check if email exists in Keycloak
- If either exists, creation is rejected

---

## Common Validation Rules

### Email Domain
All student emails must belong to `@udla.edu.ec` domain.

**Valid:**
- `juan.perez@udla.edu.ec`
- `maria.garcia@udla.edu.ec`

**Invalid:**
- `juan@gmail.com`
- `maria@udla.edu`
- `pedro@udla.ec`

### Password Requirements
- Minimum 8 characters
- No specific complexity requirements (configured in Keycloak)

### Name Requirements
- First name: 2-50 characters
- Last name: 2-50 characters
- No special validation beyond length

---

## Error Handling

### HTTP Status Codes

| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET request |
| 201 | Created | Successful POST (create/import) |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation error, invalid CSV format |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Insufficient permissions (not ADMIN) |
| 404 | Not Found | Student doesn't exist |
| 409 | Conflict | Duplicate email |

### Error Response Format

**Validation Errors:**
```json
{
  "message": "Validation failed",
  "errors": {
    "fieldName": "Error message"
  }
}
```

**Resource Not Found:**
```json
{
  "message": "Estudiante not found with id: {uuid}"
}
```

**Duplicate Resource:**
```json
{
  "message": "El email ya existe en la base de datos"
}
```

---

## Testing Guide

### Postman Collection

Import the MarkenX API collection and use the following requests:

1. **Create Student:**
   - Endpoint: `POST {{base_url}}/students`
   - Body: JSON with firstName, lastName, email, password

2. **Delete Student:**
   - Endpoint: `DELETE {{base_url}}/students/{{student_id}}`

3. **Bulk Import:**
   - Endpoint: `POST {{base_url}}/students/bulk-import`
   - Body: form-data with `file` field

4. **Download Template:**
   - Endpoint: `GET {{base_url}}/students/bulk-import/template`

### Sample Data

**Individual Student:**
```json
{
  "firstName": "Test",
  "lastName": "Student",
  "email": "test.student@udla.edu.ec",
  "password": "TestPass123!"
}
```

**Bulk CSV:**
```csv
firstName,lastName,email,enrollmentCode,courseId
Test1,User1,test1.user1@udla.edu.ec,TEST001,123e4567-e89b-12d3-a456-426614174000
Test2,User2,test2.user2@udla.edu.ec,TEST002,123e4567-e89b-12d3-a456-426614174000
Test3,User3,test3.user3@udla.edu.ec,TEST003,123e4567-e89b-12d3-a456-426614174000
```

---

## Related Documentation

- [Student Creation with Keycloak](./STUDENT_CREATION_KEYCLOAK.md) - Detailed integration architecture
- [Authentication Flow](./AUTHENTICATION.md) - How students authenticate
- [API Endpoints](./POSTMAN_ENDPOINTS.md) - Complete API reference
- [Keycloak Setup](./KEYCLOAK_SETUP.md) - Keycloak configuration
