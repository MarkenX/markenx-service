package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;

import jakarta.validation.Valid;

public interface TaskManagementControllerPort {

  ResponseEntity<TaskResponseDTO> createTask(@Valid CreateTaskRequestDTO request);

  ResponseEntity<TaskResponseDTO> updateTask(Long id, @Valid UpdateTaskRequestDTO request);

  ResponseEntity<TaskResponseDTO> getTaskById(Long id);

  ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable);

  ResponseEntity<Void> deleteTask(Long id);
}
