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
public class AssignmentJpaEntity extends BaseJpaEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_assignment_id")
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
	@JoinColumn(name = "course_id")
	private CourseJpaEntity course;
}
