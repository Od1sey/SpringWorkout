package com.entity;

import java.time.LocalDateTime;

public class Book {

    private int id;
    private String name;
    private LocalDateTime addedAt;

    public Book(){

    }

    public Book(int id, String name, LocalDateTime addedAt) {
        this.id = id;
        this.name = name;
        this.addedAt = addedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
