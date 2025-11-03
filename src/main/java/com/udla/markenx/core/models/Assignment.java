package com.udla.markenx.core.models;

import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.AssignmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    private Long id;
    private String title;
    private String summnary;
    private LocalDate dueDate;
    private AssignmentStatus currentStatus;

    public Assignment(
            String title,
            String summary,
            LocalDate dueDate,
            AssignmentStatus currentStatus) {
        this.title = title;
        this.summnary = summary;
        this.currentStatus = currentStatus;
    }
}
