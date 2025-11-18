package com.udla.markenx.classroom.application.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequestDTO {

  @NotBlank(message = "El nombre del curso es obligatorio")
  private String name;
}
