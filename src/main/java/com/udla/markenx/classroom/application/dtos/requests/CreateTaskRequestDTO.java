package com.udla.markenx.classroom.application.dtos.requests;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequestDTO {

  @NotNull(message = "El ID del curso es obligatorio")
  private UUID courseId;

  @NotBlank(message = "El título es obligatorio")
  private String title;

  @NotBlank(message = "El resumen es obligatorio")
  private String summary;

  @NotNull(message = "La fecha de vencimiento es obligatoria")
  private LocalDate dueDate;

  @NotNull(message = "El máximo de intentos es obligatorio")
  @Positive(message = "El máximo de intentos debe ser positivo")
  private Integer maxAttempts;

  @NotNull(message = "El puntaje mínimo para aprobar es obligatorio")
  private Double minScoreToPass;
}
