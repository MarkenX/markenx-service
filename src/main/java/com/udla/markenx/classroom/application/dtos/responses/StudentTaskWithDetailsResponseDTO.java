package com.udla.markenx.classroom.application.dtos.responses;

import java.time.LocalDateTime;
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
@Schema(description = "Student task with complete details including task and student task information")
public class StudentTaskWithDetailsResponseDTO {

  @Schema(description = "Student task unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID studentTaskId;

  @Schema(description = "Student task code", example = "STT-2025-0001")
  private String studentTaskCode;

  @Schema(description = "Student task status", example = "ENABLED")
  private String studentTaskStatus;

  @Schema(description = "Number of attempts made by the student", example = "3")
  private Integer attemptCount;

  @Schema(description = "Task information")
  private TaskInfo task;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Schema(description = "Task information")
  public static class TaskInfo {

    @Schema(description = "Task unique identifier", example = "6d24eb18-b2ae-473d-a9a3-9cf6b8af359d")
    private UUID id;

    @Schema(description = "Task code", example = "TSK-2025-0001")
    private String code;

    @Schema(description = "Task name", example = "First Assignment")
    private String name;

    @Schema(description = "Task description", example = "Complete the exercises from chapter 1")
    private String description;

    @Schema(description = "Maximum score for the task", example = "100.0")
    private Double maxScore;

    @Schema(description = "Minimum score to pass the task", example = "60.0")
    private Double minScoreToPass;

    @Schema(description = "Maximum number of attempts allowed", example = "3")
    private Integer maxAttempts;

    @Schema(description = "Task status", example = "ENABLED")
    private String status;

    @Schema(description = "Task start date", example = "2025-01-15T08:00:00")
    private LocalDateTime startDate;

    @Schema(description = "Task end date", example = "2025-01-22T23:59:59")
    private LocalDateTime endDate;

    @Schema(description = "Course code", example = "CRS-2025-0001")
    private String courseCode;

    @Schema(description = "Course name", example = "Mathematics 101")
    private String courseName;

    @Schema(description = "Academic term year", example = "2025")
    private Integer academicTermYear;
  }
}
