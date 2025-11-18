package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.UUID;

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

import com.udla.markenx.classroom.application.dtos.mappers.AcademicPeriodMapper;
import com.udla.markenx.classroom.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateAcademicTermRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.AcademicTermControllerPort;
import com.udla.markenx.classroom.application.services.AcademicTermManagementService;
import com.udla.markenx.classroom.domain.models.AcademicTerm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/markenx/academic-terms")
@Validated
@Tag(name = "Academic Terms", description = "Academic term/period management operations")
@SecurityRequirement(name = "bearerAuth")
public class AcademicTermController implements AcademicTermControllerPort {

  private final AcademicTermManagementService academicTermService;

  public AcademicTermController(AcademicTermManagementService academicTermService) {
    this.academicTermService = academicTermService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new academic term", description = "Creates a new academic period/term. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Academic term created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<AcademicPeriodResponseDTO> createAcademicTerm(
      @Parameter(description = "Academic term creation data", required = true) @Valid @RequestBody CreateAcademicPeriodRequestDTO request) {
    AcademicTerm academicTerm = academicTermService.createAcademicTerm(request);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update an academic term", description = "Updates an existing academic period/term. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Academic term updated successfully"),
      @ApiResponse(responseCode = "404", description = "Academic term not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<AcademicPeriodResponseDTO> updateAcademicTerm(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id,
      @Parameter(description = "Updated academic term data", required = true) @Valid @RequestBody UpdateAcademicTermRequestDTO request) {
    AcademicTerm academicTerm = academicTermService.updateAcademicTerm(id, request);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get academic term by ID", description = "Retrieves a specific academic term by its unique identifier.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Academic term found"),
      @ApiResponse(responseCode = "404", description = "Academic term not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized")
  })
  public ResponseEntity<AcademicPeriodResponseDTO> getAcademicTermById(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
    AcademicTerm academicTerm = academicTermService.getAcademicTermById(id);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all academic terms", description = "Retrieves a paginated list of all academic terms/periods.")
  @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully")
  public ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicTerms(
      @Parameter(description = "Pagination parameters") Pageable pageable) {
    Page<AcademicTerm> academicTerms = academicTermService.getAllAcademicTerms(pageable);
    Page<AcademicPeriodResponseDTO> response = academicTerms.map(AcademicPeriodMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete an academic term", description = "Soft deletes an academic term by disabling it. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Academic term deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Academic term not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> deleteAcademicTerm(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
    academicTermService.deleteAcademicTerm(id);
    return ResponseEntity.noContent().build();
  }
}
