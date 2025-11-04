package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.time.Duration;
import java.time.LocalDate;

import com.udla.markenx.core.valueobjects.enums.AttemptResult;
import com.udla.markenx.core.valueobjects.enums.AttemptStatus;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attempt_id")
	private Long id;

	@Column(name = "attempt_score")
	private double score;

	@Column(name = "attempt_date")
	private LocalDate date;

	@Column(name = "attempt_duration")
	private Duration duration;

	@Column(name = "attempt_result")
	private AttemptResult result;

	@Column(name = "attempt_current_status")
	private AttemptStatus currentStatus;

	@ManyToOne
	@JoinColumn(name = "task_id")
	private TaskJpaEntity task;
}
