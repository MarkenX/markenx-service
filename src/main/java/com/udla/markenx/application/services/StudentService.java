package com.udla.markenx.application.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.udla.markenx.application.dtos.mappers.TaskMapper;
import com.udla.markenx.application.dtos.responses.TaskResponseDTO;
import com.udla.markenx.application.ports.out.repositories.TaskRepositoryPort;
import com.udla.markenx.core.valueobjects.RangeDate;
import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

@Service
public class StudentService {
  private final TaskRepositoryPort taskRepository;

  public StudentService(TaskRepositoryPort taskRepository) {
    this.taskRepository = taskRepository;
  }

  public Page<TaskResponseDTO> getStudentTasks(long studentId, LocalDate startDate, LocalDate endDate,
      AssignmentStatus status, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);
    if (startDate != null || endDate != null) {
      RangeDate rangeDate = new RangeDate(startDate, endDate);
      if (status != null) {
        return taskRepository.getStudentTasksByDueDateAndStatus(studentId, rangeDate, status, pageable)
            .map(TaskMapper::toResponseDto);
      } else {
        return taskRepository.getStudentTasksByDueDate(studentId, rangeDate, pageable)
            .map(TaskMapper::toResponseDto);
      }
    } else if (status != null) {
      return taskRepository.getStudentTasksByStatus(studentId, status, pageable)
          .map(TaskMapper::toResponseDto);
    } else {
      return taskRepository.getTasksByStudentId(studentId, pageable)
          .map(TaskMapper::toResponseDto);
    }

  }
}
