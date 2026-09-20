package com.controller;

import com.dto.AuthorRequest;
import com.dto.AuthorResponse;
import com.dto.AuthorWithBooksResponse;
import com.mapper.AuthorMapper;
import com.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<AuthorResponse>> getAll() {
        List<AuthorResponse> authors = authorService.getAllRecords().stream().map(AuthorMapper::toAuthorDTO).toList();
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AuthorWithBooksResponse> getRecord(@PathVariable("id") int id) {
        var author = authorService.getAuthor(id);
        return ResponseEntity.ok().body(AuthorMapper.toAuthorWithBooksDTO(author));
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest authorRequest) {
        var author = authorService.createRecord(AuthorMapper.toModel(authorRequest));
        var authorResponse = AuthorMapper.toAuthorDTO(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponse);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<AuthorResponse> update(
            @PathVariable(name = "id") int id,
            @Valid @RequestBody AuthorRequest authorRequest) {
        var author = authorService.updateAuthorName(id, AuthorMapper.toModel(authorRequest));
        var authorResponse = AuthorMapper.toAuthorDTO(author);
        return ResponseEntity.ok().body(authorResponse);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        authorService.delete(id);
        return ResponseEntity.ok().build();
    }

}
