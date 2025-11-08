# MarkenX Service - Architecture Guide

## Project Vision & Domain

**Purpose**: Backend service for a Unity-based educational web game focused on **consumer conduct**.

### System Components

1. **Unity Game** (Web-based)
   - Educational game about consumer conduct
   - Loaded by students in their web portal
   - Receives game configurations (scenarios) from backend
   - Sends game results back to backend

2. **Student Portal** (Angular Web App)
   - Students log in to access their personalized content
   - Loads Unity game (like FRIV platform)
   - Students play configured game scenarios
   - View their scores and progress

3. **Admin Portal** (Angular Web App)
   - Admins create/manage students
   - Configure game scenarios (tasks, parameters, rules)
   - Assign scenarios to students
   - View student performance and analytics

4. **Backend Service** (This Project - Spring Boot)
   - Manages authentication (via Keycloak)
   - Stores game configurations and student data
   - Provides REST APIs for both portals
   - Tracks game sessions and results

### Domain Concepts

- **Student**: Person who plays the game
- **Scenario/Configuration**: Game setup created by admin (tasks, rules, parameters)
- **Round/Session**: A student playing a specific scenario (one game instance)
- **Assignment**: Linking a scenario to a student with due dates
- **Attempt**: Student's result after completing a round (score, time, etc.)

### Use Cases

**Admin:**
- Create/edit/delete students (single or bulk via CSV)
- Create game scenarios (configurations)
- Assign scenarios to students
- View student performance

**Student:**
- Log in and view profile
- See assigned scenarios
- Load Unity game with scenario configuration
- Submit game results
- View own scores and history

## Architecture Pattern: Hexagonal Architecture (Ports & Adapters)

### What is Hexagonal Architecture?

Hexagonal Architecture, also known as **Ports and Adapters**, is an architectural pattern that promotes separation of concerns by organizing code into distinct layers:

```
┌─────────────────────────────────────────────────────────┐
│                    Infrastructure                        │
│  ┌────────────────────────────────────────────────┐     │
│  │              Application Layer                 │     │
│  │  ┌──────────────────────────────────────┐     │     │
│  │  │         Domain Layer (Core)          │     │     │
│  │  │  - Pure business logic               │     │     │
│  │  │  - Domain models                     │     │     │
│  │  │  - Value objects                     │     │     │
│  │  │  - Domain exceptions                 │     │     │
│  │  └──────────────────────────────────────┘     │     │
│  │  - Use cases                                   │     │
│  │  - Ports (interfaces)                          │     │
│  │  - DTOs & Mappers                              │     │
│  └────────────────────────────────────────────────┘     │
│  - Adapters (implementations)                            │
│    - REST controllers (inbound)                          │
│    - JPA repositories (outbound)                         │
│    - External services (outbound)                        │
└─────────────────────────────────────────────────────────┘
```

### Key Principles

#### 1. **Domain Purity**
The **domain layer** (`core/`) contains pure business logic with zero dependencies on frameworks or external systems:
- ✅ Domain models like `Student`, `Task`, `Attempt`
- ✅ Value objects like `Score`, `RangeDate`
- ✅ Domain exceptions like `InvalidEntityException`
- ❌ NO JPA annotations, Keycloak IDs, or REST concerns

**Example**: The `Student` domain model does NOT contain `keycloakUserId` because Keycloak is an infrastructure concern. Only the `StudentJpaEntity` (infrastructure layer) stores this field.

#### 2. **Ports (Interfaces)**
Ports define contracts between layers without implementation details:
- **Inbound Ports**: Define what the application can do (e.g., `StudentControllerPort`)
- **Outbound Ports**: Define what the application needs (e.g., `StudentRepositoryPort`, `KeycloakAdminPort`)

**Location**: `application/ports/in/` and `application/ports/out/`

#### 3. **Adapters (Implementations)**
Adapters implement ports and handle infrastructure concerns:
- **Inbound Adapters**: REST controllers in `infrastructure/in/api/rest/controllers/`
- **Outbound Adapters**: Repository adapters in `infrastructure/out/persistance/repositories/jpa/adapters/`, external service clients in `infrastructure/out/clients/`

