package integration.service;

import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.BookService;
import integration.IntegrationTestBase;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        Book createdBook = bookService.createRecord(newBook);

        assertNotNull(createdBook.getId());

        Book savedBook = bookRepo.findById(createdBook.getId())
                .orElseThrow();

        assertEquals(newBook.getName(), savedBook.getName());
        assertEquals(newBook.getPublishYear(), savedBook.getPublishYear());

        assertNotNull(savedBook.getAuthor());
        assertEquals(
                testAuthor.getId(),
                savedBook.getAuthor().getId()
        );
    }

}
