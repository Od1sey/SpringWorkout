package com.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name="added_at")
    private LocalDateTime addedAt;
    @Column(name="publish_year")
    private int publishYear;

    public Book() {

    }

    public Book(String name, int publishYear) {
        this.name = name;
        this.publishYear = publishYear;
    }

    public Book(int id, String name, LocalDateTime addedAt,  int publishYear) {
        this.id = id;
        this.name = name;
        this.addedAt = addedAt;
        this.publishYear = publishYear;
    }

    @PrePersist
    private void prepare() {
        addedAt = LocalDateTime.now();
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

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
