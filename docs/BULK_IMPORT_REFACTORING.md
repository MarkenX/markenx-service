# Refactorización de Importación Masiva de Estudiantes

## Cambios Realizados

### 1. Consolidación de Servicios (DDD - Mismo Dominio)

**Antes:**
- `BulkStudentImportService` - Servicio separado para importación CSV
- `StudentService` - Solo para obtener tareas de estudiantes

**Después:**
- `StudentService` - Servicio único del dominio de estudiantes que incluye:
  - Obtener tareas de estudiantes
  - Importar estudiantes desde CSV

**Justificación:** Ambas funcionalidades pertenecen al mismo dominio (Student), por lo que deben estar en el mismo servicio según principios de DDD.

**Archivos Modificados:**
- ✅ `StudentService.java` - Ahora incluye método `importStudentsFromCsv()`
- ❌ `BulkStudentImportService.java` - ELIMINADO
- ✅ `AdminController.java` - Ahora inyecta `StudentService` en lugar de `BulkStudentImportService`

---

### 2. Importación Todo-o-Nada (Transaccionalidad)

**Antes:**
- Importación con éxito parcial (algunos estudiantes OK, otros KO)
- Respuesta HTTP 207 Multi-Status para éxitos parciales
- Respuesta HTTP 201 para éxito total
- Respuesta HTTP 400 para fallo total

**Después:**
- **Solo dos resultados posibles:**
  - ✅ **Éxito Total (HTTP 201)**: Todos los estudiantes fueron importados
  - ❌ **Fallo Total (HTTP 400)**: Ningún estudiante fue importado (rollback completo)

**Lógica de Validación:**
1. **FASE 1**: Validar TODOS los estudiantes primero
   - Email debe ser `@udla.edu.ec`
   - Todos los campos obligatorios deben estar presentes
   - Si ANY validación falla → lanzar `BulkImportException` con TODOS los errores

2. **FASE 2**: Importar TODOS los estudiantes (solo si TODOS son válidos)
   - Crear todos los estudiantes en Keycloak
   - Guardar todos en base de datos
   - Transacción garantiza rollback si algo falla

**Archivos Modificados:**
- ✅ `StudentService.java` - Método `processStudentImports()` valida todo antes de importar
- ✅ `BulkImportException.java` - Eliminado campo `successfulImports` (ya no hay éxito parcial)
- ✅ `BulkImportResponseDTO.java` - Eliminados métodos `partialSuccess()` y `failure()`
- ✅ `AdminController.java` - Endpoint siempre retorna HTTP 201 (fallos lanzan excepción)
- ✅ `ControllerExceptionHandler.java` - Handler actualizado para retornar solo HTTP 400

---

### 3. Mensajes de API en Español

**Antes:**
```java
"Student not found with identifier: 123"
"Student already exists with email: test@example.com"
"An unexpected error occurred: ..."
```

**Después:**
```java
"Estudiante no encontrado con identificador: 123"
"Estudiante ya existe con email: test@example.com"
"Ocurrió un error inesperado: ..."
```

**Archivos Modificados:**
- ✅ `ResourceNotFoundException.java` - Mensajes en español
- ✅ `DuplicateResourceException.java` - Mensajes en español
- ✅ `ControllerExceptionHandler.java` - Mensaje genérico en español
- ✅ `StudentService.java` - Todos los mensajes de validación en español
- ✅ `BulkImportResponseDTO.java` - Mensaje de éxito en español

---

### 4. Documentación Organizada

**Antes:**
- Archivos `.md` en la raíz del proyecto

**Después:**
- Archivos `.md` movidos a carpeta `docs/`
  - `README.md`
  - `POSTMAN_ENDPOINTS.md`
  - `IMPLEMENTATION_SUMMARY.md`
  - `BULK_IMPORT_REFACTORING.md` (nuevo)

---

## Respuestas de API Actualizadas

### Importación Exitosa (HTTP 201 Created)

