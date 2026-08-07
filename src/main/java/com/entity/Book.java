package com.entity;

import java.time.LocalDateTime;

public class Book {

    private int id;
    private String name;
    private LocalDateTime addedAt;
    private int authorId;
    private int publishYear;

    public Book(){

    }

    public Book(String name, int publishYear){
        this.name = name;
        this.publishYear = publishYear;
    }

    public Book(int id, String name, LocalDateTime addedAt, int authorId, int publishYear) {
        this.id = id;
        this.name = name;
        this.addedAt = addedAt;
        this.authorId = authorId;
        this.publishYear = publishYear;
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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }
}
