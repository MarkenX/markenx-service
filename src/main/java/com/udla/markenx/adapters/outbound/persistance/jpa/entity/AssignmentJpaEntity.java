package com.udla.markenx.adapters.outbound.persistance.jpa.entity;

import java.time.LocalDate;

import com.udla.markenx.domain.model.AssignmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class AssignmentJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @Column(name = "assignment_title")
    private String title;

    @Column(name = "assignment_summary")
    private String summnary;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_current_status")
    private AssignmentStatus currentStatus;

    @Column(name = "assignment_duedate")
    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentJpaEntity student;

    public AssignmentJpaEntity(
            String title,
            String summary,
            AssignmentStatus currentStatus,
            LocalDate dueDate,
            StudentJpaEntity student) {
        this.title = title;
        this.summnary = summary;
        this.currentStatus = currentStatus;
        this.dueDate = dueDate;
        this.student = student;
    }
}
