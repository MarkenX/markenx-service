package com.udla.markenx.classroom.application.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.udla.markenx.classroom.application.dtos.requests.BulkStudentImportDTO;
import com.udla.markenx.classroom.application.dtos.responses.AttemptResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentTaskWithDetailsResponseDTO;
import com.udla.markenx.classroom.application.dtos.responses.StudentWithCourseResponseDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.AcademicTermRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.CourseRepositoryPort;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.BulkImportException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;
import com.udla.markenx.classroom.domain.exceptions.ResourceNotFoundException;
import com.udla.markenx.classroom.domain.models.AcademicTerm;
import com.udla.markenx.classroom.domain.models.Attempt;
import com.udla.markenx.classroom.domain.models.Course;
import com.udla.markenx.classroom.domain.models.Student;
import com.udla.markenx.classroom.domain.models.StudentTask;
import com.udla.markenx.classroom.domain.models.Task;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.AttemptJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentAssignmentJpaRepository;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.AttemptMapper;
import com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.StudentTaskMapper;
import com.udla.markenx.shared.application.port.out.auth.AuthenticationServicePort;

/**
 * Service for student domain operations.
 * 
 * Handles student-related business logic including:
 * - Bulk importing students from CSV files
 * - Integration with Keycloak for user authentication
 */
@Service
public class StudentService {

  private static final String UDLA_EMAIL_PATTERN = ".*@udla\\.edu\\.ec$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(UDLA_EMAIL_PATTERN);
  private static final String DEFAULT_PASSWORD = "ChangeMe123!";
  private static final String STUDENT_ROLE = "STUDENT";

  private final StudentRepositoryPort studentRepository;
  private final CourseRepositoryPort courseRepository;
  private final AcademicTermRepositoryPort academicTermRepository;
  private final AuthenticationServicePort authenticationService;
  private final StudentAssignmentJpaRepository studentAssignmentRepository;
  private final AttemptJpaRepository attemptRepository;
  private final StudentTaskMapper studentTaskMapper;
  private final AttemptMapper attemptMapper;
  private final com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort taskRepository;
  private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository taskJpaRepository;
  private final com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository studentJpaRepository;

  public StudentService(
      StudentRepositoryPort studentRepository,
      CourseRepositoryPort courseRepository,
      AcademicTermRepositoryPort academicTermRepository,
      AuthenticationServicePort authenticationService,
      StudentAssignmentJpaRepository studentAssignmentRepository,
      AttemptJpaRepository attemptRepository,
      StudentTaskMapper studentTaskMapper,
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.mappers.AttemptMapper attemptMapper,
      com.udla.markenx.classroom.application.ports.out.persistance.repositories.TaskRepositoryPort taskRepository,
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.TaskJpaRepository taskJpaRepository,
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.interfaces.StudentJpaRepository studentJpaRepository) {
    this.studentRepository = studentRepository;
    this.courseRepository = courseRepository;
    this.academicTermRepository = academicTermRepository;
    this.authenticationService = authenticationService;
    this.studentAssignmentRepository = studentAssignmentRepository;
    this.attemptRepository = attemptRepository;
    this.studentTaskMapper = studentTaskMapper;
    this.attemptMapper = attemptMapper;
    this.taskRepository = taskRepository;
    this.taskJpaRepository = taskJpaRepository;
    this.studentJpaRepository = studentJpaRepository;
  }

