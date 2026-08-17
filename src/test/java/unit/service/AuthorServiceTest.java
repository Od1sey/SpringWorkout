package unit.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import com.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    AuthorRepo authorRepo = mock(AuthorRepo.class);
    AuthorService authorService = new AuthorService(authorRepo);

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "LastName_FirstName",
            "LastName MiddleName FirstName"
    })
    void buildAuthorRejectsInvalidInput(String input) {
        assertThrows(IllegalArgumentException.class,()->{
            authorService.buildAuthor(input);
        });
    }


    @Test
    void shouldBuildAuthor(){
        String authorInput = "Test Author";
        Author builtAuthor = authorService.buildAuthor(authorInput);
        assertEquals("Test", builtAuthor.getLastName());
        assertEquals("Author", builtAuthor.getFirstName());
    }

    @Test
    void shouldCreateRecord() {
        Author newAuthor = new Author("Test", "Author");
        when(authorRepo.save(newAuthor)).thenReturn(newAuthor);
        Author createdAuthor = authorService.createRecord(newAuthor);
        assertEquals(newAuthor, createdAuthor);
        verify(authorRepo).save(newAuthor);
    }

    @Test
    void shouldReturnAllRecords() {
        List<Author> storedAuthors = List.of(
                new Author("Test", "Author One"),
                new Author("Test", "Author Two"),
                new Author("Test", "Author Three")
        );
        when(authorRepo.findAll())
                .thenReturn(storedAuthors);
        List<Author> result = authorService.getAllRecords();
        assertEquals(storedAuthors, result);
        verify(authorRepo).findAll();
    }

    @Test
    void shouldUpdateAuthorFullName() {
        int authorId = 5;
        Author author = new Author("Old", "Name");
        author.setId(authorId);
        when(authorRepo.findById(authorId))
                .thenReturn(Optional.of(author));
        authorService.updateAuthorFullName("5, NewLastName NewFirstName");
        assertEquals("NewLastName", author.getLastName());
        assertEquals("NewFirstName", author.getFirstName());
        assertEquals("NewLastName NewFirstName", author.getFullName());
        verify(authorRepo).findById(authorId);
    }

}
