package com.udla.markenx.classroom.application.dtos.responses;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.udla.markenx.shared.domain.valueobjects.DomainBaseModelStatus;

public record CourseResponseDTO(
                UUID id,
                String code,
                String name,
                @JsonInclude(JsonInclude.Include.NON_NULL) DomainBaseModelStatus status) {
}
