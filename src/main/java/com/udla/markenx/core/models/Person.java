package com.udla.markenx.core.models;

public class Person {
    private long id;
    private String name;
    private String lastName;

    public Person(long id, String name, String lastname) {
        this.id = id;
        this.name = name;
        this.lastName = lastname;
    }

    // #region Getters

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    // #endregion
}
