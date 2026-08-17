package integration.service;

import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.BookService;
import integration.IntegrationTestBase;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import unit.service.BookServiceTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(IntegrationTestConfig.class)
public class BookServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    BookRepo bookRepo;
    @Autowired
    BookService bookService;
    @Autowired
    AuthorRepo authorRepo;

    Author testAuthor;

    @BeforeEach
    void setUp() {
        testAuthor = new Author("Initial", "Author");
        testAuthor = authorRepo.save(testAuthor);
    }

    @Test
    @Transactional
    void shouldCreateBook() {
        Book newBook = new Book("Test book", 2025);
        newBook.setAuthor(testAuthor);
        bookService.createRecord(newBook);
        List<Book> books = bookService.getAllRecords();
        assertEquals(1, books.size());
        Book savedBook = books.getFirst();
        assertEquals(newBook.getName(), savedBook.getName());
        assertEquals(newBook.getPublishYear(), savedBook.getPublishYear());
        assertEquals(
                testAuthor.getId(),
                savedBook.getAuthor().getId()
        );
    }

}
