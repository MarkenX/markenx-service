package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "assignments")
@Inheritance(strategy = InheritanceType.JOINED)
public class AssignmentJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assignment_id")
	private Long id;

	@Column(name = "assignment_title")
	private String title;

	@Column(name = "assignment_summary")
	private String summary;

	@Enumerated(EnumType.STRING)
	@Column(name = "assignment_current_status")
	private AssignmentStatus currentStatus;

	@Column(name = "assignment_duedate")
	@Temporal(TemporalType.DATE)
	private LocalDate dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	private CourseJpaEntity course;

	public AssignmentJpaEntity(
			String title,
			String summary,
			AssignmentStatus currentStatus,
			LocalDate dueDate,
			CourseJpaEntity student) {
		this.title = title;
		this.summary = summary;
		this.currentStatus = currentStatus;
		this.dueDate = dueDate;
		this.course = student;
	}
}
