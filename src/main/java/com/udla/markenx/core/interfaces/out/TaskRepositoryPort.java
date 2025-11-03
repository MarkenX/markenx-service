package com.udla.markenx.core.interfaces.out;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.udla.markenx.core.enums.AssignmentStatus;
import com.udla.markenx.core.models.Task;

public interface TaskRepositoryPort {
    Page<Task> getTasksByStudentId(Long studentId, int page, int size);

    Page<Task> getStudentTasksByDueDate(Long studentId, LocalDate startDate, LocalDate endDate, int page, int size);

    Page<Task> getStudentTasksByStatus(Long studentId, AssignmentStatus status, int page, int size);

    Page<Task> getStudentTasksByDueDateAndStatus(Long studentId, LocalDate startDate, LocalDate endDate,
            AssignmentStatus status, int page, int size);
}