**Request:**
```http
POST /api/markenx/admin/students/bulk-import
Content-Type: multipart/form-data
Authorization: Bearer {token}

file: students.csv
```

**CSV Válido:**
```csv
firstName,lastName,email,enrollmentCode
Carlos,Mendez,carlos.mendez@udla.edu.ec,2025A001
Ana,Torres,ana.torres@udla.edu.ec,2025A002
Luis,Ramirez,luis.ramirez@udla.edu.ec,2025A003
```

**Response (HTTP 201):**
```json
{
  "message": "Todos los estudiantes fueron importados exitosamente",
  "totalRecords": 3,
  "successfulImports": 3
}
```

---

### Importación Fallida (HTTP 400 Bad Request)

**CSV con Errores:**
```csv
firstName,lastName,email,enrollmentCode
Carlos,Mendez,carlos.mendez@gmail.com,2025A001
,Torres,ana.torres@udla.edu.ec,2025A002
Luis,Ramirez,luis@udla.ec,2025A003
```

**Response (HTTP 400):**
```json
{
  "message": "La importación falló: 3 estudiante(s) con errores",
  "totalRecords": 3,
  "failureCount": 3,
  "failedImports": {
    "2": "El correo 'carlos.mendez@gmail.com' debe pertenecer al dominio @udla.edu.ec",
    "3": "El nombre es obligatorio",
    "4": "El correo 'luis@udla.ec' debe pertenecer al dominio @udla.edu.ec"
  }
}
```

**Importante:** 
- Ningún estudiante fue importado (rollback completo)
- `failedImports` usa número de fila del CSV (incluye header en fila 1)
- Todos los errores de validación se retornan juntos

---

## Validaciones Implementadas

### 1. Email @udla.edu.ec
```java
Pattern: ".*@udla\\.edu\\.ec$"
```

**Válidos:**
- ✅ `juan.perez@udla.edu.ec`
- ✅ `maria123@udla.edu.ec`
- ✅ `carlos.mendez+test@udla.edu.ec`

**Inválidos:**
- ❌ `juan@gmail.com` → "debe pertenecer al dominio @udla.edu.ec"
- ❌ `maria@udla.ec` → "debe pertenecer al dominio @udla.edu.ec"
- ❌ `carlos@udla.edu.com` → "debe pertenecer al dominio @udla.edu.ec"
- ❌ `test@udla.edu.ec.fake` → "debe pertenecer al dominio @udla.edu.ec"

### 2. Campos Obligatorios
- `firstName` - "El nombre es obligatorio"
- `lastName` - "El apellido es obligatorio"
- `email` - "El correo electrónico no puede estar vacío"
- `enrollmentCode` - "El código de matrícula es obligatorio"

### 3. Generación de Contraseña
```java
enrollmentCode + firstName.substring(0, 3)
```

**Ejemplos:**
- Código: `2025A001`, Nombre: `Juan` → Contraseña: `2025A001Jua`
- Código: `2025B`, Nombre: `Ana` → Contraseña: `2025BAna`
- Código: `X`, Nombre: `Li` → Contraseña: `XLi` (si nombre < 3 chars, usa todo)

---

## Transaccionalidad y Rollback

### Anotación @Transactional
```java
@Transactional
public BulkImportResponseDTO importStudentsFromCsv(MultipartFile file)
```

**Garantiza:**
1. Si **cualquier** operación falla durante la importación → rollback completo
2. Si validación falla en Fase 1 → no se intenta importar nada
3. Si Keycloak falla al crear usuario → rollback de base de datos
4. Si base de datos falla → no se confirma ningún cambio

**Ejemplo de Rollback:**
```
CSV con 10 estudiantes:
- Estudiantes 1-9: Válidos
- Estudiante 10: Email duplicado en Keycloak

Resultado: NINGÚN estudiante es importado (rollback automático)
```

---

## Testing Manual

