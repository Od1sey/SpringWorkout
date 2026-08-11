package unit.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import com.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(authorService.createRecord(newAuthor)).thenReturn(newAuthor);
        Author createdAuthor = authorService.createRecord(newAuthor);
        assertEquals(createdAuthor, authorService.createRecord(newAuthor));
        verify(authorService).createRecord(newAuthor);
    }

    @Test
    void shouldReturnAllRecords(){
        List<Author> storedAuthors = List.of(
                new Author("Test", "Author One"),
                new Author("Test", "Author Two"),
                new Author("Test", "Author Three"));
        when(authorService.getAllRecords()).thenReturn(storedAuthors);
        assertEquals(authorService.getAllRecords(), storedAuthors);
        verify(authorRepo).getAllRecords();
    }

}
