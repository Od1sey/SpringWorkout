package com.service;

import com.entity.Author;
import com.entity.Book;
import com.repository.BookRepo;
import com.util.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book buildBook(String bookInfo) {
        String[] parts = bookInfo.split("\\s*,\\s*");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong book info provided.");
        }
        String name = parts[0];
        int publishYear = ParseUtils.parseInt(parts[1], "%s is not a valid year".formatted(parts[1]));
        if (name.isBlank()) {
            throw new IllegalArgumentException("Book name cannot be blank");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Book name must be at least 3 characters long");
        }

        return new Book(name, publishYear);
    }

    public void createRecord(Book book) {
        bookRepo.save(book);
    }

    public List<Book> getAllRecords() {
        return bookRepo.findAll();
    }

    public List<Book> getRecordsByQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be blank");
        }
        if (query.length() < 3) {
            throw new IllegalArgumentException("Query must be at least 3 characters long");
        }
        if ("author".equalsIgnoreCase(query)) {
            return bookRepo.findByAuthorFullName(query);
        } else {
            return bookRepo.findByNameContaining(query);
        }
    }

    public Optional<Book> getRecordById(int id) {
        return bookRepo.findById(id);
    }

    public Book getRequiredById(int id) {
        return getRecordById(id).orElseThrow(() ->
                new IllegalArgumentException("Book with id %s doesn't exist".formatted(id)));
    }

    public void deleteRecord(String id) {
        int bookId = ParseUtils.parseInt(id, "Book id should be a number");
        bookRepo.delete(getRequiredById(bookId));
    }

    public List<Book> getRecordsByAuthorId(int id) {
        return bookRepo.findByAuthorId(id);
    }

    public List<Book> getRecordsByAuthorFullName(String query) {
        return bookRepo.findByAuthorFullName(query);
    }

    public void updateAuthor(Book book, Author author){
        book.setAuthor(author);
        bookRepo.save(book);
    }

}
