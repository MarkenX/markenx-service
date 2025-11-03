package com.udla.markenx.core.models;

import com.udla.markenx.core.interfaces.Person;

public class Student extends Person {

    public Student(
            long id,
            String name,
            String lastname) {
        super(id, name, lastname);
    }
}
