package com.service;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final BookService bookService;

    @Autowired
    public AuthorService(AuthorRepo authorRepo, BookService bookService) {
        this.authorRepo = authorRepo;
        this.bookService = bookService;
    }

    public Author buildAuthor(String fullName) {
        fullName = fullName.trim();
        String[] parts = fullName.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong author name. Please enter lastname and firstname");
        }
        return new Author(parts[0], parts[1]);
    }

    public List<Book> buildAuthorBookList(String booksInput) {
        List<Book> authorBookList = new ArrayList<>();
        String[] books = booksInput.split("\\s*;\\s*");
        for (String book : books) {
            String[] bookParts = book.split("\\s*,\\s*");
            if (bookParts.length != 2) {
                throw new IllegalArgumentException("Wrong book info. Please enter name of a book and it's publish year");
            }
            try {
                int publishYear = Integer.parseInt(bookParts[1]);
                authorBookList.add(new Book(bookParts[0], publishYear));
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Wrong publish year %s for book %s".formatted(bookParts[1], bookParts[0]));
            }
        }
        return authorBookList;
    }

    @Transactional
    public void createRecord(AuthorWithBooksDTO dto) {
        Author newAuthor = authorRepo.createRecord(dto.getAuthor());
        if (!dto.getBooks().isEmpty()) {
            for (Book book : dto.getBooks()) {
                book.setAuthorId(newAuthor.getId());
                bookService.createRecord(book);
            }
        }
        throw new RuntimeException("Test exception");
    }

    public Optional<Author> getRecordById(int id) {
        return authorRepo.getRecordById(id);
    }

    public List<Author> getAllRecords() {
        return authorRepo.getAllRecords();
    }

    public void delete(int id) {
        authorRepo.delete(id);
    }

}
