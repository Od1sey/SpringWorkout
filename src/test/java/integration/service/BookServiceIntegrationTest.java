package integration.service;

import com.entity.Author;
import com.entity.Book;
import com.exception.NoAvailableCopiesException;
import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.BookService;
import integration.IntegrationTestBase;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.*;

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

    @Test
    void shouldOnlyBorrowOneLastBookCopy() throws Exception {
        Author author = new Author("Initial", "Author");
        author = authorRepo.save(author);
        Book book = new Book("Test book", 2025);
        book.setAuthor(author);
        book.setCopiesAmount(1);

        Book savedBook = bookService.createRecord(book);
        Integer bookId = savedBook.getId();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        Callable<Boolean> borrowTask = () -> {
            latch.await();
            try {
                bookService.borrow(bookId);
                return true;
            } catch (NoAvailableCopiesException e) {
                return false;
            }
        };

        Future<Boolean> firstBorrow = executor.submit(borrowTask);
        Future<Boolean> secondBorrow = executor.submit(borrowTask);
        latch.countDown();
        boolean firstResult = firstBorrow.get();
        boolean secondResult = secondBorrow.get();
        executor.shutdown();

        int successfulBorrows = 0;
        if (firstResult) {
            successfulBorrows++;
        }
        if (secondResult) {
            successfulBorrows++;
        }

        assertEquals(1, successfulBorrows);
        Book bookFromDb = bookRepo.findById(bookId)
                .orElseThrow();
        assertEquals(0, bookFromDb.getCopiesAmount());
    }
}