  /**
   * Creates a single student.
   * 
   * Process:
   * 1. Validate email domain
   * 2. Check for duplicates in DB and Keycloak
   * 3. Create user in Keycloak with STUDENT role
   * 4. Save student in database with assigned course
   * 
   * @param firstName First name
   * @param lastName  Last name
   * @param email     Email (must be @udla.edu.ec)
   * @param password  Initial password
   * @param courseId  UUID of the course to enroll the student
   * @return Created student
   */
  @Transactional
  public Student createStudent(String firstName, String lastName, String email, String password, UUID courseId) {
    // Validate email
    validateUdlaEmail(email);

    // Check for duplicates
    if (studentRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("El email ya existe en la base de datos");
    }

    if (authenticationService.userExists(email)) {
      throw new IllegalArgumentException("El usuario ya existe en Keycloak");
    }

    // Get authenticated user as creator
    String createdBy = getAuthenticatedUsername();

    // Create user in Keycloak with STUDENT role
    authenticationService.createUser(
        email,
        password,
        firstName,
        lastName,
        STUDENT_ROLE,
        false // Don't require password change
    );

    // Create student domain object with enrolled course
    Student student = new Student(
        courseId,
        firstName,
        lastName,
        email);

    // Save to database
    Student savedStudent = studentRepository.save(student);

    // Assign all course tasks to the new student
    var tasks = taskRepository.findByCourseId(courseId);

    // Get the persisted student entity
    var studentEntity = studentJpaRepository.findAll().stream()
        .filter(s -> s.getExternalReference() != null &&
            s.getExternalReference().getPublicId().equals(savedStudent.getId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Student entity not found after save"));

    for (Task task : tasks) {
      // Get the persisted task entity
      var taskEntity = taskJpaRepository.findAll().stream()
          .filter(t -> t.getExternalReference() != null &&
              t.getExternalReference().getPublicId().equals(task.getId()))
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("Task entity not found"));

      // Create StudentTaskJpaEntity with persisted entity references
      com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity studentTaskEntity = new com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity();
      studentTaskEntity.setAssignment(taskEntity);
      studentTaskEntity.setStudent(studentEntity);
      studentTaskEntity
          .setCurrentStatus(com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus.NOT_STARTED);
      studentTaskEntity.setCreatedBy(createdBy);
      studentTaskEntity.setCreatedAt(java.time.LocalDateTime.now());
      studentTaskEntity.setUpdatedAt(java.time.LocalDateTime.now());
      studentTaskEntity.setStatus(com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED);

      // Create external reference
      var externalRef = new com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity();
      externalRef.setPublicId(java.util.UUID.randomUUID());
      externalRef.setCode("ST-" + savedStudent.getSerialNumber() + "-" + task.getId().toString().substring(0, 8));
      externalRef.setEntityType("StudentTask");
      studentTaskEntity.setExternalReference(externalRef);

      studentAssignmentRepository.save(studentTaskEntity);
    }

    return savedStudent;
  }

  /**
   * Disables a student.
   * 
   * This is a soft delete:
   * - Sets status to DISABLED in database
   * - Disables user in Keycloak (preserves authentication history)
   * - Student record remains but is not visible to non-admins
   * 
   * @param studentId Student UUID
   */
  @Transactional
  public void disableStudent(UUID studentId) {
    // Find student to get email
    Student student = studentRepository.findByIdIncludingDisabled(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + studentId));

    // Disable in database
    studentRepository.deleteById(studentId);

    // Disable in Keycloak
    authenticationService.disableUser(student.getAcademicEmail());
  }

  /**
   * Enables a previously disabled student.
   * 
   * - Sets status to ENABLED in database
   * - Enables user in Keycloak
   * 
   * @param studentId Student UUID
   */
  @Transactional
  public void enableStudent(UUID studentId) {
    // Find student to get email
    Student student = studentRepository.findByIdIncludingDisabled(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + studentId));

    // Enable in database
    student.enable();
    studentRepository.update(student);

