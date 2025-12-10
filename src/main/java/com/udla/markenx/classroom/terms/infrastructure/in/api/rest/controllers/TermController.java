package com.udla.markenx.classroom.terms.infrastructure.in.api.rest.controllers;

import com.udla.markenx.classroom.terms.application.commands.CreateCommand;
import com.udla.markenx.classroom.terms.application.dtos.CreateRequestDTO;
import com.udla.markenx.classroom.terms.application.dtos.ResponseDTO;
import com.udla.markenx.classroom.terms.application.mappers.TermMapper;
import com.udla.markenx.classroom.terms.application.ports.in.api.rest.controllers.AcademicTermControllerPort;
import com.udla.markenx.classroom.terms.application.services.ManageTermsService;
import com.udla.markenx.classroom.terms.domain.model.Term;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/markenx/academic-terms")
@Validated
@Tag(name = "Academic Terms", description = "Academic term/period management operations")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TermController implements AcademicTermControllerPort {

    private final ManageTermsService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new academic term", description = "Creates a new academic term. Requires ADMIN role.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Academic term created successfully"), @ApiResponse(responseCode = "400", description = "Invalid input"), @ApiResponse(responseCode = "401", description = "Unauthorized"), @ApiResponse(responseCode = "403", description = "Forbidden")})
    public ResponseEntity<ResponseDTO> createTerm(@Parameter(description = "Academic term creation data", required = true) @Valid @RequestBody CreateRequestDTO request) {

        var command = new CreateCommand(request.startDate(), request.endDate(), request.year(), "ADMIN", false);

        Term term = service.createTerm(command);
        ResponseDTO response = TermMapper.toResponseDto(term);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


//  @Override
//  @PutMapping("/{id}")
//  @PreAuthorize("hasRole('ADMIN')")
//  @Operation(summary = "Update an academic term", description = "Updates an existing academic period/term. Requires ADMIN role.")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "200", description = "Academic term updated successfully"),
//      @ApiResponse(responseCode = "404", description = "Academic term not found"),
//      @ApiResponse(responseCode = "401", description = "Unauthorized"),
//      @ApiResponse(responseCode = "403", description = "Forbidden")
//  })
//  public ResponseEntity<ResponseDTO> updateAcademicTerm(
//      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id,
//      @Parameter(description = "Updated academic term data", required = true) @Valid @RequestBody UpdateRequestDTO request) {
//
//    var command = new UpdateCommand(
//        id,
//        request.getStartDate(),
//        request.getEndDate(),
//        request.getYear(),
//        "ADMIN");
//
//    Term term = service.updateAcademicTerm(command);
//    ResponseDTO response = TermMapper.toResponseDto(term);
//    return ResponseEntity.ok(response);
//  }
//
//  @Override
//  @GetMapping("/{id}")
//  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//  @Operation(summary = "Get academic term by ID", description = "Retrieves a specific academic term by its unique identifier.")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "200", description = "Academic term found"),
//      @ApiResponse(responseCode = "404", description = "Academic term not found"),
//      @ApiResponse(responseCode = "401", description = "Unauthorized")
//  })
//  public ResponseEntity<ResponseDTO> getAcademicTermById(
//      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
//    Term term = service.getAcademicTermById(id);
//    ResponseDTO response = TermMapper.toResponseDto(term);
//    return ResponseEntity.ok(response);
//  }
//
//  @Override
//  @GetMapping
//  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
//  @Operation(summary = "Get all academic terms", description = "Retrieves a paginated list of academic terms. Supports optional status filter (ENABLED/DISABLED). Example: ?page=0&size=10&status=DISABLED")
//  @ApiResponse(responseCode = "200", description = "Academic terms retrieved successfully")
//  public ResponseEntity<Page<ResponseDTO>> getAllAcademicTerms(
//      @Parameter(description = "Filter by status (ENABLED or DISABLED)", required = false) @RequestParam(required = false) com.udla.markenx.shared.domain.valueobjects.EntityStatus status,
//      @Parameter(hidden = true) Pageable pageable) {
//    Page<Term> academicTerms;
//    if (status != null) {
//      academicTerms = service.getAcademicPeriodsByStatus(status, pageable);
//    } else {
//      academicTerms = service.getAllAcademicTerms(pageable);
//    }
//    Page<ResponseDTO> response = academicTerms.map(TermMapper::toResponseDto);
//    return ResponseEntity.ok(response);
//  }
//
//  @Override
//  @PutMapping("/{id}/disable")
//  @PreAuthorize("hasRole('ADMIN')")
//  @Operation(summary = "Disable an academic term", description = "Disables an academic term by setting status to DISABLED. Requires ADMIN role. Term can only be disabled if it has no enabled courses.")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "204", description = "Academic term disabled successfully"),
//      @ApiResponse(responseCode = "400", description = "Cannot disable term with enabled courses"),
//      @ApiResponse(responseCode = "404", description = "Academic term not found"),
//      @ApiResponse(responseCode = "401", description = "Unauthorized"),
//      @ApiResponse(responseCode = "403", description = "Forbidden")
//  })
//  public ResponseEntity<Void> disableAcademicTerm(
//      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
//    service.disableAcademicTerm(id);
//    return ResponseEntity.noContent().build();
//  }
//
//  @Override
//  @PutMapping("/{id}/enable")
//  @PreAuthorize("hasRole('ADMIN')")
//  @Operation(summary = "Enable an academic term", description = "Enables a previously disabled academic term by setting status to ENABLED. Requires ADMIN role.")
//  @ApiResponses(value = {
//      @ApiResponse(responseCode = "204", description = "Academic term enabled successfully"),
//      @ApiResponse(responseCode = "404", description = "Academic term not found"),
//      @ApiResponse(responseCode = "401", description = "Unauthorized"),
//      @ApiResponse(responseCode = "403", description = "Forbidden")
//  })
//  public ResponseEntity<Void> enableAcademicTerm(
//      @Parameter(description = "Academic term ID", required = true) @PathVariable UUID id) {
//    service.enableAcademicTerm(id);
//    return ResponseEntity.noContent().build();
//  }
}
