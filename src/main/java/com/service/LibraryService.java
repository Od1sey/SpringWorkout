package com.service;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.entity.Book;
import com.util.ParseUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Author author = authorService.getRequiredById(authorId);
        List<Book> authorBooks = bookService.getRecordsByAuthorId(authorId);
        return new AuthorWithBooksDTO(author, authorBooks);
    }

    public void updateBookAuthor(String input) {
        String[] parts = input.split("\\s*,\\s*");
        int bookId = ParseUtils.parseInt(parts[0], "'%s' is invalid book id".formatted(parts[0]));
        int authorId = ParseUtils.parseInt(parts[1], "'%s' is author book id".formatted(parts[1]));
        Author author = authorService.getRequiredById(authorId);
        Book book = bookService.getRequiredById(bookId);
        updateBookAuthor(book, author);
        System.out.printf("Book %s (id: %d) was assigned to %s (id: %d) \n",
                book.getName(), book.getId(), author.getFullName(), author.getId());
    }

    public void deleteAuthor(String input) {
        int authorId = ParseUtils.parseInt(input,"Invalid author id");
        Author author = authorService.getRequiredById(authorId);
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
        if (booksInput==null || booksInput.isBlank()){
            throw new IllegalArgumentException("You did not provide any book info");
        }
        String[] books = booksInput.split("\\s*;\\s*");
        for (String book : books) {
            newAuthor.addBook(bookService.buildBook(book));
        }
        Author createdAuthor = authorService.createRecord(newAuthor);
        return new AuthorWithBooksDTO(createdAuthor, createdAuthor.getBooks());
    }

    public void updateBookAuthor(Book book, Author author){
        bookService.updateAuthor(book, author);
    }

    public Book createBookForAuthor(String input) {
        int lastComma = input.lastIndexOf(',');
        String bookInfo = input.substring(0, lastComma).trim();
        int authorId = ParseUtils.parseInt(input.substring(lastComma+1).trim(),
                "Invalid author id was provided");
        Book book = bookService.buildBook(bookInfo);
        Author author = authorService.getRequiredById(authorId);
        book.setAuthor(author);
        return book;
    }

}
