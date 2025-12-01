package com.udla.markenx.classroom.application.dtos.requests;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateStudentRequestDTO(
        @NotBlank(message = "El nombre no puede estar vacío") @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres") String firstName,

        @NotBlank(message = "El apellido no puede estar vacío") @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres") String lastName,

        @NotBlank(message = "El email no puede estar vacío") @Email(message = "El email debe ser válido") @Pattern(regexp = ".*@udla\\.edu\\.ec$", message = "El correo debe pertenecer al dominio @udla.edu.ec") String email,

        @NotBlank(message = "La contraseña no puede estar vacía") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password,

        @NotNull(message = "El ID del curso es obligatorio") UUID courseId) {
}
