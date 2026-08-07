package com.service;

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
        String name = parts[0];
        int publishYear;
        int authorId;
        try {
            publishYear = Integer.parseInt(parts[1]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("%s is not a valid year".formatted(parts[1]));
        }
        try {
            authorId = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("%s is not a valid author id".formatted(parts[2]));
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Book name cannot be blank");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Book name must be at least 3 characters long");
        }
        Book newBook = new Book(parts[0], publishYear);
        newBook.setAuthorId(authorId);
        return newBook;
    }

    public void createRecord(Book book) {
        bookRepo.create(book);
    }

    public List<Book> getAllRecords() {
        return bookRepo.getAll();
    }

    public List<Book> getRecordsByQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be blank");
        }
        if (query.length() < 3) {
            throw new IllegalArgumentException("Query must be at least 3 characters long");
        }
        return bookRepo.getRecordsByQuery(query);
    }

    public Optional<Book> getRecordById(int id) {
        return bookRepo.getRecordById(id);
    }

    public void updateRecordAuthor(int bookId, int authorId) {
        bookRepo.updateBookAuthor(bookId, authorId);
    }

    public void deleteRecord(String id) {
        int bookId = ParseUtils.parseInt(id, "Book id should be number");
        bookRepo.deleteRecord(bookId);
    }

    public List<Book> getRecordsByAuthorId(int id) {
        return bookRepo.getRecordsByAuthorId(id);
    }

}
