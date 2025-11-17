package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tasks")
@PrimaryKeyJoinColumn(name = "id")
@EqualsAndHashCode(callSuper = true)
public class TaskJpaEntity extends AssignmentJpaEntity {
	@Column(name = "task_max_attempts")
	private int maxAttempts;

	@Column(name = "attempt_minimum_score_to_pass")
	private double minScoreToPass;
}
