package com.udla.markenx.classroom.application.ports.in.api.rest.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import com.udla.markenx.classroom.application.dtos.requests.CreateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.requests.UpdateTaskRequestDTO;
import com.udla.markenx.classroom.application.dtos.responses.TaskResponseDTO;

public interface TaskControllerPort {

  ResponseEntity<Page<TaskResponseDTO>> getAllTasks(Pageable pageable);

  ResponseEntity<TaskResponseDTO> getTaskById(UUID id);

  ResponseEntity<TaskResponseDTO> createTask(@Valid CreateTaskRequestDTO request);

  ResponseEntity<TaskResponseDTO> updateTask(UUID id, @Valid UpdateTaskRequestDTO request);

  ResponseEntity<Void> disableTask(UUID id);

  ResponseEntity<Void> enableTask(UUID id);
}
