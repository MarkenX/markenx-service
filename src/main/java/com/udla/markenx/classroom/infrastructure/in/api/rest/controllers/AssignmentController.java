package com.udla.markenx.classroom.infrastructure.in.api.rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.classroom.application.dtos.mappers.AssignmentStatusMapper;
import com.udla.markenx.classroom.application.dtos.responses.AssignmentStatusResponseDTO;
import com.udla.markenx.classroom.application.ports.in.api.rest.controllers.AssignmentControllerPort;

@RestController
@RequestMapping("/api/markenx")
public class AssignmentController implements AssignmentControllerPort {

	@Override
	@GetMapping("/assignments/status")
	public ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus() {
		List<AssignmentStatusResponseDTO> statusList = AssignmentStatusMapper.toDtoList();
		return ResponseEntity.ok(statusList);
	}
}
