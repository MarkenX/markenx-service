package com.udla.markenx.classroom.infrastructure.out.persistance.repositories.jpa.entities;

import com.udla.markenx.shared.infrastructure.out.data.persistence.jpa.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "persons")
@PrimaryKeyJoinColumn(name = "id")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonJpaEntity extends BaseJpaEntity {
	@Column(name = "person_firstname")
	private String firstName;

	@Column(name = "person_lastname")
	private String lastName;
}
