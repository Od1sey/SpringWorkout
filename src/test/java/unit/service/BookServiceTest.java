package unit.service;

import com.entity.Book;
import com.repository.BookRepo;
import com.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepo bookRepo;
    @InjectMocks
    BookService bookService;

    @Test
    void createRecord() {
        Book newBook = new Book("Book from test", 2015);
        assertDoesNotThrow(() -> {
            bookService.createRecord(newBook);
        });
        verify(bookRepo).save(newBook);
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
        String bookInput = "Book name, 2015";
        Book builtBook = bookService.buildBook(bookInput);
        assertEquals("Book name", builtBook.getName());
        assertEquals(2015, builtBook.getPublishYear());
    }

    @Test
    void shouldReturnAllRecords() {
        List<Book> mockBooks = List.of(
                new Book("First test book", 2025),
                new Book("Second test book", 2026));
        when(bookRepo.findAll()).thenReturn(mockBooks);
        assertEquals(bookRepo.findAll(), mockBooks);
        verify(bookRepo).findAll();
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