### 1. Test de Éxito Total
```bash
# Crear CSV con 3 estudiantes válidos
cat > students_valid.csv << EOF
firstName,lastName,email,enrollmentCode
Test1,User1,test1@udla.edu.ec,2025T001
Test2,User2,test2@udla.edu.ec,2025T002
Test3,User3,test3@udla.edu.ec,2025T003
EOF

# Importar
curl -X POST http://localhost:8082/api/markenx/admin/students/bulk-import \
  -H "Authorization: Bearer {token}" \
  -F "file=@students_valid.csv"

# Esperado: HTTP 201 con message "Todos los estudiantes fueron importados exitosamente"
```

### 2. Test de Fallo por Email Inválido
```bash
# CSV con emails no-@udla.edu.ec
cat > students_invalid.csv << EOF
firstName,lastName,email,enrollmentCode
Bad1,User1,bad1@gmail.com,2025B001
Bad2,User2,bad2@hotmail.com,2025B002
EOF

# Importar
curl -X POST http://localhost:8082/api/markenx/admin/students/bulk-import \
  -H "Authorization: Bearer {token}" \
  -F "file=@students_invalid.csv"

# Esperado: HTTP 400 con failedImports mostrando errores de dominio email
```

### 3. Test de Fallo por Campos Faltantes
```bash
# CSV con campos vacíos
cat > students_missing.csv << EOF
firstName,lastName,email,enrollmentCode
,User1,test@udla.edu.ec,2025M001
Test2,,test2@udla.edu.ec,2025M002
Test3,User3,,2025M003
EOF

# Esperado: HTTP 400 con mensajes "El nombre es obligatorio", etc.
```

### 4. Test de Rollback Transaccional
```bash
# Importar mismo CSV dos veces
curl -X POST ... -F "file=@students_valid.csv"  # Primera vez: OK
curl -X POST ... -F "file=@students_valid.csv"  # Segunda vez: FAIL (duplicados)

# Verificar en BD que NO se crearon estudiantes duplicados
docker exec markenx-mysql mysql -u root -p markenx -e \
  "SELECT COUNT(*) FROM persons WHERE person_email LIKE 'test%@udla.edu.ec';"

# Esperado: Solo 3 registros (primer import), segunda importación hizo rollback completo
```

---

## Impacto en Base de Datos

### NO hay cambios en esquema
- Tablas: Sin cambios
- Columnas: Sin cambios
- Restricciones: Sin cambios

### Comportamiento transaccional
```sql
START TRANSACTION;

-- Fase 1: Validaciones (sin INSERT)
-- Si ANY falla → ROLLBACK y lanzar BulkImportException

-- Fase 2: Inserts (solo si Fase 1 OK)
INSERT INTO persons (keycloak_user_id, person_email, ...) VALUES (...);
INSERT INTO students (person_id, ...) VALUES (...);
-- Si ANY falla → ROLLBACK automático por @Transactional

COMMIT;
```

---

## Migración de Código Existente

### ¿Qué necesitan cambiar los desarrolladores?

**1. Importaciones:**
```java
// Antes
import com.udla.markenx.application.services.BulkStudentImportService;

// Después
import com.udla.markenx.application.services.StudentService;
```

**2. Inyección de Dependencias:**
```java
// Antes
private final BulkStudentImportService bulkImportService;

// Después
private final StudentService studentService;
```

**3. Llamadas a Métodos:**
```java
// Antes
bulkImportService.importStudentsFromCsv(file);

// Después
studentService.importStudentsFromCsv(file);
```

**4. Manejo de Respuestas:**
```java
// Antes
BulkImportResponseDTO response = service.importStudentsFromCsv(file);
if (response.getFailureCount() == 0) {
  return ResponseEntity.status(HttpStatus.CREATED).body(response);
} else if (response.getSuccessfulImports() > 0) {
  return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(response);
} else {
  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}

// Después
BulkImportResponseDTO response = studentService.importStudentsFromCsv(file);
return ResponseEntity.status(HttpStatus.CREATED).body(response);
// Fallos se manejan automáticamente con @ExceptionHandler
```

