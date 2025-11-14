package com.udla.markenx.infrastructure.in.api.rest.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.dtos.mappers.AcademicPeriodMapper;
import com.udla.markenx.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.application.dtos.requests.UpdateAcademicPeriodRequestDTO;
import com.udla.markenx.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.application.ports.in.api.rest.controllers.AcademicPeriodControllerPort;
import com.udla.markenx.application.services.AcademicTermService;
import com.udla.markenx.core.models.AcademicTerm;
import com.udla.markenx.core.models.Course;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

/**
 * REST controller for Academic Period management.
 * 
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/markenx/academic-periods")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class AcademicPeriodController implements AcademicPeriodControllerPort {

  private final AcademicTermService academicPeriodService;

  public AcademicPeriodController(AcademicTermService academicPeriodService) {
    this.academicPeriodService = academicPeriodService;
  }

  @Override
  @PostMapping
  public ResponseEntity<AcademicPeriodResponseDTO> createAcademicPeriod(
      @Valid @RequestBody CreateAcademicPeriodRequestDTO request) {

    AcademicTerm created = academicPeriodService.createAcademicPeriod(
        request.getStartDate(),
        request.getEndDate(),
        request.getYear());

    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(created);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<AcademicPeriodResponseDTO> updateAcademicPeriod(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id,
      @Valid @RequestBody UpdateAcademicPeriodRequestDTO request) {

    AcademicTerm updated = academicPeriodService.updateAcademicPeriod(
        id,
        request.getStartDate(),
        request.getEndDate(),
        request.getYear());

    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(updated);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<AcademicPeriodResponseDTO> getAcademicPeriodById(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id) {

    AcademicTerm period = academicPeriodService.getAcademicPeriodById(id);
    AcademicPeriodResponseDTO response = AcademicPeriodMapper.toResponseDto(period);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping
  public ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicPeriods(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size) {

    Page<AcademicTerm> periods = academicPeriodService.getAllAcademicPeriods(
        PageRequest.of(page, size));

    Page<AcademicPeriodResponseDTO> response = periods.map(AcademicPeriodMapper::toResponseDto);
    return ResponseEntity.ok(response);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAcademicPeriod(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id) {

    academicPeriodService.deleteAcademicPeriod(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/{id}/courses")
  public ResponseEntity<List<Course>> getCoursesByPeriodId(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id) {

    List<Course> courses = academicPeriodService.getCoursesByPeriodId(id);
    return ResponseEntity.ok(courses);
  }
}
