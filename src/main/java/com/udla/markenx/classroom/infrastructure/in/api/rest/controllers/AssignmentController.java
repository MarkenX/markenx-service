package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.AssignmentStatusMapper;
import com.udla.markenx.classroom.application.dtos.responses.AssignmentStatusResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.AssignmentControllerPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/markenx")
@Tag(name = "Assignments", description = "Assignment status catalog and operations")
public class AssignmentController implements AssignmentControllerPort {

	@Override
	@GetMapping("/assignments/status")
	@Operation(summary = "Get assignment status catalog", description = "Retrieves all possible assignment status values. This endpoint does not require authentication.")
	@ApiResponse(responseCode = "200", description = "Status list retrieved successfully", content = @Content(schema = @Schema(implementation = AssignmentStatusResponseDTO.class)))
	public ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus() {
		List<AssignmentStatusResponseDTO> statusList = AssignmentStatusMapper.toDtoList();
		return ResponseEntity.ok(statusList);
	}
}