    // Enable in Keycloak
    authenticationService.enableUser(student.getAcademicEmail());
  }

  /**
   * Imports students from CSV file.
   * 
   * CSV format (with header):
   * firstName,lastName,email
   * 
   * ALL students must be valid or NONE will be imported (transactional).
   * If ANY validation fails, the entire import is rolled back.
   * 
   * @param courseId UUID of the course to enroll all students
   * @param file     CSV file with student data
   * @return BulkImportResponseDTO with success message and count
   * @throws BulkImportException      if any student is invalid (contains all
   *                                  validation errors)
   * @throws IllegalArgumentException if file is empty or malformed
   */
  @Transactional
  public BulkImportResponseDTO importStudentsFromCsv(UUID courseId, MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("El archivo CSV está vacío");
    }

    try {
      Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

      CsvToBean<BulkStudentImportDTO> csvToBean = new CsvToBeanBuilder<BulkStudentImportDTO>(reader)
          .withType(BulkStudentImportDTO.class)
          .withIgnoreLeadingWhiteSpace(true)
          .withIgnoreEmptyLine(true)
          .build();

      List<BulkStudentImportDTO> students = csvToBean.parse();

      if (students.isEmpty()) {
        throw new IllegalArgumentException("El archivo CSV no contiene registros válidos");
      }

      return processStudentImports(courseId, students);

    } catch (BulkImportException ex) {
      throw ex; // Re-throw to preserve validation errors
    } catch (Exception ex) {
      throw new IllegalArgumentException("Error al procesar el archivo CSV: " + ex.getMessage(), ex);
    }
  }

  /**
   * Validates ALL students first, then imports them.
   * If any validation fails, throws BulkImportException with all errors.
   * 
   * Process:
   * 1. Validate all CSV data
   * 2. Check for duplicates in DB and Keycloak
   * 3. Create users in Keycloak with STUDENT role
   * 4. Save students in database assigned to specified course
   * 
   * @param courseId UUID of the course to enroll all students
   * @param students List of students from CSV
   */
  private BulkImportResponseDTO processStudentImports(UUID courseId, List<BulkStudentImportDTO> students) {
    int totalRecords = students.size();
    Map<Integer, String> validationErrors = new HashMap<>();

    // PHASE 1: Validate ALL students first
    for (int i = 0; i < students.size(); i++) {
      BulkStudentImportDTO studentDto = students.get(i);
      int rowNumber = i + 2; // +2 because row 1 is header and index is 0-based

      try {
        validateUdlaEmail(studentDto.getEmail());

        // Basic validation
        if (studentDto.getFirstName() == null || studentDto.getFirstName().trim().isEmpty()) {
          validationErrors.put(rowNumber, "El nombre es obligatorio");
          continue;
        }
        if (studentDto.getLastName() == null || studentDto.getLastName().trim().isEmpty()) {
          validationErrors.put(rowNumber, "El apellido es obligatorio");
          continue;
        }

        // Check for duplicates in database
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
          validationErrors.put(rowNumber, "El email ya existe en la base de datos");
          continue;
        }

        // Check for duplicates in Keycloak
        if (authenticationService.userExists(studentDto.getEmail())) {
          validationErrors.put(rowNumber, "El usuario ya existe en Keycloak");
          continue;
        }

      } catch (InvalidEmailException ex) {
        validationErrors.put(rowNumber, ex.getMessage());
      }
    }

    // If ANY validation failed, throw exception with ALL errors
    if (!validationErrors.isEmpty()) {
      throw new BulkImportException(
          "La importación falló: " + validationErrors.size() + " estudiante(s) con errores",
          totalRecords,
          validationErrors);
    }

    // PHASE 2: Create all students in Keycloak and database
    String createdBy = getAuthenticatedUsername();
    int successCount = 0;

    // Get all course tasks once
    var courseTasks = taskRepository.findByCourseId(courseId);

    for (BulkStudentImportDTO studentDto : students) {
      try {
        // Create user in Keycloak with STUDENT role
        authenticationService.createUser(
            studentDto.getEmail(),
            DEFAULT_PASSWORD,
            studentDto.getFirstName(),
            studentDto.getLastName(),
            STUDENT_ROLE,
            false // Don't require password change
        );

        // Create student domain object with enrolled course (from parameter)
        Student student = new Student(
            courseId,
            studentDto.getFirstName(),
            studentDto.getLastName(),
            studentDto.getEmail());

        // Save to database
        Student savedStudent = studentRepository.save(student);

        // Get the persisted student entity
        var studentEntity = studentJpaRepository.findAll().stream()
            .filter(s -> s.getExternalReference() != null &&
                s.getExternalReference().getPublicId().equals(savedStudent.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Student entity not found after save"));

        // Assign all course tasks to the new student
        for (Task task : courseTasks) {
          // Get the persisted task entity
          var taskEntity = taskJpaRepository.findAll().stream()
              .filter(t -> t.getExternalReference() != null &&
                  t.getExternalReference().getPublicId().equals(task.getId()))
              .findFirst()
              .orElseThrow(() -> new IllegalStateException("Task entity not found"));

          // Create StudentTaskJpaEntity with persisted entity references
          com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity studentTaskEntity = new com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities.StudentTaskJpaEntity();
          studentTaskEntity.setAssignment(taskEntity);
          studentTaskEntity.setStudent(studentEntity);
          studentTaskEntity
              .setCurrentStatus(com.udla.markenx.classroom.domain.valueobjects.enums.AssignmentStatus.NOT_STARTED);
          studentTaskEntity.setCreatedBy(createdBy);
          studentTaskEntity.setCreatedAt(java.time.LocalDateTime.now());
          studentTaskEntity.setUpdatedAt(java.time.LocalDateTime.now());
          studentTaskEntity.setStatus(com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus.ENABLED);

          // Create external reference
          var externalRef = new com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.ExternalReferenceJpaEntity();
          externalRef.setPublicId(java.util.UUID.randomUUID());
          externalRef.setCode("ST-" + savedStudent.getSerialNumber() + "-" + task.getId().toString().substring(0, 8));
          externalRef.setEntityType("StudentTask");
          studentTaskEntity.setExternalReference(externalRef);

          studentAssignmentRepository.save(studentTaskEntity);
        }

        successCount++;

      } catch (Exception ex) {
        // If any student fails after validation, this is a system error
        throw new RuntimeException(
            "Error al crear estudiante: " + studentDto.getEmail() + ". Causa: " + ex.getMessage(),
            ex);
      }
    }

    return BulkImportResponseDTO.success(successCount);
  }

  /**
   * Gets the profile of the currently authenticated student with course
   * information.
   * 
   * @return StudentWithCourseResponseDTO with student, course and academic term
   *         data
   */
  public StudentWithCourseResponseDTO getCurrentStudentProfile() {
    // Get authenticated user's email from SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Find student by email
    Student student = studentRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante", email));

    // Find course
    Course course = courseRepository.findById(student.getEnrolledCourseId())
        .orElseThrow(() -> new ResourceNotFoundException("Curso", student.getEnrolledCourseId()));

    // Find academic term
    AcademicTerm academicTerm = academicTermRepository.findById(course.getAcademicTermId())
        .orElseThrow(() -> new ResourceNotFoundException("Período académico", course.getAcademicTermId()));

    // Check if user is admin to include status fields
    boolean isAdmin = com.udla.markenx.shared.domain.util.SecurityUtils.isAdmin();

    // Build response DTO
    return StudentWithCourseResponseDTO.builder()
        .id(student.getId())
        .code(student.getCode())
        .firstName(student.getFirstName())
        .lastName(student.getLastName())
        .email(student.getAcademicEmail())
        .status(isAdmin ? student.getStatus().name() : null)
        .course(StudentWithCourseResponseDTO.CourseInfo.builder()
            .id(course.getId())
            .code(course.getCode())
            .name(course.getName())
            .status(isAdmin ? course.getStatus().name() : null)
            .academicTerm(StudentWithCourseResponseDTO.AcademicTermInfo.builder()
                .id(academicTerm.getId())
                .code(academicTerm.getCode())
                .name(academicTerm.getLabel())
                .year(academicTerm.getAcademicYear())
                .build())
            .build())
        .build();
  }

  /**
   * Gets all tasks assigned to the currently authenticated student.
   * 
   * @return List of StudentTaskWithDetailsResponseDTO with task and student task
   *         information
   */
  public List<StudentTaskWithDetailsResponseDTO> getCurrentStudentTasks() {
    // Get authenticated user's email from SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Find student by email
    Student student = studentRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante", email));

    // Find all student tasks
    List<StudentTaskWithDetailsResponseDTO> results = new ArrayList<>();
    List<StudentTaskJpaEntity> taskEntities = studentAssignmentRepository.findByStudentId(student.getSerialNumber())
        .stream()
        .filter(entity -> entity instanceof StudentTaskJpaEntity)
        .map(entity -> (StudentTaskJpaEntity) entity)
        .toList();

    for (StudentTaskJpaEntity entity : taskEntities) {
      StudentTask studentTask = studentTaskMapper.toDomain(entity);
      Task task = studentTask.getAssignment();
      Course course = courseRepository.findById(task.getCourseId())
          .orElseThrow(() -> new ResourceNotFoundException("Curso", task.getCourseId()));

      AcademicTerm academicTerm = academicTermRepository.findById(course.getAcademicTermId())
          .orElseThrow(() -> new ResourceNotFoundException("Período académico", course.getAcademicTermId()));

      // Check if user is admin to include status fields
      boolean isAdmin = com.udla.markenx.shared.domain.util.SecurityUtils.isAdmin();

      StudentTaskWithDetailsResponseDTO dto = StudentTaskWithDetailsResponseDTO.builder()
          .studentTaskId(studentTask.getId())
          .studentTaskCode(studentTask.getCode())
          .studentTaskStatus(isAdmin ? studentTask.getStatus().name() : null)
          .attemptCount(studentTask.getAttempts().size())
          .task(StudentTaskWithDetailsResponseDTO.TaskInfo.builder()
              .id(task.getId())
              .code(task.getCode())
              .name(task.getTitle())
              .description(task.getSummary())
              .maxScore(100.0)
              .minScoreToPass(task.getMinScoreToPass())
              .maxAttempts(task.getMaxAttempts())
              .status(isAdmin ? task.getStatus().name() : null)
              .startDate(null) // Task doesn't have start date in domain model
              .endDate(task.getDueDate().atStartOfDay())
              .courseCode(course.getCode())
              .courseName(course.getName())
              .academicTermYear(academicTerm.getAcademicYear())
              .build())
          .build();

      results.add(dto);
    }

    return results;
  }

  /**
   * Gets all attempts for a specific task of the currently authenticated student.
   * 
   * @param taskId Task UUID
   * @return List of AttemptResponseDTO
   */
  public List<AttemptResponseDTO> getCurrentStudentTaskAttempts(UUID taskId) {
    // Get authenticated user's email from SecurityContext
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    // Find student by email
    Student student = studentRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Estudiante", email));

    // Find all student tasks for this student
    List<StudentTaskJpaEntity> studentTaskEntities = studentAssignmentRepository
        .findByStudentId(student.getSerialNumber())
        .stream()
        .filter(entity -> entity instanceof StudentTaskJpaEntity)
        .map(entity -> (StudentTaskJpaEntity) entity)
        .toList();

    // Find the specific student task for this task ID
    StudentTaskJpaEntity studentTaskEntity = studentTaskEntities.stream()
        .filter(entity -> {
          StudentTask st = studentTaskMapper.toDomain(entity);
          return st.getAssignment().getId().equals(taskId);
        })
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Tarea asignada", taskId));

    // Get all attempts for this student task
    return attemptRepository.findByStudentTaskIdOrderByCreatedAtAsc(studentTaskEntity.getId())
        .stream()
        .map(attemptEntity -> {
          Attempt attempt = attemptMapper.toDomain(attemptEntity);
          return new AttemptResponseDTO(
              attempt.getId(),
              attempt.getCode(),
              attempt.getScore(),
              attemptEntity.getCreatedAt().toLocalDate(), // Use JPA entity's createdAt field
              attempt.getTimeSpent(),
              attempt.getAttemptStatus(),
              attempt.getResult());
        })
        .toList();
  }

  /**
   * Validates that email belongs to @udla.edu.ec domain.
   */
  private void validateUdlaEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new InvalidEmailException("El correo electrónico no puede estar vacío");
    }

    if (!EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches()) {
      throw new InvalidEmailException(
          String.format("El correo '%s' debe pertenecer al dominio @udla.edu.ec", email));
    }
  }

  /**
   * Gets the authenticated username from security context.
   * 
   * @return Username or "system" if not authenticated
   */
  private String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return "system";
  }

}
