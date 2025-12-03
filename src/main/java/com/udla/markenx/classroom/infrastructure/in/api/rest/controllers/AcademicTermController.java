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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.udla.markenx.classroom.academicterms.application.dtos.ResponseDTO;
import com.udla.markenx.classroom.academicterms.application.commands.CreateCommand;
import com.udla.markenx.classroom.academicterms.application.commands.UpdateCommand;
import com.udla.markenx.classroom.academicterms.application.dtos.CreateRequestDTO;
import com.udla.markenx.classroom.academicterms.application.dtos.UpdateRequestDTO;
import com.udla.markenx.classroom.academicterms.application.mappers.AcademicPeriodMapper;
import com.udla.markenx.classroom.academicterms.application.ports.in.api.rest.controllers.AcademicTermControllerPort;
import com.udla.markenx.classroom.academicterms.application.services.AcademicTermService;
import com.udla.markenx.classroom.academicterms.domain.model.AcademicTerm;

@RestController
@RequestMapping("/api/markenx/academic-terms")
@Validated
@Tag(name = "Academic Terms", description = "Academic term/period management operations")
@SecurityRequirement(name = "bearerAuth")
public class AcademicTermController implements AcademicTermControllerPort {

  private final AcademicTermService academicTermService;

  public AcademicTermController(AcademicTermService academicTermService) {
    this.academicTermService = academicTermService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new academic term", description = "Creates a new academic term. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Academic term created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<ResponseDTO> createAcademicTerm(
      @Parameter(description = "Academic term creation data", required = true) @Valid @RequestBody CreateRequestDTO request) {

    var command = new CreateCommand(
        request.getStartOfTerm(),
        request.getEndOfTerm(),
        request.getAcademicYear(),
        "ADMIN");

    AcademicTerm academicTerm = academicTermService.createAcademicTerm(command);
    ResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
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
  public ResponseEntity<ResponseDTO> updateAcademicTerm(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id,
      @Parameter(description = "Updated academic term data", required = true) @Valid @RequestBody UpdateRequestDTO request) {

    var command = new UpdateCommand(
        id,
        request.getStartDate(),
        request.getEndDate(),
        request.getYear(),
        "ADMIN");

    AcademicTerm academicTerm = academicTermService.updateAcademicTerm(command);
    ResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
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
  public ResponseEntity<ResponseDTO> getAcademicTermById(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
    AcademicTerm academicTerm = academicTermService.getAcademicTermById(id);
    ResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  @Operation(summary = "Get all academic terms", description = "Retrieves a paginated list of academic terms. Supports optional status filter (ENABLED/DISABLED). Example: ?page=0&size=10&status=DISABLED")
  @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully")
  public ResponseEntity<Page<ResponseDTO>> getAllAcademicTerms(
      @Parameter(description = "Filter by status (ENABLED or DISABLED)", required = false) @RequestParam(required = false) com.udla.markenx.shared.domain.valueobjects.EntityStatus status,
      @Parameter(hidden = true) Pageable pageable) {
    Page<AcademicTerm> academicTerms;
    if (status != null) {
      academicTerms = academicTermService.getAcademicPeriodsByStatus(status, pageable);
    } else {
      academicTerms = academicTermService.getAllAcademicTerms(pageable);
    }
    Page<ResponseDTO> response = academicTerms.map(AcademicPeriodMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @PutMapping("/{id}/disable")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Disable an academic term", description = "Disables an academic term by setting status to DISABLED. Requires ADMIN role. Term can only be disabled if it has no enabled courses.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Academic term disabled successfully"),
      @ApiResponse(responseCode = "400", description = "Cannot disable term with enabled courses"),
      @ApiResponse(responseCode = "404", description = "Academic term not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> disableAcademicTerm(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
    academicTermService.disableAcademicTerm(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PutMapping("/{id}/enable")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Enable an academic term", description = "Enables a previously disabled academic term by setting status to ENABLED. Requires ADMIN role.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Academic term enabled successfully"),
      @ApiResponse(responseCode = "404", description = "Academic term not found"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "403", description = "Forbidden")
  })
  public ResponseEntity<Void> enableAcademicTerm(
      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
    academicTermService.enableAcademicTerm(id);
    return ResponseEntity.noContent().build();
  }
}
