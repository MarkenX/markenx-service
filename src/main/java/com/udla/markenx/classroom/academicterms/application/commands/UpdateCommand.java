package com.udla.markenx.classroom.academicterms.application.commands;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateCommand(
		UUID id,
		LocalDate endOfTerm,
		Integer academicYear,
		String updatedBy) {
}
