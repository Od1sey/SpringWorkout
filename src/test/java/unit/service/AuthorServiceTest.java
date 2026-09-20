package unit.service;

import com.entity.Author;
import com.entity.Book;
import com.exception.RecordNotFoundException;
import com.repository.AuthorRepo;
import com.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepo authorRepo ;
    @InjectMocks
    AuthorService authorService;

    @Test
    void shouldCreateRecord() {
        Author newAuthor = new Author("Test", "Author");
        when(authorRepo.save(newAuthor)).thenReturn(newAuthor);
        Author createdAuthor = authorService.createRecord(newAuthor);
        assertEquals(newAuthor, createdAuthor);

        verify(authorRepo).save(newAuthor);
    }

    @Test
    void getReturnAllRecords() {
        Author author1 = new Author();
        Author author2 = new Author();

        List<Author> authors = List.of(author1, author2);
        when(authorRepo.findAll())
                .thenReturn(authors);
        List<Author> result = authorService.getAllRecords();
        assertSame(authors, result);

        verify(authorRepo).findAll();
    }

    @Test
    void shouldUpdateAuthorName(){
        int id = 1;

        Author existingAuthor = new Author(
                "Existing", "Author"
        );
        Author updateRequest = new Author(
                "Updated", "Author"
        );

        when(authorRepo.findById(id)).thenReturn(Optional.of(existingAuthor));
        Author result = authorService.updateAuthorName(id, updateRequest);

        assertSame(existingAuthor, result);
        assertEquals("Updated", result.getLastName());
        assertEquals("Author", result.getFirstName());
        assertEquals("Updated Author", result.getFullName());

        verify(authorRepo).findById(id);
    }

    @Test
    void shouldReturnAuthorWithBookList(){
        int id = 1;

        Book book1 = new Book();
        Book book2 = new Book();

        List<Book> originalBooks = new ArrayList<>();
        originalBooks.add(book1);
        originalBooks.add(book2);
        Author author = new Author();
        author.setBooks(originalBooks);

        when(authorRepo.findById(id))
                .thenReturn(Optional.of(author));
        Author result = authorService.getAuthor(id);
        assertSame(author, result);

        assertEquals(2, result.getBooks().size());
        assertEquals(originalBooks, result.getBooks());
        assertNotSame(originalBooks, result.getBooks());

        verify(authorRepo).findById(id);
    }

    @Test
    void getAuthorShouldThrowRecordNotFoundException(){
        int id = 1000;

        when(authorRepo.findById(id)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class,
                ()-> authorService.getAuthor(id));

        verify(authorRepo).findById(id);
    }

    @Test
    void deleteShouldDeleteExistingAuthor() {
        int id = 1;

        Author author = new Author();

        when(authorRepo.findById(id))
                .thenReturn(Optional.of(author));
        authorService.delete(id);

        verify(authorRepo).findById(id);
        verify(authorRepo).delete(author);
    }

    @Test
    void deleteShouldThrowWhenAuthorDoesNotExist() {
        int id = 999;

        when(authorRepo.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(
                RecordNotFoundException.class,
                () -> authorService.delete(id)
        );

        verify(authorRepo, never()).delete(any());
    }

}
