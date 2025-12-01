# Student Creation with Keycloak Integration

## Overview

When students are created in MarkenX (either individually or via bulk import), they are automatically registered in Keycloak to enable authentication and access to the system.

## Architecture

### Flow Diagram

```
CSV Upload → StudentService → Keycloak + Database
                  ↓
            1. Validate CSV
            2. Check Duplicates (DB + Keycloak)
            3. Create in Keycloak
            4. Save in Database
```

### Components Involved

1. **StudentService** (`application/services/StudentService.java`)
   - Orchestrates the bulk import process
   - Validates CSV data
   - Integrates with Keycloak and database

2. **AuthenticationServicePort** (`shared/application/port/out/auth/AuthenticationServicePort.java`)
   - Outbound port for authentication system operations
   - Methods: `createUser()`, `deleteUser()`, `userExists()`
   - Implemented by: `KeycloakAdminAdapter`

3. **StudentRepositoryPort** (`application/ports/out/persistance/repositories/StudentRepositoryPort.java`)
   - Outbound port for student persistence
   - Methods: `save()`, `existsByEmail()`, etc.

## Bulk Import Process

### Phase 1: Validation

All students are validated **before** any creation occurs:

```java
// CSV validation
- firstName: required, non-empty
- lastName: required, non-empty
- email: required, @udla.edu.ec domain
- enrollmentCode: required, non-empty

// Duplicate checks
- Database: existsByEmail(email)
- Keycloak: userExists(email)
```

**All-or-Nothing**: If any student fails validation, the entire import is rejected and NO students are created.

### Phase 2: Creation

If all validations pass, students are created:

```java
for each student:
  1. Create user in Keycloak
     - email as username
     - default password: "ChangeMe123!"
     - role: STUDENT
     - requirePasswordChange: true
  
  2. Save student in database
     - domain object created
     - persisted via repository
```

### Default Credentials

**Password**: `ChangeMe123!`
- All new students receive this temporary password
- Must be changed on first login (`requirePasswordChange: true`)

**Role**: `STUDENT`
- Assigned automatically during creation
- Grants access to student endpoints

## Error Handling

### Validation Errors

```json
{
  "message": "La importación falló: 2 estudiante(s) con errores",
  "totalRecords": 10,
  "errors": {
    "3": "El email ya existe en la base de datos",
    "7": "El correo debe pertenecer al dominio @udla.edu.ec"
  }
}
```

Row numbers are 1-indexed (row 1 = CSV header, row 2+ = data).

### System Errors

If a student fails **after** validation (e.g., Keycloak API error):

```json
{
  "message": "Error al crear estudiante: juan@udla.edu.ec. Causa: Connection timeout",
  "type": "RuntimeException"
}
```

This indicates a system issue, not a validation problem.

## API Endpoints

### Bulk Import

```http
POST /api/markenx/students/bulk-import
Content-Type: multipart/form-data
Authorization: Bearer {admin-token}

file: students.csv
```

**CSV Format**:
```csv
firstName,lastName,email,enrollmentCode
Juan,Pérez,juan.perez@udla.edu.ec,202301234
María,García,maria.garcia@udla.edu.ec,202301235
```

**Response** (Success):
```json
{
  "message": "Todos los estudiantes fueron importados exitosamente",
  "totalRecords": 2,
  "successfulImports": 2
}
```

**Response** (Validation Error - 400):
```json
{
  "message": "La importación falló: 1 estudiante(s) con errores",
  "totalRecords": 2,
  "errors": {
    "3": "El usuario ya existe en Keycloak"
  }
}
```

## Configuration

### Keycloak Settings

Located in `application.properties`:

```properties
# Keycloak Admin API
keycloak.realm=markenx
keycloak.auth-server-url=http://keycloak:8080
keycloak.resource=markenx-service
keycloak.admin-username=${KEYCLOAK_ADMIN_USER}
keycloak.admin-password=${KEYCLOAK_ADMIN_PASSWORD}
```

### Default Password

Defined in `StudentService.java`:

```java
private static final String DEFAULT_PASSWORD = "ChangeMe123!";
```

To change, update this constant. Consider making it configurable via environment variable in production.

## Security Considerations

### Password Policy

- **Temporary Password**: All students receive the same initial password
- **First Login**: Users MUST change password on first login
- **Keycloak Policy**: Ensure Keycloak has password complexity rules configured

### Email Validation

- **Domain Restriction**: Only `@udla.edu.ec` emails allowed
- **Pattern**: `.*@udla\.edu\.ec$`
- **Case Insensitive**: Emails normalized to lowercase

### Duplicate Prevention

- **Database Check**: Prevents duplicate student records
- **Keycloak Check**: Prevents duplicate user accounts
- **Both Required**: A student could exist in DB but not Keycloak (or vice versa) due to partial failures

## Transaction Management

### Database Transaction

```java
@Transactional
public BulkImportResponseDTO importStudentsFromCsv(MultipartFile file)
```

- If database save fails, transaction rolls back
- Keycloak users are NOT automatically rolled back
- **Limitation**: Distributed transaction across Keycloak + DB not supported

### Rollback Scenario

If Keycloak succeeds but database fails:
- User exists in Keycloak
- Student NOT in database
- Re-running import will fail: "El usuario ya existe en Keycloak"
- **Manual Fix**: Admin must delete user from Keycloak before retry

**Future Improvement**: Implement compensating transactions or Saga pattern.

## Testing

### Manual Testing

1. Prepare CSV file:
```csv
firstName,lastName,email,enrollmentCode
Test,User,test.user@udla.edu.ec,202399999
```

2. Import via Postman:
```bash
curl -X POST http://localhost:8082/api/markenx/students/bulk-import \
  -H "Authorization: Bearer {admin-token}" \
  -F "file=@students.csv"
```

3. Verify in Keycloak:
   - Navigate to: http://localhost:8180/admin/master/console/#/markenx/users
   - Search for: `test.user@udla.edu.ec`
   - Verify role: `STUDENT`

4. Test Login:
```bash
curl -X POST http://localhost:8180/realms/markenx/protocol/openid-connect/token \
  -d "client_id=markenx-web" \
  -d "username=test.user@udla.edu.ec" \
  -d "password=ChangeMe123!" \
  -d "grant_type=password"
```

### Integration Test Scenarios

1. **Happy Path**: Valid CSV, no duplicates → All students created
2. **Validation Error**: Invalid email → Import rejected with error details
3. **Duplicate in DB**: Email exists in database → Import rejected
4. **Duplicate in Keycloak**: Email exists in Keycloak → Import rejected
5. **Empty CSV**: No records → Exception thrown
6. **Malformed CSV**: Invalid format → Exception thrown

## Related Documentation

- [Authentication Flow](./AUTHENTICATION.md) - How students authenticate after creation
- [Keycloak Setup](./KEYCLOAK_SETUP.md) - Keycloak configuration
- [Architecture](./ARCHITECTURE.md) - Overall system architecture
- [Bulk Import Refactoring](./BULK_IMPORT_REFACTORING.md) - Implementation details

## Future Enhancements

1. **Custom Passwords**: Allow admins to specify initial passwords
2. **Email Notifications**: Send welcome email with credentials
3. **Saga Pattern**: Implement distributed transaction rollback
4. **Async Processing**: Handle large CSV imports asynchronously
5. **Progress Tracking**: Real-time import status updates
6. **Dry Run Mode**: Validate CSV without creating users
