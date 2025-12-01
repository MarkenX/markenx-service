# Ports & Adapters Pattern - Controller Refactoring

## Overview

This document describes the refactoring of all REST controllers to properly follow the **Ports & Adapters (Hexagonal Architecture)** pattern. All controllers in the infrastructure layer now implement interfaces (ports) from the application layer, with documentation residing only in the ports.

## Architectural Principle

**Rule**: Every controller in `infrastructure/in/api/rest/controllers` must implement a port interface from `application/ports/in/api/rest/controllers`.

- **Ports (Interfaces)**: Define the contract + contain all Javadoc documentation
- **Adapters (Controllers)**: Implement the contract + contain only `@Override` annotations

## Changes Made

### 1. Created New Ports

#### AdminControllerPort
**File**: `application/ports/in/api/rest/controllers/AdminControllerPort.java`

**Methods**:
- `debugAuth(Authentication)` - Debug authentication endpoint
- `createStudent(CreateStudentRequestDTO)` - Create new student
- `getAllStudents(Pageable)` - List all students with pagination
- `getStudentById(Long)` - Get student by ID
- `updateStudent(Long, UpdateStudentRequestDTO)` - Update existing student
- `deleteStudent(Long)` - Delete student
- `bulkImportStudents(MultipartFile)` - Import students from CSV (all-or-nothing)

**Documentation**: Complete Javadoc for all methods including parameters, return values, and exceptions.

---

#### TaskControllerPort
**File**: `application/ports/in/api/rest/controllers/TaskControllerPort.java`

**Methods**:
- `getTaskAttempts(Long, int, int)` - Get paginated attempts for a task

**Documentation**: Describes task operations including attempt retrieval.

---

### 2. Enhanced Existing Ports

#### StudentControllerPort
**File**: `application/ports/in/api/rest/controllers/StudentControllerPort.java`

**Added**:
- `getCurrentProfile(Authentication)` - Get authenticated student profile
- Class-level Javadoc
- Method-level Javadoc for all methods

**Existing**:
- `getTasksByFilters(...)` - Already existed, now documented

---

#### AssignmentControllerPort
**File**: `application/ports/in/api/rest/controllers/AssignmentControllerPort.java`

**Added**:
- Class-level Javadoc describing assignment operations
- Method-level Javadoc for `getAssignmentStatus()`

---

#### ControllerExceptionHandlerPort
**File**: `application/ports/in/api/rest/controllers/ControllerExceptionHandlerPort.java`

**Added**:
- Complete interface with all exception handler methods
- Class-level Javadoc
- Method-level Javadoc for all 10 exception handlers:
  - `handleInvalidEntity(InvalidEntityException)` - HTTP 400
  - `handleEntityNotFound(EntityNotFoundException)` - HTTP 404
  - `handleResourceNotFound(ResourceNotFoundException)` - HTTP 404
  - `handleInvalidEmail(InvalidEmailException)` - HTTP 400
  - `handleDuplicateResource(DuplicateResourceException)` - HTTP 409
  - `handleBulkImport(BulkImportException)` - HTTP 400
  - `handleConstraintViolation(ConstraintViolationException)` - HTTP 400
  - `handleValidationErrors(MethodArgumentNotValidException)` - HTTP 400
  - `handleTypeMismatch(MethodArgumentTypeMismatchException)` - HTTP 400
  - `handleGenericException(Exception)` - HTTP 500

---

### 3. Updated Controller Adapters

All controllers updated to:
1. **Implement their respective port interface**
2. **Remove class-level Javadoc** (moved to port)
3. **Remove method-level Javadoc** (moved to port)
4. **Add `@Override` annotations** to all implemented methods
5. **Keep only Spring annotations** (`@RestController`, `@RequestMapping`, `@GetMapping`, etc.)

#### AdminController
```java
// BEFORE
@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  /**
   * Creates a new student.
   * @param request DTO containing...
   * @return ResponseEntity with...
   */
  @PostMapping("/students")
  public ResponseEntity<StudentResponseDTO> createStudent(...) { ... }
}

// AFTER
@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController implements AdminControllerPort {
  @Override
  @PostMapping("/students")
  public ResponseEntity<StudentResponseDTO> createStudent(...) { ... }
}
```

#### StudentController
```java
// BEFORE
/**
 * REST controller for student operations.
 */
@RestController
@RequestMapping("/api/markenx/students")
@Validated
public class StudentController { ... }

// AFTER
@RestController
@RequestMapping("/api/markenx/students")
@Validated
public class StudentController implements StudentControllerPort { ... }
```

#### TaskController
```java
// BEFORE
@RestController
@RequestMapping("/api/markenx")
public class TaskController { ... }

// AFTER
@RestController
@RequestMapping("/api/markenx")
public class TaskController implements TaskControllerPort { ... }
```

#### AssignmentController
```java
// BEFORE
@RestController
@RequestMapping("/api/markenx")
public class AssignmentController implements AssignmentControllerPort {
  @GetMapping("/assignments/status")
  public ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus() { ... }
}

// AFTER
@RestController
@RequestMapping("/api/markenx")
public class AssignmentController implements AssignmentControllerPort {
  @Override
  @GetMapping("/assignments/status")
  public ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus() { ... }
}
```

#### ControllerExceptionHandler
```java
// BEFORE
/**
 * Global exception handler for all controllers.
 * Provides consistent error responses...
 */
@ControllerAdvice
public class ControllerExceptionHandler { ... }

// AFTER
@ControllerAdvice
public class ControllerExceptionHandler implements ControllerExceptionHandlerPort { ... }
```

---

## Architecture Compliance

### ✅ Ports (Application Layer)
**Location**: `src/main/java/com/udla/markenx/application/ports/in/api/rest/controllers/`

