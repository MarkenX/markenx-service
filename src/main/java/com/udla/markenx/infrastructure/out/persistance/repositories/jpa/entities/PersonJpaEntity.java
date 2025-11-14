package com.udla.markenx.infrastructure.out.persistance.repositories.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base entity for persons in the system.
 * 
 * Extends AuditableEntity to automatically track:
 * - Who created/modified the record (admin email)
 * - When the record was created/modified
 * 
 * Audit fields only track admin actions since students can only view/upload
 * data.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonJpaEntity extends AuditJpaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "person_id")
	private Long id;

	@Column(name = "person_firstname")
	private String firstName;

	@Column(name = "person_lastname")
	private String lastName;

	public PersonJpaEntity(String fistName, String lastName) {
		this.firstName = fistName;
		this.lastName = lastName;
	}
}
