package unit.service;

import com.entity.Author;
import com.entity.Book;
import com.exception.RecordNotFoundException;
import com.repository.BookRepo;
import com.service.AuthorService;
import com.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepo bookRepo;
    @Mock
    AuthorService authorService;
    @InjectMocks
    BookService bookService;

    @Test
    void createRecordShouldResolveAuthorAndSaveBook() {
        int authorId = 99;

        Author authorReference = new Author(authorId);
        Author existingAuthor = new Author(authorId);

        Book newBook = new Book("Book from test", 2015);
        newBook.setAuthor(authorReference);

        when(authorService.getRequiredById(authorId))
                .thenReturn(existingAuthor);
        when(bookRepo.save(newBook))
                .thenReturn(newBook);

        Book result = bookService.createRecord(newBook);
        assertSame(existingAuthor, newBook.getAuthor());
        assertSame(newBook, result);

        verify(authorService).getRequiredById(authorId);
        verify(bookRepo).save(newBook);
    }

    @Test
    void createRecordShouldNotSaveBookWhenAuthorDoesNotExist() {
        int authorId = 999;

        Book book = new Book("Book from test", 2015);
        book.setAuthor(new Author(authorId));

        when(authorService.getRequiredById(authorId))
                .thenThrow(new RecordNotFoundException("Author", authorId));

        assertThrows(
                RecordNotFoundException.class,
                () -> bookService.createRecord(book)
        );

        verify(bookRepo, never()).save(any());
    }

    @Test
    void shouldReturnAllRecords() {
        List<Book> books = List.of(
                new Book("First test book", 2025),
                new Book("Second test book", 2026)
        );

        when(bookRepo.findAllWithAuthors())
                .thenReturn(books);

        List<Book> result = bookService.getAllRecords();
        assertSame(books, result);

        verify(bookRepo).findAllWithAuthors();
    }

    @Test
    void updateRecordShouldChangeAuthorWhenAuthorProvided() {
        int bookId = 1;
        int newAuthorId = 20;

        Author oldAuthor = new Author(10);
        Author newAuthor = new Author(newAuthorId);

        Book existingBook = new Book("Old name", 2000);
        existingBook.setAuthor(oldAuthor);

        Book updateInfo = new Book("New name", 2025);
        updateInfo.setAuthor(new Author(newAuthorId));

        when(bookRepo.findById(bookId))
                .thenReturn(Optional.of(existingBook));
        when(authorService.getRequiredById(newAuthorId))
                .thenReturn(newAuthor);
        when(bookRepo.save(existingBook))
                .thenReturn(existingBook);

        Book result = bookService.updateRecord(bookId, updateInfo);

        assertEquals("New name", result.getName());
        assertEquals(2025, result.getPublishYear());
        assertSame(newAuthor, result.getAuthor());

        verify(authorService).getRequiredById(newAuthorId);
        verify(bookRepo).save(existingBook);
    }

    @Test
    void updateRecordShouldThrowWhenBookDoesNotExist() {
        int id = 999;

        Book updateInfo = new Book("New name", 2025);
        when(bookRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(
                RecordNotFoundException.class,
                () -> bookService.updateRecord(id, updateInfo)
        );

        verify(bookRepo, never()).save(any());
        verifyNoInteractions(authorService);
    }

    @Test
    void deleteRecordShouldThrowRecordNotFoundException(){
        int id = 1000;

        when(bookRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class,
                ()-> bookService.deleteRecord(id));

        verify(bookRepo).findById(id);
        verify(bookRepo, never()).delete(any());
    }

    @Test
    void shouldDeleteRecord(){
        int id = 1000;
        Book book = new Book();

        when(bookRepo.findById(id)).thenReturn(Optional.of(book));
        bookService.deleteRecord(id);

        verify(bookRepo).findById(id);
        verify(bookRepo).delete(book);
    }

}
