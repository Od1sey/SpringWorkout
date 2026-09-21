package com.service;

import com.entity.Author;
import com.entity.Book;
import com.exception.NoAvailableCopiesException;
import com.exception.RecordNotFoundException;
import com.repository.BookRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepo bookRepo;
    private final AuthorService authorService;

    @Autowired
    public BookService(BookRepo bookRepo, AuthorService authorService) {
        this.bookRepo = bookRepo;
        this.authorService = authorService;
    }

    @Transactional
    public Book createRecord(Book book) {
        Author author = authorService.getRequiredById(book.getAuthor().getId());
        book.setAuthor(author);
        return bookRepo.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllRecords() {
        return bookRepo.findAllWithAuthors();
    }

    @Transactional(readOnly = true)
    public List<Book> getRecordsByQuery(String query) {
        return bookRepo.findByNameContaining(query);
    }

    public Optional<Book> getRecordById(int id) {
        return bookRepo.findById(id);
    }

    public Book getRequiredById(int id) {
        return getRecordById(id).orElseThrow(() ->
                new RecordNotFoundException("Book", id));
    }

    public void deleteRecord(int id) {
        bookRepo.delete(getRequiredById(id));
    }

    @Transactional
    public Book updateRecord(Integer id, Book bookInfo) {
        Book book = getRequiredById(id);
        if (bookInfo.getAuthor()!=null){
            Author author = authorService.getRequiredById(bookInfo.getAuthor().getId());
            book.setAuthor(author);
        }
        book.setName(bookInfo.getName());
        book.setPublishYear(bookInfo.getPublishYear());
        bookRepo.save(book);
        return book;
    }

    @Transactional
    public Book borrow(Integer id){
        Book book = bookRepo.findByIdForUpdate(id).orElseThrow(()->{
            log.error("Error while book borrowing. Book with id {} doesn't exist", id);
            return new RecordNotFoundException("Book", id);
        });

        if (book.getCopiesAmount()==0){
            log.error("Error while book borrowing. Book with id {} has no copies left", id);
            throw new NoAvailableCopiesException(id);
        }
        book.setCopiesAmount(book.getCopiesAmount() - 1); ;
        log.info("Book borrow is successful. Book with id {} has {} copies left", id, book.getCopiesAmount());
        return book;
    }

}
