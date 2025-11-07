# MarkenX Service - AI Coding Assistant Instructions

## Project Vision & Domain

**Purpose**: Backend service for a Unity-based educational web game focused on **consumer conduct**. The system manages:
- Game configuration (scenarios, tasks, assignments) created by admins
- Student authentication and session data
- Game statistics (attempts, scores, results) sent from Unity client
- CSV bulk import for student enrollment

**Key Use Cases**:
1. Admin creates configurable game scenarios (tasks with parameters)
2. Students authenticate via email/password (first-time password change required)
3. Unity game loads configurations from this service
4. Game sends attempt statistics back to service
5. Admin analyzes student performance data

## Architecture Overview

**Architecture Style**: **Hexagonal/Ports & Adapters** (simplified DDD approach)
- Current implementation is **80% aligned** with Hexagonal Architecture best practices
- Focus on **separation of concerns** and **testability** (target: >60% coverage)

### Layer Structure

```
core/           → Domain models, value objects, exceptions (pure business logic)
application/    → Use cases, ports (interfaces), builders, factories, seeders
infrastructure/ → Adapters (REST controllers, JPA repositories, data generators)
  ├── in/       → Inbound adapters (REST API)
  └── out/      → Outbound adapters (persistence, data generation)
```

**Critical Pattern**: Domain models (`core/models/`) are **separate** from JPA entities (`infrastructure/out/persistance/repositories/jpa/entities/`). Never mix them - always use mappers.

## Key Architectural Decisions

### Current Architecture Assessment

**Strengths** ✅:
- Clear separation between domain and infrastructure layers
- Ports & Adapters pattern properly implemented with interfaces
- Value objects enforce business invariants in constructors
- Domain exceptions provide meaningful error messages
- Mappers separate concerns between layers

**Areas for Improvement** ⚠️:
- **Services are thin** - Only 2 services exist (`TaskService`, `StudentService`). Consider consolidating business logic here instead of in domain constructors
- **Missing aggregate roots** - `AcademicPeriod`, `Course`, `Task` act as aggregates but lack clear boundaries and consistency rules
- **Missing domain events** - No event publishing for important state changes (e.g., attempt completion, student enrollment)
- **Repository ports are anemic** - Consider richer query methods aligned with use cases
- **No application services layer** - Services live in `application/` but act more like facades than use case orchestrators

**DDD Alignment Suggestions**:
1. **Introduce Application Services**: Create dedicated use case classes (e.g., `CreateGameScenarioUseCase`, `EnrollStudentUseCase`) instead of generic services
2. **Define Aggregates Clearly**: `AcademicPeriod` should be root aggregate containing `Course` → `Task` → `Attempt` hierarchy
3. **Add Domain Events**: Publish events for significant business actions (attempt completed, task assigned, etc.)
4. **Enrich Services**: Move complex business logic from domain constructors to application services
5. **Align with Ubiquitous Language**: Rename entities to match domain language (e.g., `Task` → `GameScenario`, `Attempt` → `GameSession`)

### Ports & Adapters Implementation

- **Ports** are interfaces in `application/ports/`:
  - `in/api/rest/controllers/` - Inbound ports (e.g., `StudentControllerPort`, `AssignmentControllerPort`)
  - `out/persistance/repositories/` - Repository ports (e.g., `AttemptRepositoryPort`, `TaskRepositoryPort`)
  - `out/data/random/generators/` - Data generator ports (e.g., `RandomStudentDataGeneratorPort`)

- **Adapters** implement ports:
  - Inbound: Controllers in `infrastructure/in/api/rest/controllers/` implement controller ports
  - Outbound: `*RepositoryAdapter` in `infrastructure/out/persistance/repositories/jpa/adapters/` implement repository ports
  - JPA repositories (`*JpaRepository`) extend `JpaRepository` and are injected into adapters

**Example Flow**:
```
HTTP Request → Controller (adapter) → Service (use case) → RepositoryPort (interface) 
  → RepositoryAdapter → JpaRepository → Database
```

### Domain Model Pattern

Domain models (`core/models/`) use **value objects** from `core/valueobjects/`:
- `Score` - Validated score (0.0-1.0 range) as a Java record
- `RangeDate` - Date range validation
- `Timestamps` - Audit fields
- Enums in `core/valueobjects/enums/` (e.g., `AttemptStatus`, `AttemptResult`, `AssignmentStatus`)

