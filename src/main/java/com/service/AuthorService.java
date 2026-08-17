package com.service;

import com.entity.Author;
import com.repository.AuthorRepo;
import com.util.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
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
        return authorRepo.save(author);
    }

    @Transactional
    public void updateAuthorFullName(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Wrong update author info provided");
        }
        String[] inputParts = input.split("\\s*,\\s*");
        if (inputParts.length != 2) {
            throw new IllegalArgumentException("Wrong update author info provided. Expected 2 arguments, got %d".formatted(inputParts.length));
        }
        int authorId = ParseUtils.parseInt(inputParts[0], "Invalid author id");
        Author author = getRequiredById(authorId);
        String[] nameParts = inputParts[1].strip().split("\\s+");
        if (nameParts.length != 2) {
            throw new IllegalArgumentException("Wrong author name. Please enter lastname and firstname");
        }
        String lastName = nameParts[0];
        String firstName = nameParts[1];

        author.setLastName(lastName);
        author.setFirstName(firstName);
        author.setFullName(lastName + " " + firstName);
    }


    public Optional<Author> getRecordById(int id) {
        return authorRepo.findById(id);
    }

    public Author getRequiredById(int id) {
        return getRecordById(id).orElseThrow(() ->
                new IllegalArgumentException("Author with id %s doesn't exist".formatted(id)));
    }

    public List<Author> getAllRecords() {
        return authorRepo.findAll();
    }

    public void delete(int id) {
        authorRepo.delete(getRequiredById(id));
    }

}
