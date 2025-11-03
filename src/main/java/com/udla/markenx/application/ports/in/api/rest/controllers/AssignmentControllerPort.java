package com.udla.markenx.application.ports.in.api.rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.udla.markenx.application.dtos.AssignmentStatusResponseDTO;

public interface AssignmentControllerPort {
  ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus();
}