**Validation happens in constructors** of domain models using validators from `core/utils/validators/`. Throws custom exceptions from `core/exceptions/` (all extend `DomainException`).

**⚠️ DDD Purity**: Domain models must NOT contain infrastructure concerns:
- ❌ No Keycloak IDs, OAuth tokens, or external system references
- ❌ No JPA annotations or persistence-specific fields
- ✅ Only business logic and domain concepts
- ✅ Infrastructure adapters handle external system IDs (e.g., `keycloakUserId` in JPA entities only)

### Mapping Strategy

**Two types of mappers** (both using static methods):
1. **Infrastructure mappers** (`infrastructure/out/persistance/repositories/jpa/mappers/`): JpaEntity ↔ Domain model
2. **Application mappers** (`application/dtos/mappers/`): Domain model → DTO

Example flow: `AttemptJpaEntity` → `Attempt` (domain) → `AttemptResponseDTO`

Mappers are utility classes with private constructors that throw `UtilityClassInstantiationException`.

## Builder & Factory Pattern

- **Builders** (`application/builders/`) create domain models with fluent API, support random data via generator ports
  - Example: `TaskBuilder.reset().randomTitle().randomSummary().build()`
  
- **Factories** (`application/factories/`) orchestrate builders to create complete aggregates
  - Example: `RandomAcademicPeriodFactory` creates `AcademicPeriod` with nested `Course` objects

## Development Workflows

### Local Development

**Environment**: Uses Docker Compose with MySQL + Keycloak (authentication server)
```bash
# Start services (requires .env file with credentials)
docker compose -f docker-compose.dev.yml up -d

# Run with dev profile (enables seeders)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Windows alternative
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

**Profiles**:
- `dev` - Uses MySQL via Docker, runs `@Profile("dev")` seeders in `application/seeders/`, `ddl-auto=create-drop`
- `test` - Uses H2 in-memory database (configured in `application-test.properties`)
- `prod` - Production MySQL configuration

**Database**: Hibernate `ddl-auto=create-drop` in dev mode (⚠️ **data resets on restart**)

### Testing Strategy

**Current State**: Minimal test coverage - needs expansion to meet >60% goal

**Integration Tests**:
- Use `@SpringBootTest` with `@AutoConfigureTestDatabase(replace = Replace.ANY)` for H2
- Use Hamcrest matchers (`assertThat`, `is`, `notNullValue`, `hasSize`)
- Example: `TaskJpaRepositoryTest` in `src/test/java/com/udla/markenx/adapters/outbound/`

**Test Patterns to Implement**:
```java
// Repository tests
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
class XxxRepositoryTest { ... }

// Service/Use Case tests (unit)
@ExtendWith(MockitoExtension.class)
class XxxServiceTest {
    @Mock
    private XxxRepositoryPort repository;
    
    @InjectMocks
    private XxxService service;
}

