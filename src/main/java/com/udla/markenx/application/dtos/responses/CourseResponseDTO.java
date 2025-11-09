package com.udla.markenx.application.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
  private Long id;
  private int assignmentsCount;
  private int studentsCount;
  private Long academicPeriodId;
  private String label;

  private String createdBy;
  private LocalDateTime createdAt;
  private String lastModifiedBy;
  private LocalDateTime lastModifiedAt;
}
