package com.udla.markenx.classroom.application.dtos.responses;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Student profile with course information")
public class StudentWithCourseResponseDTO {

  @Schema(description = "Student unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Student code", example = "STU-2025-0001")
  private String code;

  @Schema(description = "Student first name", example = "John")
  private String firstName;

  @Schema(description = "Student last name", example = "Doe")
  private String lastName;

  @Schema(description = "Student email", example = "john.doe@udla.edu.ec")
  private String email;

  @Schema(description = "Student status (ADMIN only)", example = "ENABLED")
  @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
  private String status;

  @Schema(description = "Course information")
  private CourseInfo course;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Course basic information")
  public static class CourseInfo {

    @Schema(description = "Course unique identifier", example = "6d24eb18-b2ae-473d-a9a3-9cf6b8af359d")
    private UUID id;

    @Schema(description = "Course code", example = "CRS-2025-0001")
    private String code;

    @Schema(description = "Course name", example = "Mathematics 101")
    private String name;

    @Schema(description = "Course status (ADMIN only)", example = "ENABLED")
    @com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
    private String status;

    @Schema(description = "Academic term information")
    private AcademicTermInfo academicTerm;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Academic term basic information")
  public static class AcademicTermInfo {

    @Schema(description = "Academic term unique identifier", example = "7e35fc29-c0cf-594e-b911-cc8036a07a36")
    private UUID id;

    @Schema(description = "Academic term code", example = "TERM-2025-0001")
    private String code;

    @Schema(description = "Academic term name", example = "2025-1")
    private String name;

    @Schema(description = "Academic year", example = "2025")
    private Integer year;
  }
}
