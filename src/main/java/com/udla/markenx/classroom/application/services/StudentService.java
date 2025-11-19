package com.udla.markenx.classroom.application.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.udla.markenx.classroom.application.dtos.requests.BulkStudentImportDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.application.ports.out.persistance.repositories.StudentRepositoryPort;
import com.udla.markenx.classroom.domain.exceptions.BulkImportException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;
import com.udla.markenx.classroom.domain.models.Student;
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
  private final AuthenticationServicePort authenticationService;

  public StudentService(
      StudentRepositoryPort studentRepository,
      AuthenticationServicePort authenticationService) {
    this.studentRepository = studentRepository;
    this.authenticationService = authenticationService;
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

    // Create user in Keycloak with STUDENT role
    authenticationService.createUser(
        email,
        password,
        firstName,
        lastName,
        STUDENT_ROLE,
        true // Require password change on first login
    );

    // Create student domain object with enrolled course
    Student student = new Student(
        courseId,
        firstName,
        lastName,
        email);

    // Save to database
    return studentRepository.save(student);
  }

  /**
   * Deletes (disables) a student.
   * 
   * This is a soft delete:
   * - Sets status to DISABLED in database
   * - Disables user in Keycloak (preserves authentication history)
   * - Student record remains but is not visible to non-admins
   * 
   * @param studentId Student UUID
   */
  @Transactional
  public void deleteStudent(UUID studentId) {
    // Find student to get email
    Student student = studentRepository.findByIdIncludingDisabled(studentId)
        .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + studentId));

    // Disable in database
    studentRepository.deleteById(studentId);

    // Disable in Keycloak
    authenticationService.disableUser(student.getAcademicEmail());
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
    int successCount = 0;
    for (BulkStudentImportDTO studentDto : students) {
      try {
        // Create user in Keycloak with STUDENT role
        authenticationService.createUser(
            studentDto.getEmail(),
            DEFAULT_PASSWORD,
            studentDto.getFirstName(),
            studentDto.getLastName(),
            STUDENT_ROLE,
            true // Require password change on first login
        );

        // Create student domain object with enrolled course (from parameter)
        Student student = new Student(
            courseId,
            studentDto.getFirstName(),
            studentDto.getLastName(),
            studentDto.getEmail());

        // Save to database
        studentRepository.save(student);
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

}
