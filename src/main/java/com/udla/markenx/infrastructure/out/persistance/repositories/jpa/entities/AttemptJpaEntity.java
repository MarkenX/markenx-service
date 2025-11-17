package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.Duration;

import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attempts")
public class AttemptJpaEntity extends BaseJpaEntity {
	@Column(name = "attempt_score")
	private double score;

	@Column(name = "attempt_duration")
	private Duration duration;

	@Enumerated(EnumType.STRING)
	@Column(name = "attempt_result")
	private AttemptResult result;

	@Enumerated(EnumType.STRING)
	@Column(name = "attempt_current_status")
	private AttemptStatus currentStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_task_id")
	private StudentAssignmentJpaEntity studentTask;
}
