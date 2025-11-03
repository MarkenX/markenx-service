package com.udla.markenx.api.outbound.persistance.jpa.entity;

import java.time.LocalDate;

import com.udla.markenx.core.enums.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskJpaEntity extends AssignmentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_active_attempt")
    private int activeAttempt;

    @Column(name = "task_max_attempts")
    private int maxAttempts;

    public TaskJpaEntity(
            String title,
            String summary,
            AssignmentStatus currentStatus,
            LocalDate dueDate,
            int activeAttempt,
            int maxAttempts,
            StudentJpaEntity student) {
        super(title, summary, currentStatus, dueDate, student);
        this.activeAttempt = activeAttempt;
        this.maxAttempts = maxAttempts;
    }
}
