package com.udla.markenx.application.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.udla.markenx.domain.model.AssignmentStatus;
import com.udla.markenx.domain.model.Task;
import com.udla.markenx.domain.port.out.TaskRepositoryPort;

@Service
public class TaskService {
    private final TaskRepositoryPort taskRepository;

    public TaskService(TaskRepositoryPort taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Page<Task> getStudentTasks(Long studentId, LocalDate startDate, LocalDate endDate, AssignmentStatus status, int page,
            int size) {
        if (studentId == null) {
            throw new IllegalArgumentException("El código del estudiante no puede ser nulo.");
        }

        if (page < 0)
            page = 0;
        if (size <= 0 || size > 100)
            size = 10;

        if (startDate != null && endDate != null && status != null) {
            return taskRepository.getStudentTasksByDueDateAndStatus(studentId, startDate, endDate, status, page, size);
        } else if (startDate != null && endDate != null) {
            return taskRepository.getStudentTasksByDueDate(studentId, startDate, endDate, page, size);
        } else if (status != null) {
            return taskRepository.getStudentTasksByStatus(studentId, status, page, size);
        } else {
            return taskRepository.getTasksByStudentId(studentId, page, size);
        }
    }
}
