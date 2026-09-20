package com.dto;

import com.entity.Author;
import com.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorWithBooks {

    private Author author;
    private List<Book> books = new ArrayList<>();

    public AuthorWithBooks(Author author){
        this.author = author;
    }

    public AuthorWithBooks(Author author, List<Book> books){
        this.author = author;
        this.books = books;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "AuthorWithBooks{" +
                "author=" + author +
                ", books=" + books +
                '}';
    }
}
