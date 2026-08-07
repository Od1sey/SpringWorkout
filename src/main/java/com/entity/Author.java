package com.entity;

import java.time.LocalDateTime;

public class Author {

    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDateTime addedAt;

    public Author() {

    }

    public Author(String lastName, String firstName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = lastName + " " + firstName;
    }

    public Author(int id, String firstName, String lastName, String fullName, LocalDateTime addedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.addedAt = addedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fistName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
