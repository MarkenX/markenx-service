package com.udla.markenx.classroom.application.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.udla.markenx.classroom.application.dtos.requests.BulkStudentImportDTO;
import com.udla.markenx.classroom.application.dtos.requests.CreateStudentRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.BulkImportResponseDTO;
import com.udla.markenx.classroom.domain.exceptions.BulkImportException;
import com.udla.markenx.classroom.domain.exceptions.InvalidEmailException;

/**
 * Service for student domain operations.
 * 
 * Handles student-related business logic including:
 * - Bulk importing students from CSV files
 */
@Service
public class StudentService {

  private static final String UDLA_EMAIL_PATTERN = ".*@udla\\.edu\\.ec$";
  private static final Pattern EMAIL_PATTERN = Pattern.compile(UDLA_EMAIL_PATTERN);

  private final StudentManagementService studentManagementService;

  public StudentService(StudentManagementService studentManagementService) {
    this.studentManagementService = studentManagementService;
  }

  /**
   * Imports students from CSV file.
   * 
   * CSV format (with header):
   * firstName,lastName,email,enrollmentCode
   * 
   * ALL students must be valid or NONE will be imported (transactional).
   * If ANY validation fails, the entire import is rolled back.
   * 
   * @param file CSV file with student data
   * @return BulkImportResponseDTO with success message and count
   * @throws BulkImportException      if any student is invalid (contains all
   *                                  validation errors)
   * @throws IllegalArgumentException if file is empty or malformed
   */
  @Transactional
  public BulkImportResponseDTO importStudentsFromCsv(MultipartFile file) {
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

      return processStudentImports(students);

    } catch (BulkImportException ex) {
      throw ex; // Re-throw to preserve validation errors
    } catch (Exception ex) {
      throw new IllegalArgumentException("Error al procesar el archivo CSV: " + ex.getMessage(), ex);
    }
  }

  /**
   * Validates ALL students first, then imports them.
   * If any validation fails, throws BulkImportException with all errors.
   */
  private BulkImportResponseDTO processStudentImports(List<BulkStudentImportDTO> students) {
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
        if (studentDto.getEnrollmentCode() == null || studentDto.getEnrollmentCode().trim().isEmpty()) {
          validationErrors.put(rowNumber, "El código de matrícula es obligatorio");
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

    // PHASE 2: Import ALL students (only if all are valid)
    for (BulkStudentImportDTO studentDto : students) {
      String defaultPassword = generateDefaultPassword(studentDto);

      CreateStudentRequestDTO createRequest = new CreateStudentRequestDTO(
          studentDto.getFirstName(),
          studentDto.getLastName(),
          studentDto.getEmail(),
          defaultPassword);

      studentManagementService.createStudent(createRequest);
    }

    // All students imported successfully
    return BulkImportResponseDTO.success(totalRecords);
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
   * Generates default password: enrollmentCode + first 3 chars of firstName.
   * 
   * Example: If enrollmentCode is "2025A" and firstName is "Juan",
   * password will be "2025AJua"
   */
  private String generateDefaultPassword(BulkStudentImportDTO studentDto) {
    String enrollmentCode = studentDto.getEnrollmentCode();
    String firstName = studentDto.getFirstName();

    if (firstName.length() >= 3) {
      return enrollmentCode + firstName.substring(0, 3);
    } else {
      return enrollmentCode + firstName;
    }
  }
}
