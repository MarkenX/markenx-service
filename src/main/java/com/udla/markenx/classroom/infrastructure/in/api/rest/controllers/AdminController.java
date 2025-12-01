package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.AdminControllerPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/markenx/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Administrative operations and debugging tools. Requires ADMIN role.")
@SecurityRequirement(name = "bearerAuth")
public class AdminController implements AdminControllerPort {

  public AdminController() {
  }

  @Override
  @GetMapping("/debug/auth")
  @Operation(summary = "Debug authentication", description = "Returns authentication details for debugging purposes. Shows principal, authorities, and other JWT token information.")
  @ApiResponse(responseCode = "200", description = "Authentication details retrieved successfully")
  public ResponseEntity<String> debugAuth(Authentication authentication) {
    StringBuilder debug = new StringBuilder();
    debug.append("Authenticated: ").append(authentication.isAuthenticated()).append("\n");
    debug.append("Principal: ").append(authentication.getName()).append("\n");
    debug.append("Authorities: ").append(authentication.getAuthorities()).append("\n");
    debug.append("Details: ").append(authentication.getDetails()).append("\n");
    return ResponseEntity.ok(debug.toString());
  }
}
