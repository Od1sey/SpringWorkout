package com.service;

import com.dto.AuthorWithBooks;
import com.entity.Author;
import com.exception.RecordNotFoundException;
import com.repository.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;

    @Autowired
    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Transactional
    public Author createRecord(Author author) {
        return authorRepo.save(author);
    }

    @Transactional
    public Author updateAuthorName(int id, Author authorRequest) {
        Author author = getRequiredById(id);
        author.setLastName(authorRequest.getLastName());
        author.setFirstName(authorRequest.getFirstName());
        author.setFullName(authorRequest.getLastName() + " " + authorRequest.getFirstName());
        return author;
    }

    @Transactional(readOnly = true)

    public Author getAuthor(int id) {
        var author = getRequiredById(id);
        author.setBooks(List.copyOf(author.getBooks()));
        return author;
    }

    public Optional<Author> getAuthorById(int id) {
        return authorRepo.findById(id);
    }

    public Author getRequiredById(int id) {
        return getAuthorById(id).orElseThrow(() ->
                new RecordNotFoundException("Author", id));
    }

    public List<Author> getAllRecords() {
        return authorRepo.findAll();
    }

    @Transactional
    public void delete(int id) {
        authorRepo.delete(getRequiredById(id));
    }

}
