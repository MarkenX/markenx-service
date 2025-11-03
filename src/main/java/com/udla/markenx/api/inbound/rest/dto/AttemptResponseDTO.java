package com.udla.markenx.api.inbound.rest.dto;

import java.time.Duration;
import java.time.LocalDate;

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
public class AttemptResponseDTO {
  @NotNull
  private Long id;
  @Positive
  private double score;
  @NotNull
  private LocalDate date;
  @NotNull
  private Duration duration;
}
