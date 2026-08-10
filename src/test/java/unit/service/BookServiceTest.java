package unit.service;

import com.entity.Book;
import com.repository.BookRepo;
import com.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    BookRepo bookRepo = mock(BookRepo.class);
    BookService bookService = new BookService(bookRepo);

    @Test
    void createRecord() {
        Book newBook = new Book("Book from test", 2015);
        assertDoesNotThrow(() -> {
            bookService.createRecord(newBook);
        });
        verify(bookRepo).create(newBook);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Book name",
            "Book name, year, 1",
            "Book name, 2015, id"
    })
    void buildBookShouldRejectInvalidInput(String input) {
        assertThrows(IllegalArgumentException.class, () ->
                bookService.buildBook(input));
    }

    @Test
    void shouldBuildBook() {
        String bookInput = "Book name, 2015, 1";
        assertDoesNotThrow(() -> bookService.buildBook(bookInput));
    }

    @Test
    void shouldReturnAllRecords() {
        List<Book> mockBooks = List.of(
                new Book("First test book", 2025),
                new Book("Second test book", 2026));
        when(bookRepo.getAll()).thenReturn(mockBooks);
        assertEquals(bookRepo.getAll(), mockBooks);
        verify(bookRepo).getAll();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "12"
    })
    void getRecordsByQueryShouldRejectInvalidQuery(String input) {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getRecordsByQuery(input);
        });
    }

    @Test
    void deleteRecordShouldRejectInvalidInput() {
        String input = "Not a number";
        assertThrows(IllegalArgumentException.class, () -> bookService.deleteRecord(input));
    }

}
