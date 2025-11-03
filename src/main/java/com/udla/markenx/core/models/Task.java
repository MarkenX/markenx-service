package com.udla.markenx.core.models;

import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.AssignmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Task extends Assignment {
    private int activeAttempt;
    private int maxAttempts;

    public Task(
            String title,
            String summary,
            AssignmentStatus currentStatus,
            int activeAttempt,
            int maxAttempts,
            LocalDate dueDate) {
        super(title, summary, dueDate, currentStatus);
        this.activeAttempt = activeAttempt;
        this.maxAttempts = maxAttempts;
    }
}
