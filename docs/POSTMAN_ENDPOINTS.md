# MarkenX API - Complete Endpoint Reference

## Authentication

### Get Token
```http
POST http://localhost:7080/realms/markenx/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
client_id=markenx-admin-web
username=admin@markenx.com
password=admin123
scope=openid profile email
```

**Credentials:**
- Admin: `admin@markenx.com` / `admin123`
- Student: `student@markenx.com` / `student123`

---

## ADMIN ENDPOINTS

**Base URL:** `http://localhost:8082/api/markenx/admin`  
**Authorization:** Bearer Token (ADMIN role required)

### 1. Debug Authentication
```http
GET /debug/auth
Authorization: Bearer {{access_token}}
```

**Response:**
```
Authenticated: true
Principal: admin@markenx.com
Authorities: [ROLE_ADMIN]
Details: ...
```

---

### 2. Create Student
```http
POST /students
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Response (201 Created):**
```json
{
  "id": 23,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com"
}
```

**Audit Fields (in database):**
- `created_by`: "admin@markenx.com"
- `created_at`: "2025-11-07T14:30:00"
- `last_modified_by`: "admin@markenx.com"
- `last_modified_at`: "2025-11-07T14:30:00"

---

### 3. Get All Students (Paginated)
```http
GET /students?page=0&size=10
Authorization: Bearer {{access_token}}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Maria",
      "lastName": "Gonzalez",
      "email": "maria.gonzalez@udla.edu.ec"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 3,
  "totalElements": 23,
  "last": false
}
```

---

### 4. Get Student by ID
```http
GET /students/1
Authorization: Bearer {{access_token}}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "Maria",
  "lastName": "Gonzalez",
  "email": "maria.gonzalez@udla.edu.ec"
}
```

**Error (404 Not Found):**
```json
{
  "message": "Student not found with identifier: 999"
}
```

---

### 5. Update Student
```http
PUT /students/20
Authorization: Bearer {{access_token}}
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.updated@example.com"
}
```

**Response (200 OK):**
```json
{
  "id": 20,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.updated@example.com"
}
```

**Audit Update:**
- `last_modified_by` changes to current admin
- `last_modified_at` updates to current timestamp

---

### 6. Delete Student
```http
DELETE /students/22
Authorization: Bearer {{access_token}}
```

**Response (204 No Content):**
```
(empty body)
```

---

### 7. Bulk Import Students (NEW)
```http
POST /students/bulk-import
Authorization: Bearer {{access_token}}
Content-Type: multipart/form-data

file: students.csv
```

**CSV Format:**
```csv
firstName,lastName,email,enrollmentCode
Carlos,Mendez,carlos.mendez@udla.edu.ec,2025A001
Ana,Torres,ana.torres@udla.edu.ec,2025A002
Luis,Ramirez,luis.ramirez@udla.edu.ec,2025A003
```

**Email Validation:**
- ✅ Must end with `@udla.edu.ec`
- ❌ `user@gmail.com` - INVALID
- ❌ `user@udla.ec` - INVALID
- ❌ `user@udla.edu.com` - INVALID

**Default Password:** `enrollmentCode` + first 3 chars of `firstName`
- Example: `2025A001` + `Car` = `2025A001Car`

**Response - Complete Success (201 Created):**
```json
{
  "message": "Todos los estudiantes fueron importados exitosamente",
  "totalRecords": 3,
  "successfulImports": 3,
  "failureCount": 0,
  "failedImports": {},
  "partialSuccess": false
}
```

**Response - Partial Success (207 Multi-Status):**
```json
{
  "message": "Importación completada con algunos errores",
  "totalRecords": 5,
  "successfulImports": 3,
  "failureCount": 2,
  "failedImports": {
    "3": "El correo 'invalid@gmail.com' debe pertenecer al dominio @udla.edu.ec",
    "5": "Student already exists with email: duplicate@udla.edu.ec"
  },
  "partialSuccess": true
}
```

**Response - Complete Failure (400 Bad Request):**
```json
{
  "message": "La importación falló completamente",
  "totalRecords": 2,
  "successfulImports": 0,
  "failureCount": 2,
  "failedImports": {
    "2": "El correo 'test@hotmail.com' debe pertenecer al dominio @udla.edu.ec",
    "3": "El nombre es obligatorio"
  },
  "partialSuccess": false
}
```

---

## STUDENT ENDPOINTS

**Base URL:** `http://localhost:8082/api/markenx/students`  
**Authorization:** Bearer Token (STUDENT or ADMIN role)

### 8. Get Current Student Profile
```http
GET /me
Authorization: Bearer {{access_token}}
```

Returns profile of authenticated user based on JWT email.

**Response (200 OK):**
```json
{
  "id": 5,
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan.perez@udla.edu.ec"
}
```

---

### 9. Get Student Tasks (with filters)
```http
GET /{studentId}/tasks?status=ASSIGNED&startDate=2025-01-01&endDate=2025-12-31&page=0&size=10
Authorization: Bearer {{access_token}}
```

**Parameters:**
- `studentId` (required): Student ID
- `status` (optional): `ASSIGNED`, `IN_PROGRESS`, `COMPLETED`, `OVERDUE`
- `startDate` (optional): Format `YYYY-MM-DD`
- `endDate` (optional): Format `YYYY-MM-DD`
- `page` (optional): Default 0
- `size` (optional): Default 10

