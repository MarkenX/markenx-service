package com.udla.markenx.classroom.application.commands.academicterm;

import java.time.LocalDate;

public record CreateAcademicTermCommand(
		LocalDate startOfTerm,
		LocalDate endOfTerm,
		Integer academicYear,
		String createdBy) {
}
