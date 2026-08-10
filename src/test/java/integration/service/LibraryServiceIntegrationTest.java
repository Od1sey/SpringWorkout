package integration.service;

import com.dto.AuthorWithBooksDTO;
import com.entity.Author;
import com.entity.Book;
import com.service.AuthorService;
import com.service.BookService;
import com.service.LibraryService;
import integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(LibraryServiceTestConfig.class)
class LibraryServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    LibraryService libraryService;
    @Autowired
    AuthorService authorService;
    @Autowired
    BookService bookService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("TRUNCATE TABLE books, authors RESTART IDENTITY CASCADE");
    }

    @Test
    void shouldCreateAuthorWithTwoBooks() {
        Author newAuthor = new Author("Test", "Author");
        AuthorWithBooksDTO result = libraryService.createAuthorWithBooks(
                newAuthor, "First Book, 2015;Second Book, 2020");

        assertNotNull(result.getAuthor());
        assertTrue(result.getAuthor().getId() > 0);
        assertEquals("Test Author", result.getAuthor().getFullName());
        assertEquals(2, result.getBooks().size());
        assertEquals("First Book", result.getBooks().get(0).getName());
        assertEquals(2015, result.getBooks().get(0).getPublishYear());
        assertEquals("Second Book", result.getBooks().get(1).getName());
        assertEquals(2020, result.getBooks().get(1).getPublishYear());

        Optional<Author> savedAuthor = authorService.getRecordById(result.getAuthor().getId());
        assertTrue(savedAuthor.isPresent());

        List<Book> savedBooks = bookService.getRecordsByAuthorId(result.getAuthor().getId());
        assertEquals(2, savedBooks.size());
    }

    @Test
    void shouldRollbackAuthorAndFirstBookWhenSecondBookFails() {
        Author newAuthor = new Author("Test", "Author");
        String longName = "Book".repeat(65);
        String booksInput = "First Book, 2020;%s, 2021".formatted(longName);
        assertThrows(RuntimeException.class, () ->
                libraryService.createAuthorWithBooks(newAuthor, booksInput));
        Optional<Author> savedAuthor = authorService.getRecordById(newAuthor.getId());
        assertTrue(savedAuthor.isEmpty());
        List<Book> savedBooks = bookService.getRecordsByAuthorId(newAuthor.getId());
        assertTrue(savedBooks.isEmpty());
    }
}