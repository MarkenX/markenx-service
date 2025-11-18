package com.udla.markenx.classroom.application.dtos.responses;

import java.util.UUID;

public record CourseResponseDTO(
        UUID id,
        String code,
        String name) {
}
