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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/markenx/academic-terms")
@Validated
public class AcademicTermController implements AcademicTermControllerPort {

  private final AcademicTermManagementService academicTermService;

  public AcademicTermController(AcademicTermManagementService academicTermService) {
    this.academicTermService = academicTermService;
  }

  @Override
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AcademicPeriodResponseDTO> createAcademicTerm(
      @Valid @RequestBody CreateAcademicPeriodRequestDTO request) {
    AcademicTerm academicTerm = academicTermService.createAcademicTerm(request);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<AcademicPeriodResponseDTO> updateAcademicTerm(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateAcademicTermRequestDTO request) {
    AcademicTerm academicTerm = academicTermService.updateAcademicTerm(id, request);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<AcademicPeriodResponseDTO> getAcademicTermById(@PathVariable UUID id) {
    AcademicTerm academicTerm = academicTermService.getAcademicTermById(id);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(academicTerm);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
  public ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicTerms(Pageable pageable) {
    Page<AcademicTerm> academicTerms = academicTermService.getAllAcademicTerms(pageable);
    Page<AcademicPeriodResponseDTO> response = academicTerms.map(AcademicPeriodMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteAcademicTerm(@PathVariable UUID id) {
    academicTermService.deleteAcademicTerm(id);
    return ResponseEntity.noContent().build();
  }
}