**Example Flow**:
```
HTTP Request → AdminController (adapter)
            ↓
          CreateStudentUseCase (application)
            ↓
          StudentRepositoryPort (interface)
            ↓
          StudentRepositoryAdapter (adapter)
            ↓
          StudentJpaRepository (JPA)
            ↓
          MySQL Database
```

#### 4. **Separation of Domain and Persistence**
- **Domain Models** (`core/models/`): Pure business objects (e.g., `Student`)
- **JPA Entities** (`infrastructure/out/persistance/repositories/jpa/entities/`): Database-specific objects (e.g., `StudentJpaEntity`)
- **Mappers** bridge the gap between them

**Why?**
- Domain models can change for business reasons without breaking database schema
- Database schema can evolve without polluting business logic
- Easier to test domain logic in isolation

### The Authentication System ID Pattern

One critical example of domain purity is handling external system identifiers:

**❌ Wrong Approach** (violates domain purity):
```java
// DON'T DO THIS - Domain coupled to specific technology
public class Student extends Person {
    private String keycloakUserId; // ← Keycloak is infrastructure!
}

public interface StudentRepositoryPort {
    Student save(Student student); // ← Where does keycloakUserId come from?
}
```

**✅ Correct Approach** (maintains domain purity):
```java
// Domain Model (pure business logic - technology agnostic)
public class Student extends Person {
    private String email;
    // No external authentication system IDs here
}

// JPA Entity (infrastructure layer - knows about Keycloak)
@Entity
public class StudentJpaEntity {
    @Column(name = "keycloak_user_id")
    private String keycloakUserId; // ← Infrastructure concern isolated
}

// Port uses generic terminology (interface in application layer)
public interface StudentRepositoryPort {
    Student save(Student student, String externalAuthId);
    // ↑ Generic name, not tied to Keycloak
}

// Use Case uses abstraction (application layer)
public class CreateStudentUseCase {
    private final AuthenticationServicePort authService; // ← Generic port
    // NOT: KeycloakAdminPort
    
    public Student execute(CreateStudentRequestDTO request) {
        String authUserId = authService.createUser(...);
        // ↑ No mention of Keycloak
        return studentRepository.save(student, authUserId);
    }
}

// Adapter Implementation (infrastructure layer - Keycloak specific)
@Component
public class KeycloakAdminAdapter implements AuthenticationServicePort {
    // This is the ONLY place that knows about Keycloak
}

public class StudentRepositoryAdapter implements StudentRepositoryPort {
    public Student save(Student student, String externalAuthId) {
        // Map externalAuthId to keycloakUserId in JPA entity
        StudentJpaEntity entity = StudentMapper.toEntity(student, externalAuthId);
        StudentJpaEntity saved = repository.save(entity);
        return StudentMapper.toDomain(saved);
    }
}
```

**Benefits**:
1. **Domain model remains pure** - No technology coupling
2. **Use cases are technology-agnostic** - Express business intent, not implementation details
3. **Easy to swap providers** - Replace Keycloak with Auth0, Cognito, etc. without changing domain/application layers
4. **Better testability** - Mock `AuthenticationServicePort`, not Keycloak-specific APIs
5. **Clearer separation** - Only infrastructure layer knows about specific technologies

## Domain-Driven Design (DDD) Concepts

### Value Objects

Value objects encapsulate business rules and validation:
- `Score`: Enforces 0.0-1.0 range validation
- `RangeDate`: Ensures start date is before end date
- `Timestamps`: Audit fields (createdAt, updatedAt)

**Pattern**: Immutable Java records with validation in constructors.

```java
public record Score(double value) {
    public Score {
        if (value < 0.0 || value > 1.0) {
            throw new InvalidValueException("Score must be between 0.0 and 1.0");
        }
    }
}
```

### Aggregates (Planned Improvement)

Currently, entities like `AcademicPeriod`, `Course`, and `Task` act as aggregates but lack clear boundaries. Future improvements will:
- Define `AcademicPeriod` as aggregate root
- Enforce consistency rules at aggregate level (e.g., max attempts per task)
- Use domain events for inter-aggregate communication

### Domain Events (Future Feature)

Planned domain events for important state changes:
- `GameAttemptCompletedEvent` when student finishes a game scenario
- `StudentEnrolledEvent` when CSV import succeeds
- `PasswordChangeRequiredEvent` for first-time login

