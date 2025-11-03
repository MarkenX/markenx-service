package com.udla.markenx.adapters.outbound.persistance;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.udla.markenx.adapters.outbound.persistance.jpa.mapper.TaskMapper;
import com.udla.markenx.adapters.outbound.persistance.jpa.repository.TaskJpaRepository;
import com.udla.markenx.core.model.Task;
import com.udla.markenx.core.port.out.TaskRepositoryPort;
import com.udla.markenx.core.valueobjects.AssignmentStatus;

@Repository
public class TaskRepositoryAdapter implements TaskRepositoryPort {
    private final TaskJpaRepository jpaRepository;

    public TaskRepositoryAdapter(TaskJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Page<Task> getTasksByStudentId(Long studentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStudentId(studentId, pageable)
                .map(TaskMapper::toDomain);
    }

    @Override
    public Page<Task> getStudentTasksByDueDate(Long studentId, LocalDate startDate, LocalDate endDate, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStudentIdAndDueDateBetween(studentId, startDate, endDate, pageable)
                .map(TaskMapper::toDomain);
    }

    @Override
    public Page<Task> getStudentTasksByStatus(Long studentId, AssignmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStudentIdAndCurrentStatus(studentId, status, pageable)
                .map(TaskMapper::toDomain);
    }

    @Override
    public Page<Task> getStudentTasksByDueDateAndStatus(Long studentId, LocalDate startDate, LocalDate endDate,
            AssignmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository
                .findByStudentIdAndDueDateBetweenAndCurrentStatus(studentId, startDate, endDate, status, pageable)
                .map(TaskMapper::toDomain);
    }
}
