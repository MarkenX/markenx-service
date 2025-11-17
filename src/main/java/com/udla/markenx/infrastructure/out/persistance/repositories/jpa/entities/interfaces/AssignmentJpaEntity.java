package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.udla.markenx.core.valueobjects.enums.AssignmentStatus;
import com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities.CourseJpaEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "assignments")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class AssignmentJpaEntity extends BaseJpaEntity {
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

	@OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StudentAssignmentJpaEntity> studentAssignments = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	private CourseJpaEntity course;
}
