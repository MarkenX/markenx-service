package com.udla.markenx.classroom.application.dtos.responses;

import java.util.UUID;

public record StudentResponseDTO(
		UUID id,
		String code,
		String firstName,
		String lastName,
		String email) {
}
