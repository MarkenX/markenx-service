package com.udla.markenx.core.models;

import com.udla.markenx.core.interfaces.Person;

public class Student extends Person {

	public Student(String firstName, String lastName) {
		super(firstName, lastName);
	}

	public Student(long id, String firstName, String lastname) {
		super(id, firstName, lastname);
	}
}
