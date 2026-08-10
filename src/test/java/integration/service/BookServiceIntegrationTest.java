package integration.service;

import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.BookService;
import integration.IntegrationTestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class BookServiceIntegrationTest extends IntegrationTestBase {

    @Container
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16");

    static JdbcTemplate jdbcTemplate;
    static BookRepo bookRepo;
    static BookService bookService;
    static AuthorRepo authorRepo;

    static Author testAuthor;

    @BeforeAll
    static void setUp() {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());

        authorRepo = new AuthorRepo(jdbcTemplate);
        bookRepo = new BookRepo(jdbcTemplate);

        authorRepo.initiateDatabase();
        bookRepo.initiateDatabase();

        bookService = new BookService(bookRepo);

        testAuthor = new Author("Initial", "Author");
        testAuthor = authorRepo.createRecord(testAuthor);
    }

    @Test
    void shouldCreateBook() {
        Book newBook = new Book("Test book", 2025);
        newBook.setAuthorId(testAuthor.getId());
        bookService.createRecord(newBook);
        List<Book> books = bookService.getAllRecords();

        assertEquals(1, books.size());
        assertEquals(newBook.getName(), books.getFirst().getName());
        assertEquals(newBook.getPublishYear(), books.getFirst().getPublishYear());
        assertEquals(newBook.getAuthorId(), books.getFirst().getAuthorId());
    }

}
