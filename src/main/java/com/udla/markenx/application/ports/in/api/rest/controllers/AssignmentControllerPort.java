package com.udla.markenx.application.ports.in.api.rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.udla.markenx.application.dtos.responses.AssignmentStatusResponseDTO;

/**
 * Port for assignment operations.
 * 
 * Provides endpoints for retrieving assignment-related information
 * such as available assignment statuses.
 */
public interface AssignmentControllerPort {

  /**
   * Retrieves all available assignment status options.
   * 
   * Returns the complete list of assignment statuses with their
   * display names for use in UI components.
   * 
   * @return ResponseEntity with list of assignment status DTOs
   */
  ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus();
}
