package unit.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import com.service.AuthorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuthorServiceTest {

    AuthorRepo authorRepo = mock(AuthorRepo.class);
    AuthorService authorService = new AuthorService(authorRepo);

    @Test
    void buildAuthorRejectsInvalidInput() {
        String invalidInput = "LastName_FirstName";
        assertThrows(IllegalArgumentException.class,()->{
            authorService.buildAuthor(invalidInput);
        });
    }

    @Test
    void shouldBuildRecord(){
        String authorInput = "Test Author";
        assertDoesNotThrow(()-> authorService.buildAuthor(authorInput));
    }

    @Test
    void shouldCreateRecord() {
        Author newAuthor = new Author("Test", "Author");
        assertDoesNotThrow(()->authorService.createRecord(newAuthor));
        verify(authorRepo).createRecord(newAuthor);
    }

}