// Controller tests (integration)
@WebMvcTest(XxxController.class)
class XxxControllerTest {
    @Autowired
    private MockMvc mockMvc;
}
```

**Note**: Testcontainers configuration exists in `TestcontainersConfiguration.java` but is commented out - consider enabling for production-like tests

### Build Pipeline (Jenkins)

See `Jenkinsfile` for CI/CD:
1. **Build**: `mvn clean package -DskipTests`
2. **Unit Tests**: `mvn test`
3. **Code Quality**: SonarQube analysis (reports to SonarCloud)
4. **Deployment**: Docker Compose deployment with injected `.env` secrets

**Coverage**: JaCoCo plugin configured in `pom.xml` - reports sent to SonarQube

## Code Conventions

### Exception Handling

- Domain exceptions extend `DomainException` (e.g., `InvalidEntityException`, `NullFieldException`, `InvalidValueException`)
- Global handler in `infrastructure/in/api/rest/controllers/ControllerExceptionHandler` with `@ExceptionHandler` methods
- Validation happens in domain constructors, not in services

### Dependency Injection

- **Constructor injection only** (no field or setter injection)
- Services annotated with `@Service`
- Repositories (adapters) with `@Repository`
- Builders/Factories with `@Component`

### Lombok Usage

- JPA entities use `@Getter`, `@Setter`, `@NoArgsConstructor`
- Domain models typically use `@Getter` only (immutability preferred)
- Avoid `@Data` - use specific annotations

### Naming Conventions

- Domain models: `Task`, `Attempt`, `Student` (no suffix)
- JPA entities: `TaskJpaEntity`, `AttemptJpaEntity` (suffix: `JpaEntity`)
- Repositories: `TaskJpaRepository` (interface), `TaskRepositoryAdapter` (implementation)
- DTOs: `AttemptResponseDTO` (suffix: `ResponseDTO`)
- Ports: `AttemptRepositoryPort`, `StudentControllerPort` (suffix: `Port`)

## Common Pitfalls

1. **Don't mix domain and persistence layers** - Never use JPA entities in services; always map to domain models
2. **Value objects enforce invariants** - Use `Score`, `RangeDate` instead of primitives for validated values
3. **Seeders only run in dev profile** - Check `@Profile("dev")` annotation in `application/seeders/`
4. **Pagination is standard** - Controllers return `Page<DTO>`, services work with `Page<DomainModel>`
5. **JavaFaker for test data** - Use `RandomXXXDataGeneratorPort` implementations that wrap JavaFaker
6. **Constructor validation** - Domain models validate in constructors and throw `DomainException` subtypes
7. **Mappers are utilities** - All mappers use static methods and throw `UtilityClassInstantiationException` if instantiated

## Improvement Roadmap

Based on DDD best practices and project goals:

### 🎯 High Priority (Maintainability & Testing)
1. **Increase test coverage to >60%**:
   - Add unit tests for all services using Mockito
   - Add integration tests for all repository adapters
   - Add controller tests using `@WebMvcTest` and MockMvc
   - Enable Testcontainers for production-like database tests

2. **Refactor to true Use Case pattern**:
   - Create `usecases/` package in `application/`
   - Convert services to orchestration-focused use cases
   - Example: `CreateGameScenarioUseCase`, `SubmitGameAttemptUseCase`, `EnrollStudentsFromCsvUseCase`

3. **Define clear aggregate boundaries**:
   - `AcademicPeriod` aggregate: Controls `Course` → `Task` → `Attempt` consistency
   - Enforce invariants at aggregate root (e.g., max attempts per task)
   - Use domain events for cross-aggregate communication

### 🔧 Medium Priority (DDD Alignment)
4. **Align naming with ubiquitous language**:
   - Consider renaming `Task` → `GameScenario` (matches "scenarios" in requirements)
   - Consider renaming `Attempt` → `GameSession` or `GameAttempt`
   - Validate names with domain experts (admins/teachers)

5. **Add domain events**:
   - `GameAttemptCompletedEvent` when student finishes scenario
   - `StudentEnrolledEvent` when CSV import succeeds
   - `PasswordChangeRequiredEvent` for first-time login
   - Use Spring's `ApplicationEventPublisher`

6. **Enrich repository ports**:
   - Add query methods like `findOverdueTasksForStudent(studentId, today)`
   - Add `findTopScoringAttemptsForTask(taskId, limit)`
   - Align with actual use cases instead of generic CRUD

### 🚀 Lower Priority (Infrastructure)
7. **Security implementation**: ✅ **COMPLETED**
   - ✅ Keycloak integration for authentication (Docker + auto-import realm)
   - ✅ Spring Security OAuth2 Resource Server configuration
   - ✅ Role-based access control (ADMIN, STUDENT)
   - ✅ JWT authentication with role extraction
   - ⚠️ TODO: Implement password change on first login UI flow

8. **CSV import feature**:
   - Create `ImportStudentsUseCase` with validation
   - Parse CSV using Apache Commons CSV or OpenCSV
   - Bulk insert with transaction management

9. **API documentation**:
   - Add SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`)
   - Document DTOs with `@Schema` annotations
   - Generate API docs at `/swagger-ui.html`

## Reference Architectures

For DDD/Hexagonal Architecture inspiration (4-5 star examples):
- **Spring Modulith**: https://github.com/spring-projects/spring-modulith (official Spring DDD guide)
- **Buckpal**: https://github.com/thombergs/buckpal (classic hexagonal architecture example)
- **DDD Sample**: https://github.com/citerus/dddsample-core (original DDD reference)
- **Modern Bank API**: https://github.com/idugalic/digital-restaurant (event-driven DDD)

## Quick Reference

- **Base package**: `com.udla.markenx`
- **Main class**: `MarkenxServiceApplication`
- **REST API prefix**: `/api/markenx`
- **Database**: MySQL 8.0 (Docker), H2 (tests)
- **Java version**: 17
- **Spring Boot version**: 3.5.7
