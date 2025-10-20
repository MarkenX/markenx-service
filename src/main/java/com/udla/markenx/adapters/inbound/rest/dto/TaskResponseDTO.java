package com.udla.markenx.adapters.inbound.rest.dto;

import java.time.LocalDate;

import com.udla.markenx.domain.model.AssignmentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String summary;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private AssignmentStatus currentStatus;
    @Positive
    private int activeAttempt;
    @Positive
    private int maxAttempts;
}
