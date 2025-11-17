package com.udla.markenx.classroom.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for CSV bulk student import.
 * 
 * Each row in the CSV represents a student with:
 * - firstName: Student's first name
 * - lastName: Student's last name
 * - email: Must be @udla.edu.ec domain
 * - enrollmentCode: Student's enrollment code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkStudentImportDTO {

  @NotBlank(message = "El nombre es obligatorio")
  private String firstName;

  @NotBlank(message = "El apellido es obligatorio")
  private String lastName;

  @NotBlank(message = "El correo electrónico es obligatorio")
  @Email(message = "El formato del correo electrónico no es válido")
  @Pattern(regexp = ".*@udla\\.edu\\.ec$", message = "El correo debe pertenecer al dominio @udla.edu.ec")
  private String email;

  @NotBlank(message = "El código de matrícula es obligatorio")
  private String enrollmentCode;
}
