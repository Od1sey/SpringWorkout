package com.service;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.entity.Book;
import com.util.ParseUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibraryService {

    private final AuthorService authorService;
    private final BookService bookService;

    public LibraryService(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    public AuthorWithBooksDTO getAuthorBooks(String id) {
        int authorId = ParseUtils.parseInt(id, "Author id must be a number");
        Author author = authorService.getRecordById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id %s was not found".formatted(id)));
        List<Book> authorBooks = bookService.getRecordsByAuthorId(authorId);
        return new AuthorWithBooksDTO(author, authorBooks);
    }

    public void updateBookAuthor(String input) {
        String[] parts = input.split("\\s*,\\s*");
        int bookId = ParseUtils.parseInt(parts[0], "'%s' is invalid book id".formatted(parts[0]));
        int authorId = ParseUtils.parseInt(parts[1], "'%s' is author book id".formatted(parts[1]));
        Author author = authorService.getRecordById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id %s doesn't exist".formatted(authorId)));
        Book book = bookService.getRecordById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id %s doesn't exist".formatted(bookId)));
        bookService.updateRecordAuthor(bookId, authorId);
        System.out.printf("Book %s (id: %d) was assigned to %s (id: %d) \n",
                book.getName(), book.getId(), author.getFullName(), author.getId());
    }

    public void deleteAuthor(String input) {
        int authorId = ParseUtils.parseInt(input,
                "Author with id %s doesn't exist");
        Author author = authorService.getRecordById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Author with id %s doesn't exist".formatted(authorId)));
        List<Book> authorBooks = bookService.getRecordsByAuthorId(authorId);
        if (!authorBooks.isEmpty()) {
            throw new IllegalArgumentException("%s has %d books. Delete them first to delete an author"
                    .formatted(author.getFullName(), authorBooks.size()));
        }
        authorService.delete(authorId);
    }

    @Transactional
    public AuthorWithBooksDTO createAuthorWithBooks(Author newAuthor, String booksInput){
        if (newAuthor == null){
            throw new IllegalArgumentException("Author must be provided");
        }
        if (booksInput==null || booksInput.isEmpty()){
            throw new IllegalArgumentException("You did not provide any book info");
        }
        Author createdAuthor = authorService.createRecord(newAuthor);
        List<Book> authorBookList = new ArrayList<>();
        String[] books = booksInput.split("\\s*;\\s*");
        for (String book : books) {
            book = "%s, %s".formatted(book, createdAuthor.getId());
            authorBookList.add(bookService.buildBook(book));
        }
        for (Book book : authorBookList){
            book.setAuthorId(createdAuthor.getId());
            bookService.createRecord(book);
        }
        return new AuthorWithBooksDTO(createdAuthor, authorBookList);
    }

}
