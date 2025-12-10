package com.udla.markenx.classroom.terms.application.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record UpdateRequestDTO(

    @NotNull(message = "La fecha de fin es obligatoria") LocalDate endDate,

    @NotNull(message = "El año es obligatorio") Integer academicYear) {
}