**Examples:**
```http
GET /1/tasks?status=ASSIGNED
GET /1/tasks?startDate=2025-01-01&endDate=2025-12-31
GET /1/tasks?page=0&size=20
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Tarea de Matemáticas",
      "summary": "Resolver ejercicios de cálculo",
      "currentStatus": "ASSIGNED",
      "dueDate": "2025-12-15",
      "activeAttempt": 0,
      "maxAttempts": 3
    }
  ],
  "totalElements": 15
}
```

---

## TASK ENDPOINTS

**Base URL:** `http://localhost:8082/api/markenx`

### 10. Get Task Attempts
```http
GET /tasks/1/attempts?page=0&size=10
Authorization: Bearer {{access_token}}
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "score": 8.5,
      "date": "2025-11-05",
      "duration": "PT45M"
    }
  ],
  "totalElements": 5
}
```

---

## ASSIGNMENT ENDPOINTS

**Base URL:** `http://localhost:8082/api/markenx`

### 11. Get Assignment Status Options
```http
GET /assignments/status
```

**No authentication required**

**Response (200 OK):**
```json
[
  {
    "name": "ASSIGNED",
    "displayName": "Asignado"
  },
  {
    "name": "IN_PROGRESS",
    "displayName": "En Progreso"
  },
  {
    "name": "COMPLETED",
    "displayName": "Completado"
  },
  {
    "name": "OVERDUE",
    "displayName": "Vencido"
  }
]
```

---

## ERROR RESPONSES

### Validation Errors (400 Bad Request)
```json
{
  "firstName": "El nombre debe tener entre 2 y 50 caracteres",
  "email": "El email debe ser válido",
  "password": "La contraseña debe tener al menos 8 caracteres"
}
```

### Resource Not Found (404 Not Found)
```json
{
  "message": "Student not found with identifier: 999"
}
```

### Duplicate Resource (409 Conflict)
```json
{
  "message": "Student already exists with email: duplicate@udla.edu.ec"
}
```

### Invalid Email Domain (400 Bad Request)
```json
{
  "message": "El correo 'invalid@gmail.com' debe pertenecer al dominio @udla.edu.ec"
}
```

### Unauthorized (401)
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

### Forbidden (403)
```json
{
  "error": "Forbidden",
  "message": "Access Denied"
}
```

### Generic Error (500)
```json
{
  "message": "An unexpected error occurred: [error details]"
}
```

---

## Postman Pre-Request Script

Use this script at collection level for automatic token refresh:

```javascript
// Auto-refresh token if expired or missing
const currentToken = pm.collectionVariables.get('access_token');
const tokenExpiry = pm.collectionVariables.get('token_expiry');
const now = Math.floor(Date.now() / 1000);

// Check if token needs refresh (30 seconds buffer)
if (!currentToken || !tokenExpiry || now >= (tokenExpiry - 30)) {
    console.log('Refreshing token...');
    
    const tokenRequest = {
        url: 'http://localhost:7080/realms/markenx/protocol/openid-connect/token',
        method: 'POST',
        header: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: {
            mode: 'urlencoded',
            urlencoded: [
                { key: 'grant_type', value: 'password' },
                { key: 'client_id', value: 'markenx-admin-web' },
                { key: 'username', value: 'admin@markenx.com' },
                { key: 'password', value: 'admin123' },
                { key: 'scope', value: 'openid profile email' }
            ]
        }
    };

    pm.sendRequest(tokenRequest, function (err, response) {
        if (err) {
            console.error('Token refresh failed:', err);
            return;
        }

        if (response.code === 200) {
            const jsonData = response.json();
            const newExpiry = Math.floor(Date.now() / 1000) + jsonData.expires_in;

            pm.collectionVariables.set('access_token', jsonData.access_token);
            pm.collectionVariables.set('token_expiry', newExpiry);
            pm.collectionVariables.set('refresh_token', jsonData.refresh_token);

            console.log('Token refreshed! Expires in', jsonData.expires_in, 'seconds');
        } else {
            console.error('Failed to get token. Status:', response.code);
        }
    });
} else {
    const timeLeft = tokenExpiry - now;
    console.log('Token valid for', timeLeft, 'seconds');
}
```

**Collection Variables:**
- `access_token` (empty)
- `token_expiry` (empty)
- `refresh_token` (empty)

---

## Quick Reference

| Endpoint | Method | Role | Description |
|----------|--------|------|-------------|
| `/admin/students` | POST | ADMIN | Create student |
| `/admin/students` | GET | ADMIN | List students (paginated) |
| `/admin/students/{id}` | GET | ADMIN | Get student by ID |
| `/admin/students/{id}` | PUT | ADMIN | Update student |
| `/admin/students/{id}` | DELETE | ADMIN | Delete student |
| `/admin/students/bulk-import` | POST | ADMIN | **Bulk import from CSV** |
| `/students/me` | GET | STUDENT/ADMIN | Current user profile |
| `/students/{id}/tasks` | GET | STUDENT/ADMIN | Student's tasks (filtered) |
| `/tasks/{id}/attempts` | GET | Any | Task attempts |
| `/assignments/status` | GET | Public | Assignment statuses |

---

## New Features Summary

1. **Audit Tracking**: All student/assignment CRUD operations now track who (admin email) and when (timestamp)
2. **Bulk Import**: Import multiple students from CSV with @udla.edu.ec validation
3. **Enhanced Errors**: Clear, user-friendly error messages with specific validation feedback
4. **Multi-Status Responses**: Bulk import returns detailed success/failure breakdown
