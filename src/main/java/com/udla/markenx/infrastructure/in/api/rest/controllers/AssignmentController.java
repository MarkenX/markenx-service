package com.udla.markenx.infrastructure.in.api.rest.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udla.markenx.application.interfaces.in.dtos.AssignmentStatusResponseDTO;
import com.udla.markenx.application.interfaces.in.mappers.AssignmentStatusMapper;

@RestController
@RequestMapping("/api/markenx")
public class AssignmentController {

    @GetMapping("/assignments/status")
    public ResponseEntity<List<AssignmentStatusResponseDTO>> getAssignmentStatus() {
        List<AssignmentStatusResponseDTO> statusList = AssignmentStatusMapper.toDtoList();

        return ResponseEntity.ok(statusList);
    }
}