---

## Archivos Cambiados

### Servicios
- ✅ **Modificado:** `StudentService.java` (+150 líneas)
- ❌ **Eliminado:** `BulkStudentImportService.java`

### Excepciones
- ✅ **Modificado:** `BulkImportException.java` (-1 campo, -1 método)
- ✅ **Modificado:** `ResourceNotFoundException.java` (mensajes español)
- ✅ **Modificado:** `DuplicateResourceException.java` (mensajes español)

### DTOs
- ✅ **Modificado:** `BulkImportResponseDTO.java` (-3 campos, -2 métodos)

### Controllers
- ✅ **Modificado:** `AdminController.java` (cambio de dependencia)
- ✅ **Modificado:** `ControllerExceptionHandler.java` (handler actualizado)

### Documentación
- ✅ **Creado:** `docs/BULK_IMPORT_REFACTORING.md`
- ✅ **Movido:** `docs/README.md`
- ✅ **Movido:** `docs/POSTMAN_ENDPOINTS.md`
- ✅ **Movido:** `docs/IMPLEMENTATION_SUMMARY.md`

---

## Compatibilidad

### ✅ Sin Breaking Changes en API
- Endpoint: `POST /api/markenx/admin/students/bulk-import` (sin cambios)
- Content-Type: `multipart/form-data` (sin cambios)
- CSV Format: `firstName,lastName,email,enrollmentCode` (sin cambios)
- Validación email: `@udla.edu.ec` (sin cambios)

### ⚠️ Cambios en Respuestas
- **Removido:** HTTP 207 Multi-Status (ya no hay éxito parcial)
- **Removido:** Campos `partialSuccess`, `failureCount` en respuestas exitosas
- **Cambiado:** Mensajes de error ahora en español

### 🔄 Requerido para Clientes
- **Frontend:** Actualizar manejo de respuestas (eliminar lógica de HTTP 207)
- **Tests:** Actualizar assertions (no más `partialSuccess`)
- **Documentación:** Actualizar ejemplos de API

---

## Beneficios de la Refactorización

### 1. DDD Compliance
✅ Todas las operaciones de dominio Student en un solo servicio  
✅ Menor acoplamiento (un servicio menos)  
✅ Mayor cohesión (funcionalidades relacionadas juntas)

### 2. Transaccionalidad
✅ Garantía de consistencia (todo o nada)  
✅ No más estados inconsistentes (algunos OK, otros KO)  
✅ Rollback automático en caso de error

### 3. UX Mejorado
✅ Mensajes de error en español  
✅ Validación completa antes de importar (feedback más rápido)  
✅ Todos los errores mostrados juntos (no uno por uno)

### 4. Mantenibilidad
✅ Menos archivos de código  
✅ Lógica centralizada en un servicio  
✅ Más fácil de testear (un solo servicio)

---

## Próximos Pasos

### Testing
- [ ] Crear tests unitarios para `StudentService.importStudentsFromCsv()`
- [ ] Crear tests de integración con H2 para validar rollback
- [ ] Crear tests de controlador para verificar HTTP 201/400

### Documentación
- [ ] Actualizar `POSTMAN_ENDPOINTS.md` con nuevos ejemplos
- [ ] Crear guía de usuario para importación CSV
- [ ] Documentar formato CSV con ejemplos

### Futuras Mejoras
- [ ] Permitir configurar dominio de email en `application.properties`
- [ ] Agregar validación de formato de `enrollmentCode`
- [ ] Implementar preview de CSV antes de importar
- [ ] Agregar límite de tamaño de archivo CSV

---

## Contacto y Soporte

Para preguntas sobre esta refactorización:
- Revisar este documento primero
- Revisar `docs/IMPLEMENTATION_SUMMARY.md` para detalles de auditoría
- Revisar `docs/POSTMAN_ENDPOINTS.md` para ejemplos de API
