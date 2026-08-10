package com.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;

    @Autowired
    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public Author buildAuthor(String fullName) {
        fullName = fullName.trim();
        String[] parts = fullName.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong author name. Please enter lastname and firstname");
        }
        return new Author(parts[0], parts[1]);
    }

    public Author createRecord(Author author) {
        return authorRepo.createRecord(author);
    }

    public Optional<Author> getRecordById(int id) {
        return authorRepo.getRecordById(id);
    }

    public List<Author> getAllRecords() {
        return authorRepo.getAllRecords();
    }

    public void delete(int id) {
        authorRepo.delete(id);
    }

}
