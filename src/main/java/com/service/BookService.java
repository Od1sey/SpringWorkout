package com.service;

import com.repository.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepo bookRepo;

    public List<String> getAllRecords(){
        return List.of("Record 1", "Record 2", "Record 3");
    }

}
