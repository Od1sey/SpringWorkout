package unit.service;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.AuthorService;
import com.service.BookService;
import com.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    AuthorService authorService;
    @Mock
    BookService bookService;
    @Mock
    AuthorRepo authorRepo;
    @Mock
    BookRepo bookRepo;
    @InjectMocks
    LibraryService libraryService;


    @Test
    void getAuthorBooksShouldRejectInvalidInput() {
        String authorInput = "two";
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.getAuthorBooks(authorInput));
        verifyNoInteractions(bookService);
        verifyNoInteractions(authorService);
    }

    @Test
    void shouldReturnAuthorWithBooks() {
        String id = "1";
        int authorId = 1;
        Author existingAuthor = new Author("Test", "Author");
        List<Book> existingAuthorBooks = List.of(
                new Book("Test book 1", 2015),
                new Book("Test Book 2", 2017));
        when(authorService.getRequiredById(authorId)).thenReturn(existingAuthor);
        when(bookService.getRecordsByAuthorId(authorId)).thenReturn(existingAuthorBooks);
        AuthorWithBooksDTO dto = libraryService.getAuthorBooks(id);
        assertEquals(existingAuthor, dto.getAuthor());
        assertEquals(existingAuthorBooks, dto.getBooks());
        verify(authorService).getRequiredById(authorId);
        verify(bookService).getRecordsByAuthorId(authorId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "one, 2",
            "1, two"
    })
    void updateBookAuthorShouldRejectInvalidInput(String input) {
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.updateBookAuthor(input));
        verifyNoInteractions(bookService);
        verifyNoInteractions(authorService);
    }

    @Test
    void updateBookAuthorShouldRejectUnknownBook() {
        int authorId = 5;
        int bookId = 5;
        String input = "%s,%s".formatted(bookId, authorId);
        when(authorService.getRequiredById(authorId)).thenReturn(
                new Author("Test", "Author"));
        when(bookService.getRequiredById(bookId)).thenThrow(
                new IllegalArgumentException("Book with id %s doesn't exist".formatted(bookId)));
        assertThrows(IllegalArgumentException.class, () -> libraryService.updateBookAuthor(input));
        verify(bookService, never()).updateAuthor(any(Book.class), any(Author.class));
    }

    @Test
    void updateBookAuthorShouldRejectUnknownAuthor() {
        int authorId = 5;
        int bookId = 5;
        String input = "%s,%s".formatted(bookId, authorId);
        when(authorService.getRequiredById(authorId)).thenThrow(new IllegalArgumentException("Author with id %s doesn't exist".formatted(authorId)));
        assertThrows(IllegalArgumentException.class, () -> libraryService.updateBookAuthor(input));
        verify(bookService, never()).updateAuthor(any(Book.class), any(Author.class));
    }

    @Test
    void shouldUpdateBookAuthor() {
        int authorId = 5;
        int bookId = 5;
        String input = "%s,%s".formatted(bookId, authorId);
        Author author = new Author("Test", "Author");
        Book book = new Book("Test book", 2015);
        book.setId(bookId);
        author.setId(authorId);
        when(authorService.getRequiredById(authorId)).thenReturn(author);
        when(bookService.getRequiredById(bookId)).thenReturn(book);
        libraryService.updateBookAuthor(input);
        verify(bookService).getRequiredById(bookId);
        verify(bookService).updateAuthor(book, author);
    }

    @Test
    void deleteAuthorShouldRejectInvalidInput() {
        String input = "one";
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.deleteAuthor(input));
        verifyNoInteractions(authorService);
        verifyNoInteractions(bookService);
    }

    @Test
    void deleteAuthorShouldRejectUnknownAuthor() {
        int authorId = 5;
        when(authorService.getRequiredById(authorId)).thenThrow(
                new IllegalArgumentException("Author with id %s doesn't exist".formatted(authorId)));
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.deleteAuthor(String.valueOf(authorId)));
        verifyNoInteractions(bookService);
    }

    @Test
    void deleteAuthorShouldRejectAuthorWithBooks() {
        int authorId = 5;
        Author author = new Author("Test", "Author");
        when(authorService.getRequiredById(authorId)).thenReturn(author);
        when(bookService.getRecordsByAuthorId(authorId)).thenReturn(List.of(
                new Book("Existing book one", 2015),
                new Book("Existing book one", 2015),
                new Book("Existing book one", 2015)
        ));
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.deleteAuthor(String.valueOf(authorId)));
        verify(authorService).getRequiredById(authorId);
        verify(bookService).getRecordsByAuthorId(authorId);
        verify(authorService, never()).delete(authorId);
    }

    @Test
    void shouldDeleteAuthor() {
        int authorId = 5;
        Author author = new Author("Test", "Author");
        when(authorService.getRequiredById(authorId)).thenReturn(author);
        when(bookService.getRecordsByAuthorId(authorId)).thenReturn(List.of());
        assertDoesNotThrow(() -> {
            libraryService.deleteAuthor(String.valueOf(authorId));
        });
        verify(authorService).getRequiredById(authorId);
        verify(bookService).getRecordsByAuthorId(authorId);
        verify(authorService).delete(authorId);
    }

    @Test
    void createAuthorWithBooksShouldRejectMissingAuthor() {
        assertThrows(IllegalArgumentException.class, () ->
                libraryService.createAuthorWithBooks(null, "Test book, 2020")
        );
        verifyNoInteractions(authorService);
        verifyNoInteractions(bookService);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void createAuthorWithBooksShouldRejectMissingBooksInput(String input) {
        Author author = new Author("Test", "Author");
        assertThrows(IllegalArgumentException.class, () -> {
            libraryService.createAuthorWithBooks(author, input);
        });
    }

    @Test
    void createAuthorWithBooksShouldCreateAuthorAndBook() {
        int authorId = 10;
        Author newAuthor = new Author("Test", "Author");
        Book book = new Book("Test book", 2015);
        when(bookService.buildBook("Test book, 2015"))
                .thenReturn(book);
        when(authorService.createRecord(newAuthor)).thenReturn(newAuthor);
        AuthorWithBooksDTO result =
                libraryService.createAuthorWithBooks(
                        newAuthor,
                        "Test book, 2015"
                );
        assertEquals(newAuthor, result.getAuthor());
        assertEquals(List.of(book), result.getBooks());
        verify(authorService).createRecord(newAuthor);
        verify(bookService).buildBook("Test book, 2015");
        verify(bookService, never()).createRecord(book);
    }
}
