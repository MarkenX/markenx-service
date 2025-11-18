package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

/**
 * Port for administrative operations.
 * 
 * Handles admin-only debugging and system management functionality.
 * All endpoints require ADMIN role.
 */
public interface AdminControllerPort {

  /**
   * DEBUG: Test authentication and roles.
   * Temporary endpoint to verify token is working.
   * 
   * @param authentication the authentication object from security context
   * @return ResponseEntity with authentication debug information
   */
  ResponseEntity<String> debugAuth(Authentication authentication);
}
