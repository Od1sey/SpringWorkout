package integration.service;

import com.entity.Author;
import com.entity.Book;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.BookService;
import integration.IntegrationTestBase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import unit.service.BookServiceTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(IntegrationTestConfig.class)
public class BookServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    BookRepo bookRepo;
    @Autowired
    BookService bookService;
    @Autowired
    AuthorRepo authorRepo;
    @Autowired
    EntityManagerFactory entityManagerFactory;

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

    @Test
    void shouldDeleteBook(){

    }

    @Test
    void shouldFailWhenAuthorIsUpdatedSimultaneously() {
        Author author = new Author("Initial", "Author");
        author = authorRepo.save(author);

        Integer authorId = author.getId();

        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        EntityTransaction transaction1 = entityManager1.getTransaction();
        EntityTransaction transaction2 = entityManager2.getTransaction();

        try {
            transaction1.begin();
            transaction2.begin();

            Author authorFromTransaction1 =
                    entityManager1.find(Author.class, authorId);

            Author authorFromTransaction2 =
                    entityManager2.find(Author.class, authorId);

            assertEquals(
                    authorFromTransaction1.getVersion(),
                    authorFromTransaction2.getVersion()
            );

            Integer initialVersion = authorFromTransaction1.getVersion();

            authorFromTransaction1.setFirstName("First");
            authorFromTransaction2.setFirstName("Second");

            assertDoesNotThrow(transaction1::commit);

            assertThrows(
                    RollbackException.class,
                    transaction2::commit
            );

            try (EntityManager verificationEntityManager =
                         entityManagerFactory.createEntityManager()) {

                Author actual = verificationEntityManager.find(
                        Author.class,
                        authorId
                );

                assertEquals("First", actual.getFirstName());

                assertEquals(
                        initialVersion + 1,
                        actual.getVersion()
                );
            }

        } finally {
            if (transaction1.isActive()) {
                transaction1.rollback();
            }

            if (transaction2.isActive()) {
                transaction2.rollback();
            }

            entityManager1.close();
            entityManager2.close();
        }
    }

}
