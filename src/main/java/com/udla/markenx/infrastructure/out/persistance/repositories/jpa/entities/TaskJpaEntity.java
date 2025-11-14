package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode(callSuper = true)
public class TaskJpaEntity extends AssignmentJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Long id;

	@Column(name = "task_max_attempts")
	private int maxAttempts;

	@Column(name = "attempt_minimum_score_to_pass")
	private double minScoreToPass;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AttemptJpaEntity> attempts = new ArrayList<>();
}
