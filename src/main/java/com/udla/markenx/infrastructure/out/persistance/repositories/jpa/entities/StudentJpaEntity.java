package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "students")
@EqualsAndHashCode(callSuper = true)
public class StudentJpaEntity extends PersonJpaEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long id;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "keycloak_user_id", unique = true)
	private String keycloakUserId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id")
	private CourseJpaEntity course;

	public StudentJpaEntity(String firstName, String lastName) {
		super(firstName, lastName);
	}

	public StudentJpaEntity(String firstName, String lastName, String email) {
		super(firstName, lastName);
		this.email = email;
	}
}
