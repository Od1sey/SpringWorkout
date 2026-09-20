package integration.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(IntegrationTestConfig.class)
public class AuthorServiceIntegrationTest {

    @Autowired
    AuthorRepo authorRepo;
    @Autowired
    EntityManagerFactory entityManagerFactory;

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