## Layer Responsibilities

### Core Layer (`core/`)
**Purpose**: Pure business logic, zero external dependencies

**Contains**:
- `models/`: Domain entities (Student, Task, Attempt, Course, etc.)
- `valueobjects/`: Immutable value objects with validation
- `exceptions/`: Custom domain exceptions
- `interfaces/`: Domain-level interfaces
- `utils/validators/`: Validation utilities

**Rules**:
- ❌ NO framework annotations (Spring, JPA, etc.)
- ❌ NO infrastructure IDs (Keycloak, AWS, etc.)
- ✅ Validate in constructors
- ✅ Throw domain exceptions

### Application Layer (`application/`)
**Purpose**: Orchestrate use cases and coordinate domain logic

**Contains**:
- `usecases/`: Business workflows (CreateStudentUseCase, etc.)
- **`services/`**: **Application Services (Facades)** - Orchestrate multiple use cases
- `ports/`: Interface definitions for inbound/outbound communication
- `dtos/`: Data Transfer Objects for API communication
- `builders/`: Fluent builders for creating domain objects
- `factories/`: Complex object creation logic
- `seeders/`: Data initialization for development

**Rules**:
- ✅ Coordinate domain models and ports
- ✅ Handle transactions and rollbacks
- ✅ Map between domain models and DTOs
- ✅ **Services act as facades** for controllers (DDD best practice)
- ❌ NO direct database or HTTP concerns

**Important Pattern - Application Services:**
```java
// ✅ CORRECT: Controller uses Service (facade)
@RestController
public class AdminController {
    private final StudentManagementService studentService; // ONE dependency
    
    public ResponseEntity<?> createStudent(CreateStudentRequestDTO request) {
        Student student = studentService.createStudent(request);
        return ResponseEntity.ok(StudentMapper.toDto(student));
    }
}

// Application Service orchestrates use cases
@Service
public class StudentManagementService {
    private final CreateStudentUseCase createStudentUseCase;
    private final GetAllStudentsUseCase getAllStudentsUseCase;
    // ... other use cases
    
    public Student createStudent(CreateStudentRequestDTO request) {
        return createStudentUseCase.execute(request);
    }
}

// ❌ WRONG: Controller directly injects multiple use cases
@RestController
public class AdminController {
    private final CreateStudentUseCase createStudentUseCase;
    private final GetAllStudentsUseCase getAllStudentsUseCase;
    private final UpdateStudentUseCase updateStudentUseCase;
    // Too many dependencies!
}
```

### Infrastructure Layer (`infrastructure/`)
**Purpose**: Implement adapters and handle external systems

**Contains**:
- `in/api/rest/controllers/`: REST API endpoints
- `out/persistance/repositories/jpa/`: Database persistence
  - `entities/`: JPA entities
  - `adapters/`: Repository implementations
  - `mappers/`: Entity ↔ Domain mapping
- `out/clients/`: External service clients (Keycloak, etc.)
- `config/`: Spring configuration classes

**Rules**:
- ✅ Implement ports from application layer
- ✅ Handle framework-specific concerns (JPA, REST, etc.)
- ✅ Manage external system IDs and mappings
- ❌ NO business logic here

## Common Patterns

### Mapper Pattern

**Purpose**: Convert between different object representations

**Types**:
1. **Infrastructure Mappers**: JPA Entity ↔ Domain Model
   - Location: `infrastructure/out/persistance/repositories/jpa/mappers/`
   - Example: `StudentMapper.toDomain(entity)`, `StudentMapper.toEntity(domain, keycloakUserId)`

2. **Application Mappers**: Domain Model → DTO
   - Location: `application/dtos/mappers/`
   - Example: `StudentMapper.toResponseDTO(domain)`

**Rules**:
- Static utility classes with private constructors
- Throw `UtilityClassInstantiationException` if instantiated
- Infrastructure mappers may accept external IDs as parameters

### Builder Pattern

**Purpose**: Fluent API for creating complex domain objects

**Location**: `application/builders/`

**Example**:
```java
Student student = studentBuilder
    .reset()
    .randomFirstName()
    .randomLastName()
    .withEmail("student@example.com")
    .build();
```

### Factory Pattern

**Purpose**: Orchestrate builders to create complete aggregates

