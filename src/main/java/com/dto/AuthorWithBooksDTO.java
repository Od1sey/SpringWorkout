package com.dto;

import com.entity.Author;
import com.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class AuthorWithBooksDTO{

    private Author author;
    private List<Book> books = new ArrayList<>();

    public AuthorWithBooksDTO(Author author){
        this.author = author;
    }

    public AuthorWithBooksDTO(Author author, List<Book> books){
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
        return "AuthorWithBooksDTO{" +
                "author=" + author +
                ", books=" + books +
                '}';
    }
}
