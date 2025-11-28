package com.udla.markenx.classroom.application.commands;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateAcademicTermCommand(
    UUID id,
    LocalDate startOfTerm,
    LocalDate endOfTerm,
    Integer academicYear,
    String updatedBy) {
}