**Location**: `application/factories/`

**Example**: `RandomAcademicPeriodFactory` creates `AcademicPeriod` with nested `Course` and `Task` objects.

## Testing Strategy

### Unit Tests
- Test domain models in isolation
- Mock ports using Mockito
- Verify business logic without infrastructure

**Example**:
```java
@ExtendWith(MockitoExtension.class)
class CreateStudentUseCaseTest {
    @Mock
    private StudentRepositoryPort studentRepository;
    
    @Mock
    private KeycloakAdminPort keycloakAdmin;
    
    @InjectMocks
    private CreateStudentUseCase useCase;
}
```

### Integration Tests
- Use `@SpringBootTest` with H2 in-memory database
- Test adapter implementations with real Spring context
- Use Hamcrest matchers for assertions

**Example**:
```java
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
class StudentRepositoryAdapterTest {
    @Autowired
    private StudentRepositoryPort studentRepository;
}
```

### Controller Tests
- Use `@WebMvcTest` for isolated controller testing
- Mock use cases
- Verify HTTP interactions

**Example**:
```java
@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
```

## Dependency Injection

**Rule**: Constructor injection only (no field or setter injection)

```java
@Service
public class CreateStudentUseCase {
    private final StudentRepositoryPort studentRepository;
    private final KeycloakAdminPort keycloakAdmin;
    
    public CreateStudentUseCase(
        StudentRepositoryPort studentRepository,
        KeycloakAdminPort keycloakAdmin
    ) {
        this.studentRepository = studentRepository;
        this.keycloakAdmin = keycloakAdmin;
    }
}
```

## Exception Handling

### Domain Exceptions
All domain exceptions extend `DomainException`:
- `InvalidEntityException`: Invalid entity state
- `NullFieldException`: Required field is null
- `InvalidValueException`: Invalid value object
- `EntityNotFoundException`: Entity not found

**Pattern**: Throw from domain model constructors or methods

```java
public Student(String firstName, String lastName) {
    if (firstName == null || firstName.isBlank()) {
        throw new NullFieldException("First name cannot be null or blank");
    }
    // ...
}
```

### Global Exception Handler
`ControllerExceptionHandler` catches domain exceptions and returns appropriate HTTP responses:

```java
@ExceptionHandler(InvalidEntityException.class)
public ResponseEntity<ErrorResponse> handleInvalidEntity(InvalidEntityException ex) {
    return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
}
```

## Best Practices

### DO ✅
1. Keep domain models pure (no infrastructure dependencies)
2. Use value objects for validated primitives (Score, RangeDate)
3. Validate in domain model constructors
4. Use constructor injection for dependencies
5. Write tests for all layers (domain, application, infrastructure)
6. Use mappers to bridge layers
7. Pass infrastructure IDs as method parameters, not in domain models

### DON'T ❌
1. Put JPA annotations in domain models
2. Put business logic in adapters or controllers
3. Mix domain models with JPA entities
4. Use field or setter injection
5. Skip validation in domain layer
6. Expose domain models directly in REST APIs
7. Store external system IDs in domain models

## Reference Architectures

For deeper understanding of these patterns:
- **Spring Modulith**: https://github.com/spring-projects/spring-modulith
- **Buckpal**: https://github.com/thombergs/buckpal
- **DDD Sample**: https://github.com/citerus/dddsample-core
- **Modern Bank API**: https://github.com/idugalic/digital-restaurant

## Future Improvements

### High Priority
1. Increase test coverage to >60%
2. Define clear aggregate boundaries
3. Implement domain events
4. Refactor to explicit use case pattern

### Medium Priority
1. Enrich repository ports with domain-specific queries
2. Add comprehensive API documentation (SpringDoc OpenAPI)
3. Implement CSV bulk import

### Low Priority
1. Enable Testcontainers for production-like tests
2. Add performance monitoring
3. Implement caching strategy

## Questions?

If you're unsure about where to put code:
- **Business rule?** → Domain layer (`core/`)
- **Workflow/orchestration?** → Application layer (`application/usecases/`)
- **Database/REST/External system?** → Infrastructure layer (`infrastructure/`)

When in doubt, follow the dependency rule: **Dependencies flow inward** (Infrastructure → Application → Domain).
