package com.udla.markenx.classroom.terms.application.ports.in.api.rest.controllers;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.terms.application.dtos.ResponseDTO;
import com.udla.markenx.classroom.terms.application.dtos.CreateRequestDTO;

/**
 * Port interface that defines operations for managing academic terms.
 * <p>
 * This controller port abstracts the interaction layer for creating,
 * updating, retrieving, enabling and disabling academic terms within
 * the academic period management domain.
 */
public interface AcademicTermControllerPort {

  /**
   * Creates a new academic term using the provided request data.
   *
   * @param request the data required to create the academic term;
   *                must be valid according to bean validation rules
   * @return a {@link ResponseEntity} containing the created academic term
   *         information
   */
  ResponseEntity<ResponseDTO> createTerm(@Valid CreateRequestDTO request);

//  /**
//   * Updates an existing academic term identified by its unique ID.
//   *
//   * @param id      the identifier of the academic term to update
//   * @param request the updated data for the academic term;
//   *                must be valid according to bean validation rules
//   * @return a {@link ResponseEntity} containing the updated academic term
//   *         information
//   */
//  ResponseEntity<ResponseDTO> updateAcademicTerm(UUID id, @Valid UpdateRequestDTO request);
//
//  /**
//   * Retrieves the academic term associated with the given ID.
//   *
//   * @param id the identifier of the academic term
//   * @return a {@link ResponseEntity} containing the academic term details
//   */
//  ResponseEntity<ResponseDTO> getAcademicTermById(UUID id);
//
//  /**
//   * Retrieves all academic terms filtered by status and paginated.
//   *
//   * @param status   the status used to filter academic terms (e.g., ACTIVE,
//   *                 DISABLED)
//   * @param pageable pagination and sorting information
//   * @return a {@link ResponseEntity} containing a paginated list of academic
//   *         terms
//   */
//  ResponseEntity<Page<ResponseDTO>> getAllAcademicTerms(EntityStatus status, Pageable pageable);
//
//  /**
//   * Disables an academic term, preventing it from being used or displayed
//   * in active contexts.
//   *
//   * @param id the identifier of the academic term to disable
//   * @return an empty {@link ResponseEntity} with appropriate status code
//   */
//  ResponseEntity<Void> disableAcademicTerm(UUID id);
//
//  /**
//   * Enables a previously disabled academic term.
//   *
//   * @param id the identifier of the academic term to enable
//   * @return an empty {@link ResponseEntity} with appropriate status code
//   */
//  ResponseEntity<Void> enableAcademicTerm(UUID id);
}
