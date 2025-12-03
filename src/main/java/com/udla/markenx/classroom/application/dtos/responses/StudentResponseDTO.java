package com.udla.markenx.classroom.application.dtos.responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.udla.markenx.shared.domain.valueobjects.EntityStatus;

public record StudentResponseDTO(
		UUID id,
		String code,
		String firstName,
		String lastName,
		String email,
		@JsonInclude(JsonInclude.Include.NON_NULL) EntityStatus status) {
}