**Files**:
- `AdminControllerPort.java` (NEW)
- `TaskControllerPort.java` (NEW)
- `StudentControllerPort.java` (ENHANCED)
- `AssignmentControllerPort.java` (ENHANCED)
- `ControllerExceptionHandlerPort.java` (ENHANCED)

**Responsibilities**:
- ✅ Define method signatures (contracts)
- ✅ Contain all Javadoc documentation
- ✅ Declare exceptions
- ✅ Use DTO types from application layer
- ✅ No implementation details
- ✅ No Spring annotations (except validation annotations in parameters)

---

### ✅ Adapters (Infrastructure Layer)
**Location**: `src/main/java/com/udla/markenx/infrastructure/in/api/rest/controllers/`

**Files**:
- `AdminController.java` (UPDATED)
- `TaskController.java` (UPDATED)
- `StudentController.java` (UPDATED)
- `AssignmentController.java` (UPDATED)
- `ControllerExceptionHandler.java` (UPDATED)

**Responsibilities**:
- ✅ Implement port interfaces
- ✅ Add Spring annotations (`@RestController`, `@GetMapping`, etc.)
- ✅ Add security annotations (`@PreAuthorize`)
- ✅ Inject services and call business logic
- ✅ Use `@Override` for all port methods
- ❌ No Javadoc (moved to ports)
- ❌ No business logic (delegated to services)

---

## Benefits

### 1. Clear Separation of Concerns
- **Ports**: Define "what" operations are available (application layer)
- **Adapters**: Define "how" operations are exposed (infrastructure layer)

### 2. Technology Independence
- Ports can be implemented by different adapters (REST, GraphQL, gRPC, CLI)
- Business logic doesn't depend on HTTP/REST concepts

### 3. Testability
- Ports can be mocked in service tests
- Adapters can be tested with `@WebMvcTest`
- Controllers are now integration points, not business logic

### 4. Documentation Centralization
- All API documentation in one place (ports)
- Adapters remain clean and focused on technical details
- No duplicate documentation

### 5. Maintainability
- Changes to documentation → update port only
- Changes to implementation → update adapter only
- Clear contract between layers

---

## Compliance Checklist

### For Every New Controller:

1. ✅ Create port interface in `application/ports/in/api/rest/controllers/`
   - Add class-level Javadoc
   - Add method-level Javadoc for all methods
   - Use DTOs from application layer

2. ✅ Create adapter in `infrastructure/in/api/rest/controllers/`
   - Implement port interface
   - Add Spring annotations
   - Add `@Override` to all methods
   - Remove all Javadoc
   - Inject services, don't implement business logic

3. ✅ For exception handlers:
   - Define all handler methods in `ControllerExceptionHandlerPort`
   - Document exception types and HTTP status codes
   - Implement with `@ExceptionHandler` in adapter

---

## Code Examples

### Port Example
```java
/**
 * Port for student operations.
 * 
 * Provides endpoints for students to access their data and assigned tasks.
 */
public interface StudentControllerPort {

  /**
   * Retrieves the profile of the currently authenticated student.
   * 
   * @param authentication the authentication object from JWT token
   * @return ResponseEntity with student profile DTO
   */
  ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication);
}
```

### Adapter Example
```java
@RestController
@RequestMapping("/api/markenx/students")
@Validated
public class StudentController implements StudentControllerPort {

  private final StudentManagementService studentManagementService;

  public StudentController(StudentManagementService studentManagementService) {
    this.studentManagementService = studentManagementService;
  }

  @Override
  @GetMapping("/me")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<StudentResponseDTO> getCurrentProfile(Authentication authentication) {
    String email = authentication.getName();
    Student student = studentManagementService.getCurrentStudentProfile(email);
    StudentResponseDTO response = StudentMapper.toDto(student);
    return ResponseEntity.ok(response);
  }
}
```

---

## Files Modified

### Ports Created (2 new)
- `AdminControllerPort.java`
- `TaskControllerPort.java`

### Ports Enhanced (3 existing)
- `StudentControllerPort.java`
- `AssignmentControllerPort.java`
- `ControllerExceptionHandlerPort.java`

### Adapters Updated (5 existing)
- `AdminController.java`
- `TaskController.java`
- `StudentController.java`
- `AssignmentController.java`
- `ControllerExceptionHandler.java`

**Total**: 10 files modified/created

---

## Build Status

✅ **Compilation Successful**
```
[INFO] Compiling 118 source files with javac
[INFO] BUILD SUCCESS
```

No errors, only warnings about unused imports (unrelated to this refactoring).

---

## Future Considerations

### When Adding New Endpoints:

1. **Define in Port First**:
   ```java
   public interface AdminControllerPort {
     ResponseEntity<Void> newOperation(SomeRequestDTO request);
   }
   ```

2. **Implement in Adapter**:
   ```java
   public class AdminController implements AdminControllerPort {
     @Override
     @PostMapping("/new-operation")
     public ResponseEntity<Void> newOperation(@RequestBody SomeRequestDTO request) {
       // Implementation
     }
   }
   ```

### When Refactoring Existing Controllers:

1. Extract interface from controller
2. Move all Javadoc to interface
3. Remove Javadoc from controller
4. Add `implements PortInterface` to controller
5. Add `@Override` annotations

---

## References

- **Hexagonal Architecture**: Alistair Cockburn
- **Ports & Adapters**: Also known as Hexagonal Architecture
- **DDD**: Domain-Driven Design by Eric Evans
- **Spring Boot Best Practices**: Clean Architecture

---

## Summary

This refactoring brings the codebase to **100% compliance** with the Ports & Adapters pattern for inbound REST controllers. All controllers now properly implement port interfaces, with documentation centralized in the application layer and implementation details isolated in the infrastructure layer.
