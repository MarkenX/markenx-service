package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.CourseDtoMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateCourseRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.CourseResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.CourseManagementControllerPort;
import com.udla.markenx.classroom.application.services.CourseManagementService;
import com.udla.markenx.classroom.domain.models.Course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/markenx/courses")
@Validated
@Tag(name = "Courses", description = "Course management operations including CRUD operations")
@SecurityRequirement(name = "bearerAuth")
public class CourseManagementController implements CourseManagementControllerPort {

  private final CourseManagementService courseService;

  public CourseManagementController(CourseManagementService courseService) {
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
      @Parameter(description = "Course ID", required = true) @PathVariable Long id,
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
      @Parameter(description = "Course ID", required = true) @PathVariable Long id) {
    Course course = courseService.getCourseById(id);
    CourseResponseDTO response = CourseDtoMapper.toResponseDto(course);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all courses", description = "Retrieves a paginated list of all courses.", parameters = {
      @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
      @Parameter(name = "size", description = "Number of items per page", example = "10"),
      @Parameter(name = "sort", description = "Sorting criteria (e.g., 'name,asc' or 'createdAt,desc')", example = "name,asc")
  })
  @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
  public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(Pageable pageable) {
    Page<Course> courses = courseService.getAllCourses(pageable);
    Page<CourseResponseDTO> response = courses.map(CourseDtoMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete a course", description = "Soft deletes a course by disabling it. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Course deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Course not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> deleteCourse(
      @Parameter(description = "Course ID", required = true) @PathVariable Long id) {
    courseService.deleteCourse(id);
    return ResponseEntity.noContent().build();
  }
}
