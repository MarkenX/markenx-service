package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.mappers.CourseDtoMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.CourseManagementControllerPort;
import com.udla.markenx.classroom.application.services.CourseManagementService;
import com.udla.markenx.classroom.domain.models.Course;

@RestController
@RequestMapping("/api/markenx/courses")
@Validated
@Tag(name = "Courses", description = "Course management operations including CRUD operations")
@SecurityRequirement(name = "bearerAuth")
public class CourseController implements CourseManagementControllerPort {

  private final CourseManagementService courseService;

  public CourseController(CourseManagementService courseService) {
    this.courseService = courseService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new course", description = "Creates a new course. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Course created successfully", content = @Content(schema = @Schema(implementation = CourseResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<CourseResponseDTO> createCourse(
      @Parameter(description = "Course creation data", required = true) @Valid @RequestBody CreateCourseRequestDTO request) {
    Course course = courseService.createCourse(request);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update a course", description = "Updates an existing course. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Course updated successfully"),
      @ApiResponse(responseCode = "404", description = "Course not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<CourseResponseDTO> updateCourse(
      @Parameter(description = "Course ID", required = true) @PathVariable UUID id,
      @Parameter(description = "Updated course data", required = true) @Valid @RequestBody UpdateCourseRequestDTO request) {
    Course course = courseService.updateCourse(id, request);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get course by ID", description = "Retrieves a specific course by its unique identifier.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Course found"),
      @ApiResponse(responseCode = "404", description = "Course not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<CourseResponseDTO> getCourseById(
      @Parameter(description = "Course ID", required = true) @PathVariable UUID id) {
    Course course = courseService.getCourseById(id);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all courses", description = "Retrieves a paginated list of all courses. Supports optional status filter (ENABLED/DISABLED). "
      +
      "Valid sort properties: id, status, createdAt, updatedAt, name, credits, academicTermId. " +
      "Example: ?page=0&size=10&sort=name,asc&status=DISABLED")
  @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
  public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(
      @Parameter(description = "Filter by status (ENABLED or DISABLED)", required = false) @RequestParam(required = false) com.udla.markenx.shared.domain.valueobjects.EntityStatus status,
      @Parameter(hidden = true) Pageable pageable) {
    Page<Course> courses;
    if (status != null) {
      courses = courseService.getCoursesByStatus(status, pageable);
    } else {
      courses = courseService.getAllCourses(pageable);
    }
    Page<CourseResponseDTO> response = courses.map(CourseDtoMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @PutMapping("/{id}/disable")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Disable a course", description = "Disables a course by setting status to DISABLED. Requires ADMIN role. Course can only be disabled if it has no enabled tasks.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Course disabled successfully"),
      @ApiResponse(responseCode = "400", description = "Cannot disable course with enabled tasks"),
      @ApiResponse(responseCode = "404", description = "Course not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> disableCourse(
      @Parameter(description = "Course ID", required = true) @PathVariable UUID id) {
    courseService.disableCourse(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PutMapping("/{id}/enable")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Enable a course", description = "Enables a previously disabled course by setting status to ENABLED. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Course enabled successfully"),
      @ApiResponse(responseCode = "404", description = "Course not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> enableCourse(
      @Parameter(description = "Course ID", required = true) @PathVariable UUID id) {
    courseService.enableCourse(id);
    return ResponseEntity.noContent().build();
  }
}
