package com.udla.markenx.classroom.application.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for updating student information.
 * 
 * @param firstName the student's first name
 * @param lastName  the student's last name
 * @param email     the student's email address
 */
public record UpdateStudentRequestDTO(
    @NotBlank(message = "First name is required") String firstName,

    @NotBlank(message = "Last name is required") String lastName,

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email) {
}
