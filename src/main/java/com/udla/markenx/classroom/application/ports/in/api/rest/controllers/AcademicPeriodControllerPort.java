package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.udla.markenx.classroom.application.dtos.requests.CreateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateAcademicPeriodRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.AcademicPeriodResponseDTO;
import com.udla.markenx.classroom.domain.models.Course;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Port for Academic Period management operations.
 * 
 * Provides CRUD operations and validation for academic periods (semesters).
 */
public interface AcademicPeriodControllerPort {

  /**
   * Creates a new academic period.
   * 
   * The semester number is automatically determined:
   * - The period that starts first in the year gets semester 1
   * - The period that starts second gets semester 2
   * 
   * @param request DTO with startDate, endDate, and year
   * @return ResponseEntity with created academic period
   */
  ResponseEntity<AcademicPeriodResponseDTO> createAcademicPeriod(
      @Valid @RequestBody CreateAcademicPeriodRequestDTO request);

  /**
   * Updates an existing academic period (dates and year).
   * Semester number is immutable (determined by which period starts first).
   * 
   * @param id      the period ID
   * @param request DTO with updated dates and/or year
   * @return ResponseEntity with updated academic period
   */
  ResponseEntity<AcademicPeriodResponseDTO> updateAcademicPeriod(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id,
      @Valid @RequestBody UpdateAcademicPeriodRequestDTO request);

  /**
   * Retrieves an academic period by ID.
   * 
   * @param id the period ID
   * @return ResponseEntity with academic period details
   */
  ResponseEntity<AcademicPeriodResponseDTO> getAcademicPeriodById(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id);

  /**
   * Retrieves all academic periods with pagination.
   * 
   * @param page page number (default 0)
   * @param size page size (default 10)
   * @return ResponseEntity with paginated academic periods
   */
  ResponseEntity<Page<AcademicPeriodResponseDTO>> getAllAcademicPeriods(
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) int size);

  /**
   * Deletes an academic period by ID.
   * Fails if the period has associated courses.
   * 
   * @param id the period ID
   * @return ResponseEntity with no content
   */
  ResponseEntity<Void> deleteAcademicPeriod(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id);

  /**
   * Retrieves all courses for a specific academic period.
   * 
   * @param id the period ID
   * @return ResponseEntity with list of courses
   */
  ResponseEntity<List<Course>> getCoursesByPeriodId(
      @PathVariable @Positive(message = "El ID del período debe ser positivo") Long id);
}
