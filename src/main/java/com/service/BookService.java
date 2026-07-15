package com.service;

import com.entity.Book;
import com.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo){
        this.bookRepo = bookRepo;
    }

    public void createNewRecord(String name){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Book name cannot be blank");
        }
        if (name.length()<3){
            throw new IllegalArgumentException("Book name must be at least 3 characters long");
        }
        bookRepo.createRecord(name);
    }

    public List<Book> getAllRecords(){
        return bookRepo.getAllRecords();
    }

    public List<Book> getRecordsByQuery(String query){
        if ( query == null  || query.isBlank()){
            throw new IllegalArgumentException("Query cannot be blank");
        }
        if (query.length()<3) {
            throw new IllegalArgumentException("Query must be at least 3 characters long");
        }
        return bookRepo.getRecordsByQuery(query);
    }

}
